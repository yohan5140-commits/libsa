import { useEffect, useState } from 'react';
import { fetchPopularBooks } from './api/popularBooks';
import type { PopularBook } from './types/PopularBook';
import PopularBookList from './components/PopularBookList';

const DEFAULT_LIB_CODE = '149016';
const DEFAULT_START_DT = '2026-01-01';
const DEFAULT_END_DT = '2026-07-31';
const DEFAULT_PAGE_SIZE = 100;

interface Query {
  libCode: string;
  startDt: string;
  endDt: string;
  pageSize: number;
}

export default function App() {
  const [libCodeInput, setLibCodeInput] = useState(DEFAULT_LIB_CODE);
  const [startDtInput, setStartDtInput] = useState(DEFAULT_START_DT);
  const [endDtInput, setEndDtInput] = useState(DEFAULT_END_DT);
  const [query, setQuery] = useState<Query>({
    libCode: DEFAULT_LIB_CODE,
    startDt: DEFAULT_START_DT,
    endDt: DEFAULT_END_DT,
    pageSize: DEFAULT_PAGE_SIZE,
  });
  const [books, setBooks] = useState<PopularBook[]>([]);
  const [resolvedRange, setResolvedRange] = useState<{ startDt: string | null; endDt: string | null }>({
    startDt: null,
    endDt: null,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    setLoading(true);
    setError(null);

    fetchPopularBooks(query)
      .then((data) => {
        if (cancelled) return;
        setBooks(data.books);
        setResolvedRange({ startDt: data.startDt, endDt: data.endDt });
      })
      .catch((err: Error) => {
        if (!cancelled) setError(err.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [query]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setQuery({
      libCode: libCodeInput.trim() || DEFAULT_LIB_CODE,
      startDt: startDtInput || DEFAULT_START_DT,
      endDt: endDtInput || DEFAULT_END_DT,
      pageSize: DEFAULT_PAGE_SIZE,
    });
  };

  return (
    <main className="app">
      <h1>인기대출도서</h1>
      <form className="lib-form" onSubmit={handleSubmit}>
        <label htmlFor="libCode">도서관 코드</label>
        <input
          id="libCode"
          value={libCodeInput}
          onChange={(e) => setLibCodeInput(e.target.value)}
          inputMode="numeric"
        />
        <label htmlFor="startDt">대출기간</label>
        <input
          id="startDt"
          type="date"
          value={startDtInput}
          onChange={(e) => setStartDtInput(e.target.value)}
        />
        <span>~</span>
        <input
          id="endDt"
          type="date"
          value={endDtInput}
          onChange={(e) => setEndDtInput(e.target.value)}
        />
        <button type="submit">조회</button>
      </form>

      {!loading && !error && (resolvedRange.startDt || resolvedRange.endDt) && (
        <p className="range-note">
          조회기간: {resolvedRange.startDt ?? '-'} ~ {resolvedRange.endDt ?? '-'}
        </p>
      )}

      {loading && <p className="status">불러오는 중...</p>}
      {error && <p className="status error">{error}</p>}
      {!loading && !error && <PopularBookList books={books} />}
    </main>
  );
}

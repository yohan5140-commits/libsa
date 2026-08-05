import { useEffect, useState } from 'react';
import { fetchPopularBooks } from './api/popularBooks';
import type { PopularBook } from './types/PopularBook';
import PopularBookList from './components/PopularBookList';

const DEFAULT_LIB_CODE = '149016';

export default function App() {
  const [libCodeInput, setLibCodeInput] = useState(DEFAULT_LIB_CODE);
  const [libCode, setLibCode] = useState(DEFAULT_LIB_CODE);
  const [books, setBooks] = useState<PopularBook[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    setLoading(true);
    setError(null);

    fetchPopularBooks(libCode)
      .then((data) => {
        if (!cancelled) setBooks(data.books);
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
  }, [libCode]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLibCode(libCodeInput.trim() || DEFAULT_LIB_CODE);
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
        <button type="submit">조회</button>
      </form>

      {loading && <p className="status">불러오는 중...</p>}
      {error && <p className="status error">{error}</p>}
      {!loading && !error && <PopularBookList books={books} />}
    </main>
  );
}

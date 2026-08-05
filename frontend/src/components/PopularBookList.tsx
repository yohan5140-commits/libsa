import type { PopularBook } from '../types/PopularBook';

interface Props {
  books: PopularBook[];
}

export default function PopularBookList({ books }: Props) {
  if (books.length === 0) {
    return <p className="empty">표시할 도서가 없습니다.</p>;
  }

  return (
    <ul className="book-list">
      {books.map((book) => (
        <li key={`${book.ranking}-${book.isbn13 ?? book.title}`} className="book-card">
          <span className="rank">{book.ranking}</span>
          <img
            className="cover"
            src={book.bookImageUrl ?? undefined}
            alt={`${book.title} 표지`}
            loading="lazy"
            onError={(e) => {
              (e.currentTarget as HTMLImageElement).style.visibility = 'hidden';
            }}
          />
          <div className="info">
            <a
              className="title"
              href={book.bookDetailUrl ?? undefined}
              target="_blank"
              rel="noreferrer"
            >
              {book.title}
            </a>
            <p className="meta">
              {book.authors} · {book.publisher} {book.publicationYear ? `(${book.publicationYear})` : ''}
            </p>
            <p className="loan-count">대출 {book.loanCount.toLocaleString()}회</p>
          </div>
        </li>
      ))}
    </ul>
  );
}

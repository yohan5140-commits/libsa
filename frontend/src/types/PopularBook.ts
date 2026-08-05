export interface PopularBook {
  order: number;
  title: string;
  authors: string;
  publisher: string;
  publicationYear: string | null;
  isbn13: string | null;
  loanCount: number | null;
  bookImageUrl: string | null;
  bookDetailUrl: string | null;
}

export interface PopularBookListResponse {
  libCode: string;
  startDt: string | null;
  endDt: string | null;
  resultNum: number;
  books: PopularBook[];
}

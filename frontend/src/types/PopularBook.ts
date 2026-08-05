export interface PopularBook {
  ranking: number;
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
  resultNum: number;
  books: PopularBook[];
}

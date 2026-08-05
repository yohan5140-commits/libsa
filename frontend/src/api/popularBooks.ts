import type { PopularBookListResponse } from '../types/PopularBook';

export interface PopularBooksQuery {
  libCode: string;
  pageSize?: number;
  startDt?: string;
  endDt?: string;
}

export async function fetchPopularBooks({
  libCode,
  pageSize = 30,
  startDt,
  endDt,
}: PopularBooksQuery): Promise<PopularBookListResponse> {
  const params = new URLSearchParams({ libCode, pageSize: String(pageSize) });
  if (startDt) params.set('startDt', startDt);
  if (endDt) params.set('endDt', endDt);

  const res = await fetch(`/api/popular-books?${params.toString()}`);

  if (!res.ok) {
    const body = await res.json().catch(() => null);
    throw new Error(body?.message ?? `요청이 실패했습니다. (HTTP ${res.status})`);
  }

  return res.json();
}

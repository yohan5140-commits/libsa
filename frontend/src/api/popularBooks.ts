import type { PopularBookListResponse } from '../types/PopularBook';

export async function fetchPopularBooks(libCode: string, pageSize = 30): Promise<PopularBookListResponse> {
  const params = new URLSearchParams({ libCode, pageSize: String(pageSize) });
  const res = await fetch(`/api/popular-books?${params.toString()}`);

  if (!res.ok) {
    const body = await res.json().catch(() => null);
    throw new Error(body?.message ?? `요청이 실패했습니다. (HTTP ${res.status})`);
  }

  return res.json();
}

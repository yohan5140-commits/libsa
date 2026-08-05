# libsa — 인기대출도서 목록

[정보나루(data4library.kr)](https://www.data4library.kr) Open API의 `loanItemSrchByLib`
(도서관별 인기대출도서) 엔드포인트를 이용해 특정 도서관의 인기대출도서 목록을 보여주는
풀스택 예제입니다.

- `backend/` — Spring Boot 3 (Java 21, Gradle). data4library API를 호출해 프런트엔드가
  쓰기 쉬운 JSON으로 가공해주는 프록시 서버입니다. API 인증키를 서버에서만 다루므로
  브라우저에 키가 노출되지 않습니다.
- `frontend/` — React 18 + TypeScript (Vite). 백엔드의 `/api/popular-books`를 호출해
  순위, 표지, 서명, 저자, 출판사, 대출횟수를 보여줍니다.

## 사전 준비: API 인증키 발급

1. https://www.data4library.kr 에서 회원가입 후 오픈API 인증키를 신청합니다.
2. 발급받은 인증키는 **절대로 소스코드나 git에 커밋하지 마세요.** 이 프로젝트는 인증키를
   환경변수 `DATA4LIBRARY_AUTH_KEY`로만 주입받습니다.

> 채팅으로 공유된 인증키는 절대 저장소에 커밋되지 않았습니다. 로컬에서 아래 환경변수로만
> 설정해서 사용하세요. 혹시 그 키가 외부에 노출되었을 가능성이 있다면 정보나루 마이페이지에서
> 재발급하는 것을 권장합니다.

## 백엔드 실행

```bash
cd backend
export DATA4LIBRARY_AUTH_KEY=발급받은인증키
./gradlew bootRun
```

기본 포트는 `8080`이며, 기본 도서관 코드는 `149016`입니다(`application.yml`의
`data4library.default-lib-code`, 또는 요청 시 `libCode` 쿼리 파라미터로 변경 가능).

### API

```
GET /api/popular-books?libCode=149016&pageSize=30
```

```json
{
  "libCode": "149016",
  "resultNum": 30,
  "books": [
    {
      "ranking": 1,
      "title": "책 제목",
      "authors": "저자",
      "publisher": "출판사",
      "publicationYear": "2024",
      "isbn13": "9791100000000",
      "loanCount": 123,
      "bookImageUrl": "https://...",
      "bookDetailUrl": "https://..."
    }
  ]
}
```

## 프런트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

`http://localhost:5173`에서 확인할 수 있습니다. Vite dev 서버는 `/api` 요청을
`http://localhost:8080`(백엔드)으로 프록시합니다(`vite.config.ts`).

배포용 빌드:

```bash
npm run build
```

## 환경변수 요약

| 변수 | 위치 | 설명 |
| --- | --- | --- |
| `DATA4LIBRARY_AUTH_KEY` | backend | data4library.kr 오픈API 인증키 (필수) |
| `CORS_ALLOWED_ORIGINS` | backend | 프런트엔드 오리진, 기본값 `http://localhost:5173` |

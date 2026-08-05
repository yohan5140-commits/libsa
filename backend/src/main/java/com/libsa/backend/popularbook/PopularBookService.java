package com.libsa.backend.popularbook;

import com.libsa.backend.popularbook.dto.Data4LibraryApiResponse;
import com.libsa.backend.popularbook.dto.PopularBookDto;
import com.libsa.backend.popularbook.dto.PopularBookListResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
public class PopularBookService {

    private final RestClient restClient;
    private final Data4LibraryProperties properties;

    public PopularBookService(RestClient.Builder restClientBuilder, Data4LibraryProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.baseUrl()).build();
        this.properties = properties;
    }

    public PopularBookListResponse getPopularBooks(String libCode, int pageSize, LocalDate startDt, LocalDate endDt) {
        if (properties.authKey() == null || properties.authKey().isBlank()) {
            throw new Data4LibraryConfigurationException(
                    "data4library.auth-key is not configured. Set the DATA4LIBRARY_AUTH_KEY environment variable.");
        }

        if (startDt != null && endDt != null && startDt.isAfter(endDt)) {
            throw new IllegalArgumentException("startDt must not be after endDt");
        }

        String resolvedLibCode = (libCode == null || libCode.isBlank()) ? properties.defaultLibCode() : libCode;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/loanItemSrchByLib")
                .queryParam("authKey", properties.authKey())
                .queryParam("libCode", resolvedLibCode)
                .queryParam("pageSize", pageSize)
                .queryParam("format", "json");

        if (startDt != null) {
            uriBuilder.queryParam("startDt", startDt);
        }
        if (endDt != null) {
            uriBuilder.queryParam("endDt", endDt);
        }

        Data4LibraryApiResponse apiResponse = restClient.get()
                .uri(uriBuilder.build().toUriString())
                .retrieve()
                .body(Data4LibraryApiResponse.class);

        if (apiResponse == null || apiResponse.response() == null) {
            return new PopularBookListResponse(resolvedLibCode, null, null, 0, List.of());
        }

        if (apiResponse.response().errCode() != null) {
            throw new Data4LibraryUpstreamException(
                    "data4library.kr API error [" + apiResponse.response().errCode() + "]: "
                            + apiResponse.response().error());
        }

        List<Data4LibraryApiResponse.Doc> docs = apiResponse.response().docs() == null
                ? List.of()
                : apiResponse.response().docs().stream()
                .map(Data4LibraryApiResponse.DocWrapper::doc)
                .filter(Objects::nonNull)
                .toList();

        List<PopularBookDto> books = IntStream.range(0, docs.size())
                .mapToObj(i -> toDto(docs.get(i), i + 1))
                .toList();

        int resultNum = apiResponse.response().resultNum() == null ? books.size() : apiResponse.response().resultNum();
        Data4LibraryApiResponse.RequestInfo requestInfo = apiResponse.response().request();
        String resolvedStartDt = requestInfo == null ? null : requestInfo.startDt();
        String resolvedEndDt = requestInfo == null ? null : requestInfo.endDt();

        return new PopularBookListResponse(resolvedLibCode, resolvedStartDt, resolvedEndDt, resultNum, books);
    }

    private PopularBookDto toDto(Data4LibraryApiResponse.Doc doc, int order) {
        return new PopularBookDto(
                order,
                doc.bookname() == null ? "" : doc.bookname().trim(),
                doc.authors() == null ? "" : doc.authors().trim(),
                doc.publisher() == null ? "" : doc.publisher().trim(),
                doc.publicationYear(),
                doc.isbn13(),
                parseLongOrNull(doc.loanCount()),
                doc.bookImageUrl(),
                doc.bookDetailUrl()
        );
    }

    private Long parseLongOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

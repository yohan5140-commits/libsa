package com.libsa.backend.popularbook;

import com.libsa.backend.popularbook.dto.Data4LibraryApiResponse;
import com.libsa.backend.popularbook.dto.PopularBookDto;
import com.libsa.backend.popularbook.dto.PopularBookListResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
public class PopularBookService {

    private final RestClient restClient;
    private final Data4LibraryProperties properties;

    public PopularBookService(RestClient.Builder restClientBuilder, Data4LibraryProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.baseUrl()).build();
        this.properties = properties;
    }

    public PopularBookListResponse getPopularBooks(String libCode, int pageSize) {
        if (properties.authKey() == null || properties.authKey().isBlank()) {
            throw new Data4LibraryConfigurationException(
                    "data4library.auth-key is not configured. Set the DATA4LIBRARY_AUTH_KEY environment variable.");
        }

        String resolvedLibCode = (libCode == null || libCode.isBlank()) ? properties.defaultLibCode() : libCode;

        String uri = UriComponentsBuilder.fromPath("/loanItemSrchByLib")
                .queryParam("authKey", properties.authKey())
                .queryParam("libCode", resolvedLibCode)
                .queryParam("pageSize", pageSize)
                .queryParam("format", "json")
                .build()
                .toUriString();

        Data4LibraryApiResponse apiResponse = restClient.get()
                .uri(uri)
                .retrieve()
                .body(Data4LibraryApiResponse.class);

        if (apiResponse == null || apiResponse.response() == null) {
            return new PopularBookListResponse(resolvedLibCode, 0, List.of());
        }

        if (apiResponse.response().errCode() != null) {
            throw new Data4LibraryUpstreamException(
                    "data4library.kr API error [" + apiResponse.response().errCode() + "]: "
                            + apiResponse.response().error());
        }

        List<PopularBookDto> books = apiResponse.response().docs() == null
                ? List.of()
                : apiResponse.response().docs().stream()
                .map(Data4LibraryApiResponse.DocWrapper::doc)
                .filter(Objects::nonNull)
                .map(this::toDto)
                .toList();

        int resultNum = apiResponse.response().resultNum() == null ? books.size() : apiResponse.response().resultNum();

        return new PopularBookListResponse(resolvedLibCode, resultNum, books);
    }

    private PopularBookDto toDto(Data4LibraryApiResponse.Doc doc) {
        return new PopularBookDto(
                parseIntSafely(doc.ranking()),
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

    private int parseIntSafely(String value) {
        try {
            return value == null ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
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

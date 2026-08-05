package com.libsa.backend.popularbook.dto;

public record PopularBookDto(
        int order,
        String title,
        String authors,
        String publisher,
        String publicationYear,
        String isbn13,
        Long loanCount,
        String bookImageUrl,
        String bookDetailUrl
) {
}

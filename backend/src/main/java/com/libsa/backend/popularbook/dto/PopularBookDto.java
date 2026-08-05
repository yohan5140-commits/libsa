package com.libsa.backend.popularbook.dto;

public record PopularBookDto(
        int ranking,
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

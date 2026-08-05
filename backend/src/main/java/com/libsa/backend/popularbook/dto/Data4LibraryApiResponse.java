package com.libsa.backend.popularbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Data4LibraryApiResponse(@JsonProperty("response") ResponseBody response) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResponseBody(
            @JsonProperty("request") RequestInfo request,
            @JsonProperty("resultNum") Integer resultNum,
            @JsonProperty("numFound") Integer numFound,
            @JsonProperty("pageNo") Integer pageNo,
            @JsonProperty("pageSize") Integer pageSize,
            @JsonProperty("docs") List<DocWrapper> docs,
            @JsonProperty("errCode") String errCode,
            @JsonProperty("error") String error
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RequestInfo(
            @JsonProperty("startDt") String startDt,
            @JsonProperty("endDt") String endDt
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DocWrapper(@JsonProperty("doc") Doc doc) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Doc(
            @JsonProperty("no") String no,
            @JsonProperty("ranking") String ranking,
            @JsonProperty("bookname") String bookname,
            @JsonProperty("authors") String authors,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("publication_year") String publicationYear,
            @JsonProperty("isbn13") String isbn13,
            @JsonProperty("vol") String vol,
            @JsonProperty("class_no") String classNo,
            @JsonProperty("class_nm") String className,
            @JsonProperty("loan_count") String loanCount,
            @JsonProperty("bookImageURL") String bookImageUrl,
            @JsonProperty("bookDtlUrl") String bookDetailUrl
    ) {
    }
}

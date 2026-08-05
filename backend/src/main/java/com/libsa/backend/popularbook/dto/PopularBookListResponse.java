package com.libsa.backend.popularbook.dto;

import java.util.List;

public record PopularBookListResponse(String libCode, int resultNum, List<PopularBookDto> books) {
}

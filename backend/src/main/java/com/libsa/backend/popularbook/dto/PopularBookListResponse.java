package com.libsa.backend.popularbook.dto;

import java.util.List;

public record PopularBookListResponse(
        String libCode,
        String startDt,
        String endDt,
        int resultNum,
        List<PopularBookDto> books
) {
}

package com.libsa.backend.popularbook;

import com.libsa.backend.popularbook.dto.PopularBookListResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PopularBookController {

    private final PopularBookService popularBookService;

    public PopularBookController(PopularBookService popularBookService) {
        this.popularBookService = popularBookService;
    }

    @GetMapping("/api/popular-books")
    public PopularBookListResponse getPopularBooks(
            @RequestParam(required = false) String libCode,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int pageSize) {
        return popularBookService.getPopularBooks(libCode, pageSize);
    }
}

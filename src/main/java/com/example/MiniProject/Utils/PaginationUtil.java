package com.example.MiniProject.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static Pageable createPageable(int pageNo, int pageSize, String sortBy, String sortDir) {

        if (pageNo < 0) {
            pageNo = 0;
        }

        pageSize = (pageSize <= 0 || pageSize > 50) ? 10 : pageSize;

        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNo, pageSize, sort);
    }
}

package com.skhu.moodfriend.global.dto;

import java.util.List;

public record PagedResponse<T>(
        List<T> content, // 페이지에 포함된 데이터 목록
        int pageNumber, // 현재 페이지 번호
        int pageSize, // 페이지 당 항목 수
        long totalElements, // 전체 항목 수
        int totalPages // 전체 페이지 수
) {
}
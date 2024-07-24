package com.skhu.moodfriend.app.dto.hospital.reqDto;

public record KakaoMapKeywordReqDto(
        String query,
        String x,
        String y,
        String radius,
        Integer count // Null이 들어올 수 있도록 수정
) {
}

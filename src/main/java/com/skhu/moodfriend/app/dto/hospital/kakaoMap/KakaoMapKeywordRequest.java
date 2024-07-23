package com.skhu.moodfriend.app.dto.hospital.kakaoMap;

public record KakaoMapKeywordRequest(
        String query,
        String x, // longitude
        String y, // latitude
        String radius,
        Integer count // Null이 들어올 수 있도록 수정
) {
}

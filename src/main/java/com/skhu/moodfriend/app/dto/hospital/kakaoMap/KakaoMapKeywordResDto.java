package com.skhu.moodfriend.app.dto.hospital.kakaoMap;

public record KakaoMapKeywordResDto(
        String categoryName,
        String placeName,
        Integer distance,
        String placeUrl
) {}

package com.skhu.moodfriend.app.dto.hospital.resDto;

public record KakaoMapKeywordResDto(
        String categoryName,
        String placeName,
        Integer distance,
        String placeUrl
) {}

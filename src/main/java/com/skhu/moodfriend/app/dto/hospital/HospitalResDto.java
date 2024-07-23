package com.skhu.moodfriend.app.dto.hospital;

public record HospitalResDto(
        String categoryName,
        String placeName,
        Integer distance,
        String placeUrl
) {
}
package com.skhu.moodfriend.app.dto.hospital.kakaoMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMapDocument(
        @JsonProperty("place_name")
        String placeName,
        Integer distance,
        @JsonProperty("place_url")
        String placeUrl,
        @JsonProperty("category_name")
        String categoryName,
        @JsonProperty("address_name")
        String addressName,
        @JsonProperty("road_address_name")
        String roadAddressName,
        String id,
        String phone,
        @JsonProperty("category_group_code")
        String categoryGroupCode,
        @JsonProperty("category_group_name")
        String categoryGroupName,
        String x,
        String y
) {}
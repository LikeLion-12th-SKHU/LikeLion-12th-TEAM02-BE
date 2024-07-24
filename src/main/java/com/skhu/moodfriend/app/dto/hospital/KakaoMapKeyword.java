package com.skhu.moodfriend.app.dto.hospital;

import lombok.Builder;

import java.util.List;

@Builder
public record KakaoMapKeyword(
        List<KakaoMapDocument> documents
) {
}
package com.skhu.moodfriend.app.dto.auth;

import lombok.Builder;

@Builder
public record RefreshTokenParsingDto(
        Long memberId
) {
}

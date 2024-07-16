package com.skhu.moodfriend.app.dto.auth;

import lombok.Builder;

@Builder
public record RenewAccessTokenDto(
        String renewAccessToken
) {
}

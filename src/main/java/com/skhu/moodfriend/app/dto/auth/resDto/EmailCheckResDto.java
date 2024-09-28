package com.skhu.moodfriend.app.dto.auth.resDto;

public record EmailCheckResDto(
        boolean isDuplicated
) {
    public static EmailCheckResDto from(boolean isDuplicated) {
        return new EmailCheckResDto(isDuplicated);
    }
}

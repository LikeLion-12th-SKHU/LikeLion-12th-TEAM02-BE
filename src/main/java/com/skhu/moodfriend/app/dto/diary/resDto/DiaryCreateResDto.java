package com.skhu.moodfriend.app.dto.diary.resDto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DiaryCreateResDto(
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

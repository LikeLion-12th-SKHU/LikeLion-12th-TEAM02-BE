package com.skhu.moodfriend.app.dto.diary.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DiaryCreateResDto(
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime updatedAt
) {
}

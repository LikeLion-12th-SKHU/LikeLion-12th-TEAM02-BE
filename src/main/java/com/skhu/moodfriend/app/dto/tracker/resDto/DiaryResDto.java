package com.skhu.moodfriend.app.dto.tracker.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DiaryResDto(
        String emotionType,
        String weatherType,
        String content,
        LocalDate createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime updatedAt
) {
}

package com.skhu.moodfriend.app.dto.tracker.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skhu.moodfriend.app.entity.diary.EmotionType;
import com.skhu.moodfriend.app.entity.diary.WeatherType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DiaryResDto(
        EmotionType emotionType,
        WeatherType weatherType,
        String title,
        String content,
        LocalDate createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime updatedAt
) {
}

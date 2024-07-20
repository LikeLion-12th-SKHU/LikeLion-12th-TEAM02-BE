package com.skhu.moodfriend.app.dto.tracker.reqDto;

import com.skhu.moodfriend.app.entity.diary.EmotionType;
import com.skhu.moodfriend.app.entity.diary.WeatherType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DiaryUpdateReqDto(
        EmotionType emotionType,
        WeatherType weatherType,

        @Max(value = 30, message = "제목은 최대 30자까지 입력 가능합니다.")
        String title,

        @Max(value = 1024, message = "내용은 최대 1024자까지 입력 가능합니다.")
        String content,

        @NotNull(message = "작성일은 필수 입력 항목입니다.")
        @PastOrPresent(message = "작성일은 과거 또는 현재 날짜여야 합니다.")
        LocalDate createdAt
) {
}

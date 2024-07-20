package com.skhu.moodfriend.app.dto.tracker.reqDto;

import com.skhu.moodfriend.app.entity.diary.EmotionType;
import com.skhu.moodfriend.app.entity.diary.WeatherType;
import com.skhu.moodfriend.global.exception.EnumValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DiaryCreateReqDto(
        @NotNull(message = "감정은 필수 입력 항목입니다.")
        @EnumValid(enumClass = EmotionType.class, message = "올바르지 않은 감정 타입 값입니다.")
        EmotionType emotionType,

        @NotNull(message = "날씨는 필수 입력 항목입니다.")
        @EnumValid(enumClass = WeatherType.class, message = "올바르지 않은 날씨 타입 값입니다.")
        WeatherType weatherType,

        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        @Size(max = 30, message = "제목은 최대 30자까지 입력 가능합니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        @Size(max = 1024, message = "내용은 최대 1024자까지 입력 가능합니다.")
        String content,

        @NotNull(message = "작성일은 필수 입력 항목입니다.")
        @PastOrPresent(message = "작성일은 과거 또는 현재 날짜여야 합니다.")
        LocalDate createdAt
) {
}

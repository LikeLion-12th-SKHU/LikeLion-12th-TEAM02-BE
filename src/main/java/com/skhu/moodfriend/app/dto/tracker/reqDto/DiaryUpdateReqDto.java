package com.skhu.moodfriend.app.dto.tracker.reqDto;

import com.skhu.moodfriend.app.entity.diary.EmotionType;
import com.skhu.moodfriend.app.entity.diary.WeatherType;

import java.time.LocalDate;

public record DiaryUpdateReqDto(
        EmotionType emotionType,
        WeatherType weatherType,
        String title,
        String content,
        LocalDate createdAt
) {
}

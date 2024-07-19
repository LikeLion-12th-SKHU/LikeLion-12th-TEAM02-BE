package com.skhu.moodfriend.app.dto.tracker.reqDto;

import com.skhu.moodfriend.app.entity.diary.EmotionType;
import com.skhu.moodfriend.app.entity.diary.WeatherType;

public record DiaryCreateReqDto(
        EmotionType emotionType,
        WeatherType weatherType,
        String content
) {
}

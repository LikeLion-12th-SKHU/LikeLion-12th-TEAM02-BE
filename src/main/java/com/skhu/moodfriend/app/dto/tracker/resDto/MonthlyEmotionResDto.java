package com.skhu.moodfriend.app.dto.tracker.resDto;

import com.skhu.moodfriend.app.entity.diary.EmotionType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;

@Builder
public record MonthlyEmotionResDto(
        Map<LocalDate, EmotionType> dailyEmotions
) {
}
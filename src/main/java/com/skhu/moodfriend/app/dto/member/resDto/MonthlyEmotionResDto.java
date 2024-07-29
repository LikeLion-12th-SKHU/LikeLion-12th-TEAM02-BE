package com.skhu.moodfriend.app.dto.member.resDto;

import com.skhu.moodfriend.app.domain.tracker.diary.EmotionType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;

@Builder
public record MonthlyEmotionResDto(
        Map<LocalDate, EmotionType> dailyEmotions
) {
}

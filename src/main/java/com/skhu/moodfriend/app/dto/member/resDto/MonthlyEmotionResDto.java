package com.skhu.moodfriend.app.dto.member.resDto;

import com.skhu.moodfriend.app.domain.tracker.diary.Diary;
import com.skhu.moodfriend.app.domain.tracker.diary.EmotionType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Builder
public record MonthlyEmotionResDto(
        Map<LocalDate, EmotionType> dailyEmotions
) {
    public static MonthlyEmotionResDto of(List<Diary> diaries) {
        Map<LocalDate, EmotionType> dailyEmotions = diaries.stream()
                .collect(Collectors.toMap(
                        Diary::getCreatedAt,
                        Diary::getEmotionType,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
        return MonthlyEmotionResDto.builder()
                .dailyEmotions(dailyEmotions)
                .build();
    }
}

package com.skhu.moodfriend.app.dto.tracker.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryAIResDto(
        Long diaryAIId,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate createdAt,
        String summary
) {
    public static DiaryAIResDto of(DiaryAI diaryAI) {
        return DiaryAIResDto.builder()
                .diaryAIId(diaryAI.getDiaryAIId())
                .createdAt(diaryAI.getCreatedAt())
                .summary(diaryAI.getSummary())
                .build();
    }
}

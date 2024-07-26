package com.skhu.moodfriend.app.dto.tracker.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryAIResDto(
        Long diaryAIId,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate createdAt,
        String summary
) {
}

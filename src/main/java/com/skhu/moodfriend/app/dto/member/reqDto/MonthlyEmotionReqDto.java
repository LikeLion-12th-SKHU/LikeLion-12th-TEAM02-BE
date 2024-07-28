package com.skhu.moodfriend.app.dto.member.reqDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

public record MonthlyEmotionReqDto(
        @NotNull(message = "년도-월은 필수 입력 항목입니다.")
        @DateTimeFormat(pattern = "yyyy-MM")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
        YearMonth yearMonth
) {
}

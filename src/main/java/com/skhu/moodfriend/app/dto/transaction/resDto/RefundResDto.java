package com.skhu.moodfriend.app.dto.transaction.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RefundResDto(
        Long orderId,
        Integer refundedAmount,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime refundAt
) {
}

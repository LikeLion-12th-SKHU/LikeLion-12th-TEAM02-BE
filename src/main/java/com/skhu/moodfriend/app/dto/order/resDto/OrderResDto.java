package com.skhu.moodfriend.app.dto.order.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderResDto(
        Long orderId,
        String productName,
        int price,
        String impUid,
        String merchantUid,
        int updatedMileage,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
}

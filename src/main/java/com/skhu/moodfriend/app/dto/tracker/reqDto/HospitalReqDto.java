package com.skhu.moodfriend.app.dto.tracker.reqDto;

public record HospitalReqDto(
        String query,
        String x,
        String y,
        String radius,
        Integer count
) {
}

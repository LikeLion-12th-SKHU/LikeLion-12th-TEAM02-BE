package com.skhu.moodfriend.app.dto.member.reqDto;

public record HospitalReqDto(
        String query,
        String x,
        String y,
        String radius,
        Integer count
) {
}

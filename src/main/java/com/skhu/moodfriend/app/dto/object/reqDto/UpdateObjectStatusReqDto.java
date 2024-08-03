package com.skhu.moodfriend.app.dto.object.reqDto;

public record UpdateObjectStatusReqDto(
        Long memberObjectId,
        boolean status
) {
}

package com.skhu.moodfriend.app.dto.member.reqDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateReqDto(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Size(max = 10, message = "이름은 최대 10자까지 입력 가능합니다.")
        String name
) {
}

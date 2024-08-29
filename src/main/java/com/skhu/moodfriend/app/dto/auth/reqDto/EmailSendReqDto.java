package com.skhu.moodfriend.app.dto.auth.reqDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailSendReqDto(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "이메일 형식에 알맞지 않습니다."
        )
        String email
) {
}

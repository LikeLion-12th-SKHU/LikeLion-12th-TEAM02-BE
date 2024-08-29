package com.skhu.moodfriend.app.dto.auth.reqDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailVerifyReqDto(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "이메일 형식에 알맞지 않습니다."
        )
        String email,

        @NotBlank(message = "인증 코드는 필수 입력 항목입니다.")
        @Size(min = 8, max = 8, message = "인증 코드는 8자리입니다.")
        String code
) {
}

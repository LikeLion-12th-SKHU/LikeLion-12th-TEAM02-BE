package com.skhu.moodfriend.app.dto.auth.reqDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpReqDto(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "이메일 형식에 알맞지 않습니다."
        )
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,16}$",
                message = "비밀번호는 영문, 숫자, 특수문자 조합의 10자 이상 16자 이하여야 합니다."
        )
        String password,

        @NotBlank(message = "비밀번호 재확인은 필수 입력 항목입니다.")
        String confirmPassword
) {
}

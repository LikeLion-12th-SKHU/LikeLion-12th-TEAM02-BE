package com.skhu.moodfriend.app.controller.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.LoginReqDto;
import com.skhu.moodfriend.app.dto.auth.reqDto.SignUpReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.LoginResDto;
import com.skhu.moodfriend.app.dto.auth.resDto.SignUpResDto;
import com.skhu.moodfriend.app.service.auth.LoginService;
import com.skhu.moodfriend.app.service.auth.SignUpService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "회원가입/로그인", description = "회원가입/로그인을 담당하는 api 그룹")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SignUpService signUpService;
    private final LoginService loginService;

    @PostMapping("/signUp")
    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 받아 DB에 저장합니다. refreshToken, accessToken을 발급하여 회원가입을 진행하고 사용자 정보와 함께 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "403", description = "url문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<SignUpResDto>> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        ApiResponseTemplate<SignUpResDto> data = signUpService.signUp(signUpReqDto);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "사용자의 이메일, 비밀번호를 받습니다. 암호화되어 저장된 비밀번호와 일치하는지 검사하고, 로그인을 통해서 갱신된 refreshToken, accessToken을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 암호 입력"),
                    @ApiResponse(responseCode = "404", description = "이메일을 찾을수 없거나, 잘못된 값을 입력"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<LoginResDto>> login(@RequestBody LoginReqDto loginReqDto) {
        ApiResponseTemplate<LoginResDto> data = loginService.login(loginReqDto);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

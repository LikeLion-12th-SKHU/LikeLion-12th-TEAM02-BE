package com.skhu.moodfriend.app.controller.auth;

import com.skhu.moodfriend.app.dto.auth.resDto.OAuthResDto;
import com.skhu.moodfriend.app.service.auth.GoogleOAuthService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "소셜 회원가입/로그인", description = "소셜 회원가입/로그인을 담당하는 api 그룹")
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    private final GoogleOAuthService googleOauthService;

    @GetMapping("/callback/google")
    @Operation(
            summary = "구글 소셜 회원가입/로그인 콜백",
            description = "구글 로그인 후 리다이렉션된 URI입니다. 인가 코드를 받아서 accessToken을 요청하고, 회원가입 또는 로그인을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입/로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<OAuthResDto>> googleCallback(@RequestParam(name = "code") String code) {
        ApiResponseTemplate<OAuthResDto> data = googleOauthService.signUpOrLogin(googleOauthService.getGoogleAccessToken(code).getData());
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

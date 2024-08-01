package com.skhu.moodfriend.app.controller.auth;

import com.skhu.moodfriend.app.service.auth.LogoutService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "로그아웃/회원탈퇴", description = "로그아웃/회원탈퇴를 담당하는 api 그룹")
@RequestMapping("/api/v1/account")
public class AccountController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "사용자의 refreshToken을 삭제하여 로그아웃을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<Void>> logout(Principal principal) {
        ApiResponseTemplate<Void> response = logoutService.logout(principal);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

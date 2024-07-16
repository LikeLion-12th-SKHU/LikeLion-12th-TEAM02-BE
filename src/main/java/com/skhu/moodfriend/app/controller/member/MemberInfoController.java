package com.skhu.moodfriend.app.controller.member;

import com.skhu.moodfriend.app.dto.member.resDto.MemberInfoResDto;
import com.skhu.moodfriend.app.service.member.MemberInfoService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "사용자 정보", description = "사용자 정보를 관리하는 api 그룹")
@RequestMapping("/api/v1/member/info")
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @GetMapping
    @Operation(
            summary = "사용자 정보 조회",
            description = "현재 로그인된 사용자의 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            })
    public ResponseEntity<ApiResponseTemplate<MemberInfoResDto>> getMemberInfo(Principal principal) {
        ApiResponseTemplate<MemberInfoResDto> data = memberInfoService.getMemberInfo(principal);

        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

package com.skhu.moodfriend.app.controller.chat;

import com.skhu.moodfriend.app.dto.chat.resDto.HoyaResDto;
import com.skhu.moodfriend.app.service.chat.HoyaService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "호야 챗봇", description = "호야 챗봇을 담당하는 api 그룹")
@RequestMapping("/api/v1/hoya/chat")
public class HoyaController {

    private final HoyaService hoyaService;

    @PostMapping
    @Operation(
            summary = "챗봇 응답",
            description = "사용자의 프롬프트에 대한 챗봇 응답을 받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "챗봇 응답 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 or 입력값 오류"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<HoyaResDto>> chat(
            @RequestParam(name = "prompt") String prompt,
            Principal principal) {

        ApiResponseTemplate<HoyaResDto> data = hoyaService.getResponse(prompt, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

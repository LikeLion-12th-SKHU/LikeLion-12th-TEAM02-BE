package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryAIResDto;
import com.skhu.moodfriend.app.service.tracker.DiaryAIDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "AI 일기", description = "AI 일기를 담당하는 api 그룹")
@RequestMapping("/api/v1/diary/ai")
public class DiaryAIController {

    private final DiaryAIDisplayService diaryAIDisplayService;

    @GetMapping("/display/{createdAt}")
    @Operation(
            summary = "사용자의 특정 AI 일기 조회",
            description = "작성일을 기준으로 사용자의 특정 AI 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "특정 AI 일기 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "해당 AI 일기를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<DiaryAIResDto>> getDiarySummaryByCreatedAt(
            @PathVariable LocalDate createdAt, Principal principal) {

        ApiResponseTemplate<DiaryAIResDto> data = diaryAIDisplayService.getDiarySummaryByCreatedAt(createdAt, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

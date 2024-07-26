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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "AI 일기 조회", description = "AI 일기를 조회하는 api 그룹")
@RequestMapping("/api/v1/diary/ai/display")
public class DiaryAIDisplayController {

    private final DiaryAIDisplayService diaryAIDisplayService;

    @GetMapping
    @Operation(
            summary = "사용자의 모든 AI 일기 조회",
            description = "사용자의 모든 AI 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "AI 일기 목록 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<DiaryAIResDto>>> getAllDiaryAISummariesByUser(@RequestParam Long memberId) {

        ApiResponseTemplate<List<DiaryAIResDto>> data = diaryAIDisplayService.getAllDiarySummariesByMember(memberId);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}
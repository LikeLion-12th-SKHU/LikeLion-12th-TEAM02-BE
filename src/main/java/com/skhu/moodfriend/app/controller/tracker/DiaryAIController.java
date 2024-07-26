package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.service.tracker.DiaryAIModifyService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "AI 일기 삭제", description = "AI 일기를 삭제하는 api 그룹")
@RequestMapping("/api/v1/diary/ai")
public class DiaryAIController {

    private final DiaryAIModifyService diaryAIModifyService;

    @DeleteMapping("delete/{diaryAIId}")
    @Operation(
            summary = "사용자 AI 일기 삭제",
            description = "사용자 AI 일기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 AI 일기 삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "해당 AI 일기를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<Void>> deleteDiaryAI(
            @PathVariable Long diaryAIId,
            @RequestParam Long memberId) {

        ApiResponseTemplate<Void> data = diaryAIModifyService.deleteDiaryAI(diaryAIId, memberId);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

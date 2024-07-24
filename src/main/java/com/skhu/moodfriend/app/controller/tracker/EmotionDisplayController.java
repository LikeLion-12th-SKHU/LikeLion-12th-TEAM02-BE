package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.MonthlyEmotionReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.MonthlyEmotionResDto;
import com.skhu.moodfriend.app.service.tracker.EmotionDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "월별 감정 리스트 조회", description = "월별 감정 리스트를 조회하는 api 그룹")
@RequestMapping("/api/v1/emotion/display")
public class EmotionDisplayController {

    private final EmotionDisplayService emotionDisplayService;

    @PostMapping
    @Operation(
            summary = "사용자의 월별 감정 리스트 조회",
            description = "특정 년, 월에 해당하는 사용자의 감정 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "월별 감정 리스트 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "해당 감정 리스트를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<MonthlyEmotionResDto>> getMonthlyEmotionStats(
            @Valid @RequestBody MonthlyEmotionReqDto reqDto,
            Principal principal) {

        ApiResponseTemplate<MonthlyEmotionResDto> data = emotionDisplayService.getMonthlyEmotions(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

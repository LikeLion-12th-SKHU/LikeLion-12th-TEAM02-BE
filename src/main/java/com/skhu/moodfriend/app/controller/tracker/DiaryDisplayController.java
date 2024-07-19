package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryResDto;
import com.skhu.moodfriend.app.service.tracker.DiaryDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "일기 조회", description = "일기를 조회하는 api 그룹")
@RequestMapping("/api/v1/diary/display")
public class DiaryDisplayController {

    private final DiaryDisplayService diaryDisplayService;

    @GetMapping("/{diaryId}")
    @Operation(
            summary = "사용자의 특정 일기 조회",
            description = "사용자의 특정 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "404", description = "해당 일기를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<DiaryResDto>> getDiaryById(
            @PathVariable Long diaryId,
            Principal principal) {

        ApiResponseTemplate<DiaryResDto> data = diaryDisplayService.getDiaryById(diaryId, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping
    @Operation(
            summary = "사용자의 모든 일기 조회",
            description = "사용자의 모든 일기를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일기 목록 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<DiaryResDto>>> getAllDiariesByUser(Principal principal) {

        ApiResponseTemplate<List<DiaryResDto>> data = diaryDisplayService.getAllDiariesByMember(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

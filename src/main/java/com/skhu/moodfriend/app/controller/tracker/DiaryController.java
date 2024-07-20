package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryCreateReqDto;
import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryUpdateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryResDto;
import com.skhu.moodfriend.app.service.tracker.DiaryCreateService;
import com.skhu.moodfriend.app.service.tracker.DiaryModifyService;
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
@Tag(name = "일기 작성/수정/삭제", description = "일기를 작성/수정/삭제하는 api 그룹")
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryCreateService diaryCreateService;
    private  final DiaryModifyService diaryModifyService;

    @PostMapping("/create")
    @Operation(
            summary = "사용자 일기 작성",
            description = "사용자 일기를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "사용자 일기 작성 성공"),
                    @ApiResponse(responseCode = "403", description = "url문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<DiaryResDto>> createDiary(
            @Valid @RequestBody DiaryCreateReqDto reqDto,
            Principal principal) {

        ApiResponseTemplate<DiaryResDto> data = diaryCreateService.createDiary(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PatchMapping("/update")
    @Operation(
            summary = "사용자 일기 수정",
            description = "사용자 일기를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 일기 수정 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "404", description = "해당 일기를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<DiaryResDto>> updateDiary(
            @Valid @RequestBody DiaryUpdateReqDto reqDto,
            Principal principal) {

        ApiResponseTemplate<DiaryResDto> data = diaryModifyService.updateDiary(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @DeleteMapping("/delete/{diaryId}")
    @Operation(
            summary = "사용자 일기 삭제",
            description = "사용자 일기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 일기 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "권한 문제"),
                    @ApiResponse(responseCode = "404", description = "해당 일기를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "토큰 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<Void>> deleteDiary(
            @PathVariable Long diaryId,
            Principal principal) {

        ApiResponseTemplate<Void> data = diaryModifyService.deleteDiary(diaryId, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

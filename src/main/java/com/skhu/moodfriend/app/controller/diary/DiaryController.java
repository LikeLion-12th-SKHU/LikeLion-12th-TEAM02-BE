package com.skhu.moodfriend.app.controller.diary;

import com.skhu.moodfriend.app.dto.diary.reqDto.DiaryCreateReqDto;
import com.skhu.moodfriend.app.dto.diary.resDto.DiaryCreateResDto;
import com.skhu.moodfriend.app.service.diary.DiaryCreateService;
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
@Tag(name = "사용자 일기 작성/수정/삭제", description = "사용자 일기를 작성/수정/삭제하는 api 그룹")
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryCreateService diaryCreateService;

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
    public ResponseEntity<ApiResponseTemplate<DiaryCreateResDto>> createDiary(
            @RequestBody DiaryCreateReqDto reqDto,
            Principal principal) {

        ApiResponseTemplate<DiaryCreateResDto> data = diaryCreateService.createDiary(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

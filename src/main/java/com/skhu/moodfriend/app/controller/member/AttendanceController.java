package com.skhu.moodfriend.app.controller.member;

import com.skhu.moodfriend.app.service.member.AttendanceService;
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
@Tag(name = "사용자 출석", description = "일일 출석을 관리하는 api 그룹")
@RequestMapping("/api/v1/member/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(
            summary = "일일 출석 기록",
            description = "현재 로그인된 사용자의 일일 출석을 기록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "출석 완료 및 마일리지 적립"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 오늘 출석했습니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> recordAttendance(Principal principal) {
        ApiResponseTemplate<String> data = attendanceService.recordAttendance(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

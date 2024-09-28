package com.skhu.moodfriend.app.controller.member;

import com.skhu.moodfriend.app.dto.member.reqDto.MemberInfoUpdateReqDto;
import com.skhu.moodfriend.app.dto.member.resDto.MemberInfoResDto;
import com.skhu.moodfriend.app.dto.member.reqDto.MonthlyEmotionReqDto;
import com.skhu.moodfriend.app.dto.member.resDto.HospitalResDto;
import com.skhu.moodfriend.app.dto.member.resDto.MonthlyEmotionResDto;
import com.skhu.moodfriend.app.service.member.AttendanceService;
import com.skhu.moodfriend.app.service.member.MemberInfoService;
import com.skhu.moodfriend.app.service.member.EmotionDisplayService;
import com.skhu.moodfriend.app.service.member.HospitalDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "사용자 정보", description = "사용자 정보를 담당하는 api 그룹")
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberInfoService memberInfoService;
    private final AttendanceService attendanceService;
    private final HospitalDisplayService hospitalDisplayService;
    private final EmotionDisplayService emotionDisplayService;

    @GetMapping("/info")
    @Operation(
            summary = "사용자 정보 조회",
            description = "현재 로그인된 사용자의 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<MemberInfoResDto>> getMemberInfo(Principal principal) {
        ApiResponseTemplate<MemberInfoResDto> data = memberInfoService.getMemberInfo(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PatchMapping("info/edit")
    @Operation(
            summary = "사용자 정보 수정",
            description = "현재 로그인된 사용자의 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<MemberInfoResDto>> updateMemberInfo(
            @Valid @RequestBody MemberInfoUpdateReqDto reqDto, Principal principal) {

        ApiResponseTemplate<MemberInfoResDto> data = memberInfoService.updateMemberInfo(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/attendance")
    @Operation(
            summary = "사용자 일일 출석",
            description = "현재 로그인된 사용자의 일일 출석을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "출석 완료 및 마일리지 적립"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 오늘 출석했습니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<MemberInfoResDto>> recordAttendance(Principal principal) {
        ApiResponseTemplate<MemberInfoResDto> data = attendanceService.recordAttendance(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/hospital/display")
    @Operation(
            summary = "사용자 주변의 정신과, 심리 관련 의료기관 조회",
            description = "사용자 위치정보를 받아 카카오 지도 API를 호출합니다. 정신과, 심리 관련 의료기관을 모두 나열합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "의료기관 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<HospitalResDto>>> retrieveHospitals(
            @Parameter(name = "x", description = "경도 (longitude)") @RequestParam String x,
            @Parameter(name = "y", description = "위도 (latitude)") @RequestParam String y,
            @Parameter(name = "radius", description = "반경 (단위: 미터, 최소: 0, 최대: 20000)") @RequestParam String radius) {

        ApiResponseTemplate<List<HospitalResDto>> data = hospitalDisplayService.retrieve(x, y, radius);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/emotion/display")
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
            @Valid @RequestBody MonthlyEmotionReqDto reqDto, Principal principal) {

        ApiResponseTemplate<MonthlyEmotionResDto> data = emotionDisplayService.getMonthlyEmotions(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

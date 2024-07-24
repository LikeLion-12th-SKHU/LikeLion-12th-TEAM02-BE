package com.skhu.moodfriend.app.controller.tracker;

import com.skhu.moodfriend.app.dto.tracker.resDto.HospitalResDto;
import com.skhu.moodfriend.app.service.tracker.HospitalDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "의료기관 조회", description = "의료기관 조회를 담당하는 api 그룹")
@RequestMapping("/api/v1/hospitals")
public class HospitalDisplayController {
    private final HospitalDisplayService hospitalDisplayService;

    @GetMapping
    @Operation(
            summary = "사용자 주변의 정신과, 심리 관련 의료기관 조회",
            description = "사용자 위치정보를 받아 카카오 지도 API를 호출합니다. 정신과, 심리 관련 의료기관을 모두 나열합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "의료기관 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "URL 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<HospitalResDto>>> retrieveHospitals(
            @Parameter(name = "x", description = "경도 (longitude)") @RequestParam String x,
            @Parameter(name = "y", description = "위도 (latitude)") @RequestParam String y,
            @Parameter(name = "radius", description = "반경 (단위: 미터, 최소: 0, 최대: 20000)") @RequestParam String radius
    ) {
        ApiResponseTemplate<List<HospitalResDto>> data = hospitalDisplayService.retrieve(x, y, radius);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

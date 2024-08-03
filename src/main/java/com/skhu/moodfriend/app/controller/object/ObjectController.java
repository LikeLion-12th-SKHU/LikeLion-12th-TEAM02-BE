package com.skhu.moodfriend.app.controller.object;

import com.skhu.moodfriend.app.dto.object.reqDto.PurchaseReqDto;
import com.skhu.moodfriend.app.dto.object.resDto.PurchaseResDto;
import com.skhu.moodfriend.app.service.object.PurchaseService;
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
@Tag(name = "오브제", description = "오브제를 담당하는 api 그룹")
@RequestMapping("/api/v1/object")
public class ObjectController {

    private final PurchaseService purchaseService;

    @PostMapping("/purchase")
    @Operation(
            summary = "오브제 구매",
            description = "사용자가 오브제를 구매합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 구매 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제나 사용자를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 구매한 오브제"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<PurchaseResDto>> purchaseObject(Principal principal, @RequestBody PurchaseReqDto reqDto) {
        ApiResponseTemplate<PurchaseResDto> data = purchaseService.purchaseObject(principal, reqDto);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

package com.skhu.moodfriend.app.controller.object;

import com.skhu.moodfriend.app.dto.object.reqDto.PurchaseReqDto;
import com.skhu.moodfriend.app.dto.object.reqDto.UpdateObjectStatusReqDto;
import com.skhu.moodfriend.app.dto.object.resDto.ObjectResDto;
import com.skhu.moodfriend.app.service.object.ObjectDisplayService;
import com.skhu.moodfriend.app.service.object.ObjectStatusService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "오브제", description = "오브제를 담당하는 api 그룹")
@RequestMapping("/api/v1/object")
public class ObjectController {

    private final PurchaseService purchaseService;
    private final ObjectDisplayService objectDisplayService;
    private final ObjectStatusService objectStatusService;

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
    public ResponseEntity<ApiResponseTemplate<ObjectResDto>> purchaseObject(@RequestBody PurchaseReqDto reqDto, Principal principal) {
        ApiResponseTemplate<ObjectResDto> data = purchaseService.purchaseObject(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/owned/display")
    @Operation(
            summary = "보유 오브제 조회",
            description = "사용자가 보유한 오브제를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "보유 오브제 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<ObjectResDto>>> getMemberObjects(Principal principal) {
        ApiResponseTemplate<List<ObjectResDto>> data = objectDisplayService.getOwnedObjects(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/available/display")
    @Operation(
            summary = "구매 가능한 오브제 조회",
            description = "사용자가 구매하지 않은 오브제를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구매 가능한 오브제 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<ObjectResDto>>> getAvailableObjects(Principal principal) {
        ApiResponseTemplate<List<ObjectResDto>> data = objectDisplayService.getAvailableObjects(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PatchMapping("/status")
    @Operation(
            summary = "오브제 상태 관리",
            description = "사용자가 보유한 오브제의 상태를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 상태 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제나 사용자를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<ObjectResDto>> updateObjectStatus(@RequestBody UpdateObjectStatusReqDto reqDto, Principal principal) {
        ApiResponseTemplate<ObjectResDto> data = objectStatusService.updateObjectStatus(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

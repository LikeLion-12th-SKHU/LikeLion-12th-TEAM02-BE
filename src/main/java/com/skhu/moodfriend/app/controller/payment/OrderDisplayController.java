package com.skhu.moodfriend.app.controller.payment;

import com.skhu.moodfriend.app.dto.order.resDto.OrderResDto;
import com.skhu.moodfriend.app.service.payment.OrderDisplayService;
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
@Tag(name = "주문", description = "주문 내역을 조회하는 api 그룹")
@RequestMapping("/api/v1/order/display")
public class OrderDisplayController {

    private final OrderDisplayService orderDisplayService;

    @GetMapping
    @Operation(
            summary = "모든 주문 내역 조회",
            description = "현재 로그인된 사용자의 모든 주문 내역을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모든 주문 내역 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "주문 내역을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<OrderResDto>>> getOrderHistory(Principal principal) {
        ApiResponseTemplate<List<OrderResDto>> data = orderDisplayService.getOrderHistory(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "특정 주문 내역 조회",
            description = "현재 로그인된 사용자의 특정 주문 내역을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "특정 주문 내역 조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "주문 내역을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<OrderResDto>> getOrderDetail(@PathVariable Long orderId, Principal principal) {
        ApiResponseTemplate<OrderResDto> data = orderDisplayService.getOrderDetail(orderId, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}
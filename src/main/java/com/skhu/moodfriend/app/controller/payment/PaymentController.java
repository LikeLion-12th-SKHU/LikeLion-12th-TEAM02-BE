package com.skhu.moodfriend.app.controller.payment;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.skhu.moodfriend.app.dto.order.reqDto.OrderReqDto;
import com.skhu.moodfriend.app.dto.order.resDto.OrderResDto;
import com.skhu.moodfriend.app.service.payment.OrderDisplayService;
import com.skhu.moodfriend.app.service.payment.PaymentService;
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
@Tag(name = "주문/결제", description = "주문/결제를 담당하는 api 그룹")
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderDisplayService orderDisplayService;

    @PostMapping("/validation/{imp_uid}")
    @Operation(
            summary = "결제 정보 검증",
            description = "imp_uid를 통해 아임포트 결제 정보를 검증합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 정보 검증 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) {
        return paymentService.validateIamport(imp_uid);
    }

    @PostMapping("/order")
    @Operation(
            summary = "주문 처리",
            description = "주문 정보를 받아 결제를 요청하고, 주문 정보를 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 요청 및 주문 정보 저장 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<OrderResDto>> processOrder(@RequestBody OrderReqDto reqDto, Principal principal) {
        ApiResponseTemplate<OrderResDto> data = paymentService.saveOrder(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/cancel/{imp_uid}")
    @Operation(
            summary = "결제 취소",
            description = "imp_uid를 통해 아임포트 결제를 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 취소 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public IamportResponse<Payment> cancelPayment(@PathVariable String imp_uid) {
        return paymentService.cancelPayment(imp_uid);
    }

    @GetMapping("/display")
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

    @GetMapping("/display/{orderId}")
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

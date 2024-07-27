package com.skhu.moodfriend.app.controller.transaction;

import com.skhu.moodfriend.app.dto.transaction.reqDto.PaymentReqDto;
import com.skhu.moodfriend.app.dto.transaction.reqDto.RefundReqDto;
import com.skhu.moodfriend.app.dto.transaction.resDto.PaymentResDto;
import com.skhu.moodfriend.app.dto.transaction.resDto.RefundResDto;
import com.skhu.moodfriend.app.service.transaction.RefundService;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import com.skhu.moodfriend.app.service.transaction.PaymentService;
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
@Tag(name = "거래", description = "결제/환불하는 api 그룹")
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final PaymentService paymentService;
    private final RefundService refundService;

    @PostMapping("/payment")
    @Operation(
            summary = "주문 및 결제 처리",
            description = "주문을 저장하고 결제 처리합니다. 결제 성공 시 주문 상태를 '완료'로 변경하고, 결제 실패 시 '실패'로 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 및 결제 처리 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 or 입력값 오류"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<PaymentResDto>> saveOrderAndProcessPayment(
            @RequestBody PaymentReqDto reqDto,
            Principal principal) {

        ApiResponseTemplate<PaymentResDto> data = paymentService.saveOrderAndProcessPayment(reqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PostMapping("/refund")
    @Operation(
            summary = "환불 처리",
            description = "환불을 처리하고 상태를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "환불 처리 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 or 입력값 오류"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<RefundResDto>> processRefund(
            @RequestBody RefundReqDto reqDto,
            Principal principal) {

        RefundResDto refundResult = refundService.processRefund(reqDto, principal);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.REFUND_SUCCESS, refundResult));
    }
}


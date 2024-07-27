package com.skhu.moodfriend.app.service.transaction;

import com.skhu.moodfriend.app.dto.transaction.reqDto.PaymentReqDto;
import com.skhu.moodfriend.app.dto.transaction.resDto.PaymentResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.order.MemberOrder;
import com.skhu.moodfriend.app.entity.member.order.OrderStatus;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.OrderRepository;
import com.skhu.moodfriend.global.config.ImpConfig;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentService {

    private final ImpConfig impConfig;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ApiResponseTemplate<PaymentResDto> saveOrderAndProcessPayment(
            PaymentReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        MemberOrder order = MemberOrder.builder()
                .mileage(reqDto.mileage())
                .amount(reqDto.price())
                .platform(reqDto.platform())
                .status(OrderStatus.PROCESSING)
                .member(member)
                .build();

        MemberOrder savedOrder = orderRepository.save(order);

        PaymentResponse paymentResponse = processPaymentRequest(savedOrder, reqDto);

        if (paymentResponse.success()) {
            String impUid = paymentResponse.impUid();
            savedOrder.completePayment(impUid);
            member.updateMileage(reqDto.mileage());
            memberRepository.save(member);
        } else {
            if (refundPayment(savedOrder.getImpUid(), savedOrder.getAmount())) {
                savedOrder.failPayment(savedOrder.getImpUid());
            } else {
                throw new CustomException(ErrorCode.FAILED_REFUND_EXCEPTION, ErrorCode.FAILED_REFUND_EXCEPTION.getMessage());
            }
        }
        orderRepository.save(savedOrder);

        PaymentResDto resDto = PaymentResDto.builder()
                .orderId(savedOrder.getOrderId())
                .impUid(savedOrder.getImpUid())
                .chargedMileage(reqDto.mileage())
                .totalMileage(member.getMileage())
                .price(savedOrder.getAmount())
                .platform(savedOrder.getPlatform())
                .status(savedOrder.getStatus())
                .orderAt(savedOrder.getOrderAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.SAVE_ORDER_SUCCESS, resDto);
    }

    @Transactional(readOnly = true)
    protected PaymentResponse processPaymentRequest(MemberOrder order, PaymentReqDto reqDto) {
        String url = "https://api.iamport.kr/payments/prepare";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthToken());

        String body = String.format("{\"amount\": %d, \"merchant_uid\": \"%s\", \"name\": \"%s\"}",
                reqDto.price(), order.getOrderId(), "Payment for order " + order.getOrderId());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, PaymentResponse.class);
        PaymentResponse responseBody = response.getBody();

        if (responseBody != null && responseBody.success()) {
            return responseBody;
        } else {
            throw new CustomException(ErrorCode.FAILED_PAYMENT_EXCEPTION, ErrorCode.FAILED_PAYMENT_EXCEPTION.getMessage());
        }
    }

    @Transactional(readOnly = true)
    protected boolean refundPayment(String impUid, Integer amount) {
        String url = "https://api.iamport.kr/payments/cancel";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthToken());

        String body = String.format("{\"imp_uid\": \"%s\", \"amount\": %d}", impUid, amount);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<RefundRes> response = restTemplate.exchange(url, HttpMethod.POST, entity, RefundRes.class);
        RefundRes responseBody = response.getBody();

        return responseBody != null && responseBody.success();
    }

    @Transactional(readOnly = true)
    protected String getAuthToken() {
        String url = "https://api.iamport.kr/users/getToken";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"imp_key\": \"%s\", \"imp_secret\": \"%s\"}", impConfig.getApiKey(), impConfig.getApiSecretKey());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AuthRes> response = restTemplate.exchange(url, HttpMethod.POST, entity, AuthRes.class);
        AuthRes authResponse = response.getBody();

        if (authResponse != null && authResponse.success()) {
            return authResponse.accessToken();
        } else {
            throw new CustomException(ErrorCode.FAILED_GET_TOKEN_EXCEPTION, ErrorCode.FAILED_GET_TOKEN_EXCEPTION.getMessage());
        }
    }

    private record PaymentResponse(boolean success, String impUid) {}

    private record RefundRes(boolean success) {}

    private record AuthRes(boolean success, String accessToken) {}
}

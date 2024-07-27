package com.skhu.moodfriend.app.service.transaction;

import com.skhu.moodfriend.app.dto.transaction.reqDto.RefundReqDto;
import com.skhu.moodfriend.app.dto.transaction.resDto.RefundResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.order.MemberOrder;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.OrderRepository;
import com.skhu.moodfriend.global.config.ImpConfig;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundService {

    private final ImpConfig impConfig;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public RefundResDto processRefund(
            RefundReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        MemberOrder order = orderRepository.findByImpUid(reqDto.impUid())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER_EXCEPTION, ErrorCode.NOT_FOUND_ORDER_EXCEPTION.getMessage()));

        if (!order.getMember().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS_EXCEPTION, ErrorCode.FORBIDDEN_ACCESS_EXCEPTION.getMessage());
        }

        boolean isRefunded = refundPayment(reqDto.impUid(), order.getAmount());
        if (isRefunded) {
            order.failPayment(reqDto.impUid());
            member.updateMileage(-order.getAmount());
            memberRepository.save(member);
            orderRepository.save(order);

            return RefundResDto.builder()
                    .orderId(order.getOrderId())
                    .refundedAmount(order.getAmount())
                    .refundAt(LocalDateTime.now())
                    .build();
        } else {
            throw new CustomException(ErrorCode.FAILED_REFUND_EXCEPTION, ErrorCode.FAILED_REFUND_EXCEPTION.getMessage());
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

    private record RefundRes(boolean success) {}

    private record AuthRes(boolean success, String accessToken) {}
}

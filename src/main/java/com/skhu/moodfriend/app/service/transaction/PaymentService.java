package com.skhu.moodfriend.app.service.transaction;

import com.skhu.moodfriend.app.dto.payment.reqDto.OrderReqDto;
import com.skhu.moodfriend.app.dto.payment.resDto.OrderResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.object.MemberObject;
import com.skhu.moodfriend.app.entity.member.order.MemberOrder;
import com.skhu.moodfriend.app.entity.member.order.OrderStatus;
import com.skhu.moodfriend.app.entity.object_store.ObjectName;
import com.skhu.moodfriend.app.repository.MemberObjectRepository;
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
    private final MemberObjectRepository memberObjectRepository;

    @Transactional
    public ApiResponseTemplate<OrderResDto> saveOrderAndProcessPayment(
            OrderReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        MemberOrder order = MemberOrder.builder()
                .objectName(reqDto.objectName())
                .platform(reqDto.platform())
                .status(OrderStatus.PROCESSING)
                .member(member)
                .build();

        MemberOrder savedOrder = orderRepository.save(order);

        boolean isValidPayment = validatePayment(savedOrder.getImpUid(), savedOrder);
        if (isValidPayment) {
            savedOrder.completePayment(savedOrder.getImpUid());
            addMemberObject(reqDto.objectName(), member);
        } else {
            savedOrder.failPayment(savedOrder.getImpUid());
        }
        orderRepository.save(savedOrder);

        OrderResDto resDto = OrderResDto.builder()
                .orderId(savedOrder.getOrderId())
                .objectName(savedOrder.getObjectName())
                .platform(savedOrder.getPlatform())
                .status(savedOrder.getStatus())
                .orderAt(savedOrder.getOrderAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.SAVE_ORDER_SUCCESS, resDto);
    }

    private void addMemberObject(ObjectName objectName, Member member) {
        MemberObject memberObject = MemberObject.builder()
                .objectName(objectName)
                .status(false)
                .member(member)
                .build();
        memberObjectRepository.save(memberObject);
    }

    @Transactional(readOnly = true)
    protected boolean validatePayment(String impUid, MemberOrder order) {
        String url = "https://api.iamport.kr/payments/verify";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthToken());

        String body = String.format("{\"imp_uid\": \"%s\"}", impUid);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<IamportRes> response = restTemplate.exchange(url, HttpMethod.POST, entity, IamportRes.class);
        IamportRes responseBody = response.getBody();

        if (responseBody != null && responseBody.success()) {
            order.completePayment(impUid);
            return true;
        } else {
            order.failPayment(impUid);
            return false;
        }
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

    private record IamportRes(boolean success) {
    }

    private record AuthRes(boolean success, String accessToken) {
    }
}

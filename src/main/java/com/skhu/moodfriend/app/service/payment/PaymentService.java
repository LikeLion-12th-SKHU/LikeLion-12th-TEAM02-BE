package com.skhu.moodfriend.app.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.skhu.moodfriend.app.dto.payment.reqDto.OrderReqDto;
import com.skhu.moodfriend.app.dto.payment.resDto.OrderResDto;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.payment.Order;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.OrderRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentService {

    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public IamportResponse<Payment> validateIamport(String imp_uid) {
        try {
            return iamportClient.paymentByImpUid(imp_uid);
        } catch (Exception e) {
            return null;
        }
    }

    public IamportResponse<Payment> cancelPayment(String imp_uid) {
        try {
            CancelData cancelData = new CancelData(imp_uid, true);
            return iamportClient.cancelPaymentByImpUid(cancelData);
        } catch (Exception e) {
            return null;
        }
    }

    public ApiResponseTemplate<OrderResDto> saveOrder(OrderReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Order order = reqDto.toEntity(member);

        try {
            orderRepository.save(order);

            member.updateMileage(reqDto.mileageIncrement());
            memberRepository.save(member);
        } catch (Exception e) {
            cancelPayment(reqDto.impUid());
            throw new CustomException(ErrorCode.FAILED_ORDER_SAVE_EXCEPTION, ErrorCode.FAILED_ORDER_SAVE_EXCEPTION.getMessage());
        }

        OrderResDto resDto = OrderResDto.builder()
                .orderId(order.getOrderId())
                .productName(order.getProductName())
                .price(order.getPrice())
                .impUid(order.getImpUid())
                .merchantUid(order.getMerchantUid())
                .createdAt(order.getCreatedAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.ORDER_SAVE_SUCCESS, resDto);
    }
}

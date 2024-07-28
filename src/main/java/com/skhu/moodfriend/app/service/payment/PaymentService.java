package com.skhu.moodfriend.app.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.skhu.moodfriend.app.dto.order.reqDto.OrderReqDto;
import com.skhu.moodfriend.app.dto.order.resDto.OrderResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.order.Order;
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
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
            return payment;
        } catch (Exception e) {
            return null;
        }
    }

    public IamportResponse<Payment> cancelPayment(String imp_uid) {
        try {
            CancelData cancelData = new CancelData(imp_uid, true);
            IamportResponse<Payment> payment = iamportClient.cancelPaymentByImpUid(cancelData);
            return payment;
        } catch (Exception e) {
            return null;
        }
    }

    public ApiResponseTemplate<OrderResDto> saveOrder(OrderReqDto reqDto, Principal principal) {
        try {
            Long memberId = Long.parseLong(principal.getName());

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

            Order order = Order.builder()
                    .productName(reqDto.productName())
                    .price(reqDto.price())
                    .impUid(reqDto.impUid())
                    .merchantUid(reqDto.merchantUid())
                    .member(member)
                    .build();

            orderRepository.save(order);

            member.updateMileage(reqDto.mileageIncrement());
            memberRepository.save(member);

            OrderResDto resDto = OrderResDto.builder()
                    .orderId(order.getOrderId())
                    .productName(order.getProductName())
                    .price(order.getPrice())
                    .impUid(order.getImpUid())
                    .merchantUid(order.getMerchantUid())
                    .createdAt(order.getCreatedAt())
                    .build();

            return ApiResponseTemplate.success(SuccessCode.ORDER_SAVE_SUCCESS, resDto);
        } catch (Exception e) {
            cancelPayment(reqDto.impUid());
            throw new CustomException(ErrorCode.FAILED_ORDER_SAVE_EXCEPTION, ErrorCode.FAILED_ORDER_SAVE_EXCEPTION.getMessage());
        }
    }
}

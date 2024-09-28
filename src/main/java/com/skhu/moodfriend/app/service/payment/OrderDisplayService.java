package com.skhu.moodfriend.app.service.payment;

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
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDisplayService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public ApiResponseTemplate<List<OrderResDto>> getOrderHistory(Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        List<Order> orders = orderRepository.findByMember(member);

        List<OrderResDto> resDtos = orders.stream()
                .map(OrderResDto::of)
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_ALL_ORDERS_SUCCESS, resDtos);
    }

    public ApiResponseTemplate<OrderResDto> getOrderDetail(
            Long orderId, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER_EXCEPTION,
                        ErrorCode.NOT_FOUND_ORDER_EXCEPTION.getMessage()));

        if (!order.getMember().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS_EXCEPTION,
                    ErrorCode.FORBIDDEN_ACCESS_EXCEPTION.getMessage());
        }

        return ApiResponseTemplate.success(SuccessCode.GET_ORDER_SUCCESS, OrderResDto.of(order));
    }
}

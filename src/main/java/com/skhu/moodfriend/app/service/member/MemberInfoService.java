package com.skhu.moodfriend.app.service.member;

import com.skhu.moodfriend.app.dto.member.reqDto.MemberInfoUpdateReqDto;
import com.skhu.moodfriend.app.dto.member.resDto.MemberInfoResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ApiResponseTemplate<MemberInfoResDto> getMemberInfo(Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        MemberInfoResDto resDto = MemberInfoResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .mileage(member.getMileage())
                .loginType(member.getLoginType().getDisplayName())
                .build();

        return ApiResponseTemplate.success(SuccessCode.GET_MEMBER_INFO_SUCCESS, resDto);
    }

    @Transactional
    public ApiResponseTemplate<MemberInfoResDto> updateMemberInfo(
            MemberInfoUpdateReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        member.updateInfo(reqDto.name());

        MemberInfoResDto resDto = MemberInfoResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .mileage(member.getMileage())
                .loginType(member.getLoginType().getDisplayName())
                .build();

        return ApiResponseTemplate.success(SuccessCode.UPDATE_MEMBER_INFO_SUCCESS, resDto);
    }
}

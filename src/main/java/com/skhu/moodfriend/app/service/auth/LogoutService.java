package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.repository.MemberRefreshTokenRepository;
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
public class LogoutService {

    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<Void> logout(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        memberRefreshTokenRepository.findByMember_MemberId(memberId)
                .ifPresent(memberRefreshTokenRepository::delete);

        return ApiResponseTemplate.success(SuccessCode.LOGOUT_MEMBER_SUCCESS, null);
    }
}

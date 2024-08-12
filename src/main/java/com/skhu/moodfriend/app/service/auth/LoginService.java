package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.LoginReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.jwt.TokenProvider;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenRenewService tokenRenewService;

    @Transactional
    public ApiResponseTemplate<AuthResDto> login(LoginReqDto loginReqDto) {
        Member member = memberRepository.findByEmail(loginReqDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION,
                    ErrorCode.PASSWORD_MISMATCH_EXCEPTION.getMessage());
        }

        String accessToken = tokenProvider.createAccessToken(member);
        String refreshToken = tokenProvider.createRefreshToken(member);

        if (tokenRenewService.isBlacklisted(accessToken) || tokenRenewService.isBlacklisted(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
        }

        tokenRenewService.saveRefreshToken(refreshToken, member.getMemberId());
        AuthResDto resDto = AuthResDto.of(accessToken, refreshToken);

        return ApiResponseTemplate.success(SuccessCode.LOGIN_MEMBER_SUCCESS, resDto);
    }
}

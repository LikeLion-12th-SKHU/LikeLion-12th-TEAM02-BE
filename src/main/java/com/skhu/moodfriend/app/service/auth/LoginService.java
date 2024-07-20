package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.RenewAccessTokenDto;
import com.skhu.moodfriend.app.dto.auth.reqDto.LoginReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.LoginResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.MemberRefreshToken;
import com.skhu.moodfriend.app.repository.MemberRefreshTokenRepository;
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
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    private final TokenProvider tokenProvider;
    private final TokenRenewService tokenRenewService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponseTemplate<LoginResDto> login(LoginReqDto loginReqDto) {

        Member member = memberRepository.findByEmail(loginReqDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION,
                    ErrorCode.PASSWORD_MISMATCH_EXCEPTION.getMessage());
        }

        String renewRefreshToken = tokenProvider.createRefreshToken(member);

        MemberRefreshToken refreshTokenEntity = memberRefreshTokenRepository.findByMember_MemberId(member.getMemberId())
                .orElseGet(() -> {
                    MemberRefreshToken logoutMemberRenewRefreshToken = new MemberRefreshToken();
                    logoutMemberRenewRefreshToken.setMember(member);
                    return memberRefreshTokenRepository.save(logoutMemberRenewRefreshToken);
                });

        refreshTokenEntity.setRefreshToken(renewRefreshToken);
        refreshTokenEntity.setMember(member);

        memberRefreshTokenRepository.save(refreshTokenEntity);

        RenewAccessTokenDto renewAccessTokenDto = tokenRenewService.renewAccessTokenDtoFromRefreshToken(renewRefreshToken);
        String renewAccessToken = renewAccessTokenDto.renewAccessToken();

        LoginResDto resDto = LoginResDto.builder()
                .accessToken(renewAccessToken)
                .refreshToken(renewRefreshToken)
                .build();

        return ApiResponseTemplate.success(SuccessCode.LOGIN_MEMBER_SUCCESS, resDto);
    }
}

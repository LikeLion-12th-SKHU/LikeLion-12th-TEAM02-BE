package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.SignUpReqDto;
import com.skhu.moodfriend.app.domain.member.*;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
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
public class SignUpService {

    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ApiResponseTemplate<AuthResDto> signUp(SignUpReqDto signUpReqDto) {

        if (!signUpReqDto.password().equals(signUpReqDto.confirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION, ErrorCode.PASSWORD_MISMATCH_EXCEPTION.getMessage());
        }

        if (memberRepository.existsByEmail(signUpReqDto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER_EXCEPTION, ErrorCode.ALREADY_EXIST_MEMBER_EXCEPTION.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(signUpReqDto.password());

        Member member = signUpReqDto.toEntity(encodedPassword);
        memberRepository.save(member);

        String accessToken = tokenProvider.createAccessToken(member);
        String refreshToken = tokenProvider.createRefreshToken(member);

        MemberRefreshToken memberRefreshToken = new MemberRefreshToken();
        memberRefreshToken.setRefreshToken(refreshToken);
        memberRefreshToken.setMember(member);

        memberRefreshTokenRepository.deleteByMember(member);
        memberRefreshTokenRepository.save(memberRefreshToken);

        AuthResDto resDto = AuthResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ApiResponseTemplate.success(SuccessCode.CREATE_MEMBER_SUCCESS, resDto);
    }
}

package com.skhu.moodfriend.app.service;

import com.skhu.moodfriend.app.dto.auth.reqDto.SignUpReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
import com.skhu.moodfriend.app.entity.member.LoginType;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.MemberRefreshToken;
import com.skhu.moodfriend.app.entity.member.RoleType;
import com.skhu.moodfriend.app.repository.MemberRefreshTokenRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
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

        String encodePassword = passwordEncoder.encode(signUpReqDto.password());
        SignUpReqDto updatedSignUpReqDto = new SignUpReqDto(
                signUpReqDto.email(),
                encodePassword,
                signUpReqDto.name(),
                signUpReqDto.loginType(),
                signUpReqDto.roleType()
        );

        // 이메일 중복검사
        if (memberRepository.existsByEmail(updatedSignUpReqDto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_EXCEPTION, ErrorCode.ALREADY_EXIST_USER_EXCEPTION.getMessage());
        }

        LoginType loginType = LoginType.getLoginTypeOfString(signUpReqDto.loginType());
        RoleType roleType = RoleType.getRoleTypeOfString(signUpReqDto.roleType());

        Member member = memberRepository.save(Member.builder()
                .email(signUpReqDto.email())
                .password(signUpReqDto.password())
                .name(signUpReqDto.name())
                .loginType(loginType)
                .roleType(roleType)
                .build()
        );

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

        return ApiResponseTemplate.<AuthResDto>builder()
                .status(201)
                .success(true)
                .message("회원가입 성공")
                .data(resDto)
                .build();
    }
}

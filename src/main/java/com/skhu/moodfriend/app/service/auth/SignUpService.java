package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.SignUpReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.SignUpResDto;
import com.skhu.moodfriend.app.entity.member.LoginType;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.MemberRefreshToken;
import com.skhu.moodfriend.app.entity.member.RoleType;
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

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpService {

    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,16}$";

    @Transactional
    public ApiResponseTemplate<SignUpResDto> signUp(SignUpReqDto signUpReqDto) {

        if (!Pattern.matches(EMAIL_REGEX, signUpReqDto.email())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT_EXCEPTION, ErrorCode.INVALID_EMAIL_FORMAT_EXCEPTION.getMessage());
        }

        if (!Pattern.matches(PASSWORD_REGEX, signUpReqDto.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT_EXCEPTION, ErrorCode.INVALID_PASSWORD_FORMAT_EXCEPTION.getMessage());
        }

        String encodePassword = passwordEncoder.encode(signUpReqDto.password());

        if (memberRepository.existsByEmail(signUpReqDto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_EXCEPTION, ErrorCode.ALREADY_EXIST_USER_EXCEPTION.getMessage());
        }

        LoginType loginType = LoginType.getLoginTypeOfString(signUpReqDto.loginType());
        RoleType roleType = RoleType.getRoleTypeOfString(signUpReqDto.roleType());

        Member member = memberRepository.save(Member.builder()
                .email(signUpReqDto.email())
                .password(encodePassword)
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

        SignUpResDto resDto = SignUpResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ApiResponseTemplate.success(SuccessCode.CREATE_MEMBER_SUCCESS, resDto);
    }
}

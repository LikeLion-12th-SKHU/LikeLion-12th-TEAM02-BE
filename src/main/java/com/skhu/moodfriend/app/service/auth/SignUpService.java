package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.EmailCheckReqDto;
import com.skhu.moodfriend.app.dto.auth.reqDto.SignUpReqDto;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.dto.auth.resDto.EmailCheckResDto;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public ApiResponseTemplate<AuthResDto> signUp(SignUpReqDto signUpReqDto) {

        if (memberRepository.existsByEmail(signUpReqDto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER_EXCEPTION,
                    ErrorCode.ALREADY_EXIST_MEMBER_EXCEPTION.getMessage());
        }

        if (!emailService.isEmailVerified(signUpReqDto.email())) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED_EXCEPTION,
                    ErrorCode.EMAIL_NOT_VERIFIED_EXCEPTION.getMessage());
        }

        if (!signUpReqDto.password().equals(signUpReqDto.confirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION,
                    ErrorCode.PASSWORD_MISMATCH_EXCEPTION.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(signUpReqDto.password());
        Member member = signUpReqDto.toEntity(encodedPassword);
        memberRepository.save(member);

        emailService.removeVerifiedEmail(signUpReqDto.email());

        return ApiResponseTemplate.success(SuccessCode.CREATE_MEMBER_SUCCESS, null);
    }

    public ApiResponseTemplate<EmailCheckResDto> checkEmailDuplication(EmailCheckReqDto reqDto) {
        boolean isDuplicated = memberRepository.existsByEmail(reqDto.email());
        return ApiResponseTemplate.success(SuccessCode.EMAIL_CHECK_SUCCESS, EmailCheckResDto.from(isDuplicated));
    }
}

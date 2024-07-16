package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.RefreshTokenParsingDto;
import com.skhu.moodfriend.app.dto.auth.RenewAccessTokenDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRenewService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public RenewAccessTokenDto renewAccessTokenDtoFromRefreshToken(String refreshToken) {

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION,
                    ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
        }

        RefreshTokenParsingDto memberIdDto = getStudentInfoFromRefreshToken(refreshToken);

        Member member = memberRepository.findById(memberIdDto.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ID_EXCEPTION,
                        ErrorCode.NOT_FOUND_ID_EXCEPTION.getMessage()));

        String renewAccessToken;

        try {
            renewAccessToken = tokenProvider.createAccessToken(member);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.TOKEN_CREATION_FAILED_EXCEPTION,
                    ErrorCode.TOKEN_CREATION_FAILED_EXCEPTION.getMessage());
        }

        return RenewAccessTokenDto.builder()
                .renewAccessToken(renewAccessToken)
                .build();
    }


    private RefreshTokenParsingDto getStudentInfoFromRefreshToken(String refreshToken) {
        Claims claims = tokenProvider.getClaimsFromToken(refreshToken);

        Long memberId = Long.parseLong(claims.getSubject());

        return RefreshTokenParsingDto.builder()
                .memberId(memberId)
                .build();
    }
}
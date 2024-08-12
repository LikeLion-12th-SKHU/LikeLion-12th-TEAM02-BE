package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.jwt.TokenProvider;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRenewService {

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public ApiResponseTemplate<AuthResDto> renewAccessToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;

        if (isBlacklisted(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
        }

        String memberIdStr = redisTemplate.opsForValue().get(key);

        if (memberIdStr == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
        }

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
        }

        Long memberId = Long.parseLong(memberIdStr);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ID_EXCEPTION,
                        ErrorCode.NOT_FOUND_ID_EXCEPTION.getMessage()));

        String renewAccessToken = tokenProvider.createAccessToken(member);
        AuthResDto resDto = AuthResDto.of(renewAccessToken, refreshToken);

        return ApiResponseTemplate.success(SuccessCode.GET_TOKEN_SUCCESS, resDto);
    }

    public void saveRefreshToken(String refreshToken, Long memberId) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        redisTemplate.opsForValue().set(key, memberId.toString());
    }

    public void deleteRefreshToken(String refreshToken) {
        String redisKey = REFRESH_TOKEN_PREFIX + refreshToken;
        redisTemplate.delete(redisKey);
    }

    public void addToBlacklist(String token) {
        if (token != null) {
            long expiration = getRemainingExpirationTime(token);
            if (expiration > 0) {
                redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMillis(expiration));
            }
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    private long getRemainingExpirationTime(String token) {
        Claims claims = tokenProvider.getClaimsFromToken(token);
        Date expirationDate = claims.getExpiration();
        long now = new Date().getTime();
        return expirationDate.getTime() - now;
    }
}

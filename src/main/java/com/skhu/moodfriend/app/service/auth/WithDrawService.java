package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.domain.member.LoginType;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WithDrawService {

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    private final MemberRepository memberRepository;
    private final TokenRenewService tokenRenewService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${oauth.google.revoke-url}")
    private String googleRevokeUrl;

    @Value("${oauth.kakao.admin-key}")
    private String kakaoAdminKey;

    @Transactional
    public ApiResponseTemplate<Void> withdraw(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));
        LoginType loginType = member.getLoginType();

        String refreshTokenKey = REFRESH_TOKEN_PREFIX + memberId;
        String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);

        if (refreshToken != null) {
            tokenRenewService.deleteRefreshToken(refreshToken);
            tokenRenewService.addToBlacklist(refreshToken);
        }

        if (principal instanceof Authentication) {
            String accessToken = extractAccessToken((Authentication) principal);

            if (accessToken != null) {
                tokenRenewService.addToBlacklist(accessToken);

                switch (loginType) {
                    case GOOGLE_LOGIN:
                        revokeGoogleAccess(accessToken);
                        break;
                    case KAKAO_LOGIN:
                        unlinkKakaoAccount(memberId);
                        break;
                    case NATIVE_LOGIN:
                        break;
                }
            }
        }

        memberRepository.delete(member);

        return ApiResponseTemplate.success(SuccessCode.WITHDRAW_MEMBER_SUCCESS, null);
    }

    private String extractAccessToken(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt) {
            return ((Jwt) credentials).getTokenValue();
        }
        return null;
    }

    private void revokeGoogleAccess(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = googleRevokeUrl + "?token=" + accessToken;
            restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_WITHDRAW_EXCEPTION, ErrorCode.FAILED_WITHDRAW_EXCEPTION.getMessage());
        }
    }

    private void unlinkKakaoAccount(Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String kakaoUnlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            Map<String, Object> params = new HashMap<>();
            params.put("target_id_type", "user_id");
            params.put("target_id", memberId);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
            restTemplate.postForEntity(kakaoUnlinkUrl, request, String.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_WITHDRAW_EXCEPTION, ErrorCode.FAILED_WITHDRAW_EXCEPTION.getMessage());
        }
    }
}

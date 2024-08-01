package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.domain.member.LoginType;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.repository.MemberRefreshTokenRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutService {

    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${oauth.google.logout-url}")
    private String googleLogoutUrl;

    @Value("${oauth.kakao.admin-key}")
    private String kakaoAdminKey;

    @Transactional
    public ApiResponseTemplate<Void> logout(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));
        LoginType loginType = member.getLoginType();

        memberRefreshTokenRepository.findByMember_MemberId(memberId)
                .ifPresent(memberRefreshTokenRepository::delete);

        if (principal instanceof Authentication) {
            String accessToken = extractAccessToken((Authentication) principal);

            if (accessToken != null) {
                switch (loginType) {
                    case GOOGLE_LOGIN:
                        logoutFromGoogle(accessToken);
                        break;
                    case KAKAO_LOGIN:
                        logoutFromKakao(memberId);
                        break;
                    case NATIVE_LOGIN:
                        break;
                }
            }
        }

        return ApiResponseTemplate.success(SuccessCode.LOGOUT_MEMBER_SUCCESS, null);
    }

    private String extractAccessToken(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt) {
            return ((Jwt) credentials).getTokenValue();
        }
        return null;
    }

    private void logoutFromGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(googleLogoutUrl + accessToken, null, String.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_LOGOUT_EXCEPTION, ErrorCode.FAILED_LOGOUT_EXCEPTION.getMessage());
        }
    }

    private void logoutFromKakao(Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String kakaoLogoutUrl = "https://kapi.kakao.com/v1/user/logout";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            Map<String, Object> params = new HashMap<>();
            params.put("target_id_type", "user_id");
            params.put("target_id", memberId);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
            restTemplate.postForEntity(kakaoLogoutUrl, request, String.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_LOGOUT_EXCEPTION, ErrorCode.FAILED_LOGOUT_EXCEPTION.getMessage());
        }
    }
}

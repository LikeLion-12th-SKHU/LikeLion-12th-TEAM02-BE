package com.skhu.moodfriend.app.service.auth;

import com.google.gson.Gson;
import com.skhu.moodfriend.app.dto.auth.resDto.OAuthResDto;
import com.skhu.moodfriend.app.entity.member.LoginType;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.RoleType;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.dto.MemberInfo;
import com.skhu.moodfriend.global.dto.Token;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.jwt.TokenProvider;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    @Value("${oauth.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${oauth.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/api/v1/auth/callback/google";
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public ApiResponseTemplate<String> getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = Map.of(
                "code", code,
                "scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
                "client_id", GOOGLE_CLIENT_ID,
                "client_secret", GOOGLE_CLIENT_SECRET,
                "redirect_uri", GOOGLE_REDIRECT_URI,
                "grant_type", "authorization_code"
        );

        ResponseEntity<String> resEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);
        if (resEntity.getStatusCode().is2xxSuccessful()) {
            String json = resEntity.getBody();
            Gson gson = new Gson();
            String accessToken = gson.fromJson(json, Token.class).accessToken();
            return ApiResponseTemplate.success(SuccessCode.GET_TOKEN_SUCCESS, accessToken);
        }

        throw new CustomException(ErrorCode.FAILED_GET_TOKEN_EXCEPTION, ErrorCode.FAILED_GET_TOKEN_EXCEPTION.getMessage());
    }

    public ApiResponseTemplate<OAuthResDto> signUpOrLogin(String googleAccessToken) {
        MemberInfo memberInfo = getMemberInfo(googleAccessToken);

        Member member = memberRepository.findByEmail(memberInfo.email())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(memberInfo.email())
                        .name(memberInfo.name())
                        .password(null)
                        .mileage(0)
                        .loginType(LoginType.GOOGLE_LOGIN)
                        .roleType(RoleType.ROLE_USER)
                        .build())
                );

        OAuthResDto resDto = OAuthResDto.builder()
                .accessToken(tokenProvider.createAccessToken(member))
                .build();

        return ApiResponseTemplate.success(SuccessCode.LOGIN_MEMBER_SUCCESS, resDto);
    }

    public MemberInfo getMemberInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();

            return gson.fromJson(json, MemberInfo.class);
        }

        throw new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage());
    }
}

package com.skhu.moodfriend.app.service.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skhu.moodfriend.app.dto.auth.resDto.AuthResDto;
import com.skhu.moodfriend.app.domain.member.LoginType;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.RoleType;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${oauth.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public ApiResponseTemplate<String> getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_TOKEN_URL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                .queryParam("code", code)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (resEntity.getStatusCode().is2xxSuccessful()) {
            String json = resEntity.getBody();
            Gson gson = new Gson();
            String accessToken = gson.fromJson(json, Token.class).accessToken();
            return ApiResponseTemplate.success(SuccessCode.GET_TOKEN_SUCCESS, accessToken);
        }

        throw new CustomException(ErrorCode.FAILED_GET_TOKEN_EXCEPTION, ErrorCode.FAILED_GET_TOKEN_EXCEPTION.getMessage());
    }


    @Transactional
    public ApiResponseTemplate<AuthResDto> signUpOrLogin(String kakaoAccessToken) {
        MemberInfo memberInfo = getMemberInfo(kakaoAccessToken);

        Member member = memberRepository.findByEmail(memberInfo.email())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(memberInfo.email())
                        .name(memberInfo.name())
                        .password(null)
                        .mileage(0)
                        .loginType(LoginType.KAKAO_LOGIN)
                        .roleType(RoleType.ROLE_USER)
                        .build())
                );

        AuthResDto resDto = AuthResDto.builder()
                .accessToken(tokenProvider.createAccessToken(member))
                .refreshToken(tokenProvider.createRefreshToken(member))
                .build();

        return ApiResponseTemplate.success(SuccessCode.LOGIN_MEMBER_SUCCESS, resDto);
    }


    public MemberInfo getMemberInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v2/user/me?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

            JsonObject kakaoAccount = jsonObject.getAsJsonObject("kakao_account");
            String email = kakaoAccount.get("email").getAsString();
            String name = kakaoAccount.get("profile").getAsJsonObject().get("nickname").getAsString();

            return new MemberInfo(email, name);
        }

        throw new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage());
    }

}

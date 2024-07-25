package com.skhu.moodfriend.app.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skhu.moodfriend.app.dto.chat.reqDto.TranslationReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.TranslationResDto;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TranslationService {

    @Value("${deepl.api.url}")
    private String deeplApiUrl;

    @Value("${deepl.api.key}")
    private String authKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String translate(String text, String targetLang) {
        TranslationReqDto reqDto = new TranslationReqDto(Collections.singletonList(text), targetLang);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(reqDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_TRANSLATION_EXCEPTION, "Failed to serialize request body: " + e.getMessage());
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TranslationResDto> response;
        try {
            response = restTemplate.exchange(deeplApiUrl + "?auth_key=" + authKey, HttpMethod.POST, entity, TranslationResDto.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_TRANSLATION_EXCEPTION, "Failed to translate: " + e.getMessage());
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.FAILED_TRANSLATION_EXCEPTION, "Failed to translate. Status code: " + response.getStatusCode());
        }

        TranslationResDto resDto = response.getBody();
        if (resDto == null || resDto.translations().isEmpty()) {
            throw new CustomException(ErrorCode.FAILED_TRANSLATION_EXCEPTION, "No translation results");
        }

        return resDto.translations().get(0).text();
    }
}

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
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
            throw new CustomException(ErrorCode.JSON_SERIALIZATION_ERROR, ErrorCode.JSON_SERIALIZATION_ERROR.getMessage());
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TranslationResDto> response = restTemplate.exchange(deeplApiUrl + "?auth_key=" + authKey, HttpMethod.POST, entity, TranslationResDto.class);

        TranslationResDto resDto = response.getBody();
        if (resDto == null || resDto.translations().isEmpty()) {
            throw new CustomException(ErrorCode.FAILED_TRANSLATION_EXCEPTION, ErrorCode.FAILED_TRANSLATION_EXCEPTION.getMessage());
        }

        return resDto.translations().get(0).text();
    }
}

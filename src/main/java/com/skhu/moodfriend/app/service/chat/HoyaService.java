package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.reqDto.HoyaReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.HoyaResDto;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HoyaService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    public ApiResponseTemplate<HoyaResDto> getResponse(
            String prompt, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        HoyaReqDto reqDto = new HoyaReqDto(model, prompt);
        HoyaResDto resDto = restTemplate.postForObject(apiURL, reqDto, HoyaResDto.class);

        return ApiResponseTemplate.success(SuccessCode.GET_HOYA_SUCCESS, resDto);
    }
}

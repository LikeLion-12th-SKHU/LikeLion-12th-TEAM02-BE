package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HoyaService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ConversationService conversationService;
    private final TranslationService translationService;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    public ApiResponseTemplate<HoyaResDto> getResponse(String prompt, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        String translatedPromptToEn = translationService.translate(prompt, "EN");

        List<Message> messages = new ArrayList<>(conversationService.getConversation(memberId));
        messages.add(new Message("user", translatedPromptToEn));

        HoyaReqDto reqDto = new HoyaReqDto(model, messages);
        HoyaResDto resDto = restTemplate.postForObject(apiURL, reqDto, HoyaResDto.class);

        if (resDto == null || resDto.choices().isEmpty()) {
            throw new CustomException(ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION, ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION.getMessage());
        }

        HoyaResDto.Choice choice = resDto.choices().get(0);
        String responseInEN = choice.message().content();

        Message responseMessage = new Message("assistant", responseInEN);
        conversationService.addMessage(memberId, responseMessage);

        String translatedResponseToKO = translationService.translate(responseInEN, "KO");

        Message translatedMessage = new Message("assistant", translatedResponseToKO);

        HoyaResDto responseDto = new HoyaResDto(
                Collections.singletonList(new HoyaResDto.Choice(0, translatedMessage)),
                resDto.usage()
        );

        return ApiResponseTemplate.success(SuccessCode.GET_HOYA_SUCCESS, responseDto);
    }
}

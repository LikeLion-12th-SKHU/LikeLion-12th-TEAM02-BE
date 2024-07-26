package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
import com.skhu.moodfriend.app.dto.chat.reqDto.HoyaReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.HoyaResDto;
import com.skhu.moodfriend.app.entity.diary_ai.DiaryAI;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.DiaryAIRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySummaryService {

    private final ConversationService conversationService;
    private final DiaryAIRepository diaryAIRepository;
    private final MemberRepository memberRepository;
    private final TranslationService translationService;
    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    @Transactional
    @Scheduled(cron = "0 55 10 * * *")
    public void summarizeConversations() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            Long memberId = member.getMemberId();
            List<Message> messages = conversationService.getConversation(memberId);

            String prompt = "다음은 사용자의 대화 내용입니다. 대화의 주요 내용을 분석하여 전체적으로 요약해 주세요. 요약된 내용은 1024자를 초과하지 않아야 합니다. 대화의 핵심 포인트와 중요한 정보를 포함시켜야 합니다.";
            String translatedPromptToEn = translationService.translate(prompt, "EN");

            messages.add(new Message("user", translatedPromptToEn));

            HoyaReqDto reqDto = new HoyaReqDto(model, messages);
            HoyaResDto resDto = restTemplate.postForObject(apiURL, reqDto, HoyaResDto.class);

            if (resDto == null || resDto.choices().isEmpty()) {
                throw new CustomException(ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION, ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION.getMessage());
            }

            HoyaResDto.Choice choice = resDto.choices().get(0);
            String summaryInEN = choice.message().content();

            String summaryInKO = translationService.translate(summaryInEN, "KO");

            DiaryAI diaryAI = DiaryAI.builder()
                    .summary(summaryInKO)
                    .member(member)
                    .build();

            diaryAIRepository.save(diaryAI);
        }
    }
}

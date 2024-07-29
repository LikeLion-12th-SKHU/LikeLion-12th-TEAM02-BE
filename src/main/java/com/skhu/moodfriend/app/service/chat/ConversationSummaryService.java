package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
import com.skhu.moodfriend.app.dto.chat.reqDto.HoyaReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.HoyaResDto;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import com.skhu.moodfriend.app.domain.member.Member;
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
public class ConversationSummaryService {

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
    @Scheduled(cron = "0 0 2 * * *")
    public void summarizeConversations() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            Long memberId = member.getMemberId();
            List<Message> messages = conversationService.getConversation(memberId);

            String prompt = "Here is the user's conversation history. Please summarize the main points of the conversation in a concise manner. The summary should not exceed 1024 characters and must include the key points and important information.";

            messages.add(new Message("user", prompt));

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

package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.domain.tracker.conversation.ContentType;
import com.skhu.moodfriend.app.domain.tracker.conversation.Conversation;
import com.skhu.moodfriend.app.dto.chat.Message;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.dto.chat.reqDto.HoyaReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.HoyaResDto;
import com.skhu.moodfriend.app.repository.DiaryAIRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
            if (hasUserInput(memberId)) {
                List<Conversation> conversations = conversationService.getConversation(memberId);

                StringBuilder prompt = new StringBuilder("Here is the user's conversation history. Please summarize the main points of the conversation in a concise and casual manner, as if the user is writing a diary entry. The summary should not exceed 1024 characters and must include the key points and important information: ");
                conversations.forEach(conversation -> prompt.append(conversation.getContent()).append(" "));

                HoyaReqDto reqDto = new HoyaReqDto(model, List.of(new Message("system", prompt.toString())));
                HoyaResDto resDto = restTemplate.postForObject(apiURL, reqDto, HoyaResDto.class);

                if (resDto == null || resDto.choices().isEmpty()) {
                    throw new CustomException(ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION, ErrorCode.FAILED_GET_GPT_RESPONSE_EXCEPTION.getMessage());
                }

                String summaryInEN = resDto.choices().get(0).message().content();
                String summaryInKO = translationService.translate(summaryInEN, "KO");

                DiaryAI diaryAI = DiaryAI.builder()
                        .summary(summaryInKO)
                        .member(member)
                        .build();
                diaryAIRepository.save(diaryAI);

                conversationService.clearConversation(memberId);
                conversationService.addConversation(memberId, summaryInKO, ContentType.ASSISTANT);
            }
        }
    }

    private boolean hasUserInput(Long memberId) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);

        return conversationService.getConversation(memberId).stream()
                .anyMatch(conv -> conv.getCreatedAt().isAfter(since)
                        && conv.getContentType() == ContentType.USER);
    }
}

package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.repository.DiaryAIRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ConversationCleanupService {

    private final ConversationService conversationService;
    private final DiaryAIRepository diaryAIRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 30 2 * * *")
    public void clearAndInsertSummary() {

        List<Member> members = memberRepository.findAll();

        Map<Long, String> summaries = diaryAIRepository.findAllByCreatedAt(LocalDate.now()).stream()
                .collect(Collectors.toMap(
                        diaryAI -> diaryAI.getMember().getMemberId(),
                        DiaryAI::getSummary
                ));

        for (Member member : members) {
            Long memberId = member.getMemberId();

            conversationService.clearConversation(memberId);

            String summary = summaries.get(memberId);
            if (summary != null) {
                Message summaryMessage = new Message("system", summary);
                conversationService.addMessage(memberId, summaryMessage);
            }
        }
    }
}

package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.tracker.conversation.ContentType;
import com.skhu.moodfriend.app.domain.tracker.conversation.Conversation;
import com.skhu.moodfriend.app.repository.ConversationRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MemberRepository memberRepository;

    public List<Conversation> getConversation(Long memberId) {
        return conversationRepository.findByMember_MemberIdOrderByCreatedAtAsc(memberId);
    }

    @Transactional
    public void addConversation(Long memberId, String content, ContentType contentType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Conversation conversation = Conversation.builder()
                .member(member)
                .content(content)
                .contentType(contentType)
                .build();
        conversationRepository.save(conversation);
    }

    @Transactional
    public void clearConversation(Long memberId) {
        conversationRepository.deleteByMember_MemberId(memberId);
    }
}

package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.tracker.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByMember_MemberIdOrderByCreatedAtAsc(Long memberId);

    void deleteByMember_MemberId(Long memberId);
}

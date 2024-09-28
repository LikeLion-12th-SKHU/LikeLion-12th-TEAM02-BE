package com.skhu.moodfriend.app.service.friend;

import com.skhu.moodfriend.app.dto.friend.reqDto.FriendReqDto;
import com.skhu.moodfriend.app.domain.friend.Friend;
import com.skhu.moodfriend.app.domain.friend.Status;
import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.repository.FriendRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public ApiResponseTemplate<Void> sendFriendRequest(
            FriendReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member requester = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Member receiver = memberRepository.findByEmail(reqDto.receiverEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        if (requester.getEmail().equals(reqDto.receiverEmail())) {
            throw new CustomException(ErrorCode.INVALID_FRIEND_REQUEST_EXCEPTION,
                    ErrorCode.INVALID_FRIEND_REQUEST_EXCEPTION.getMessage());
        }

        if (friendRepository.existsByRequesterAndMemberAndStatus(requester, receiver, Status.WAITING) ||
                friendRepository.existsByRequesterAndMemberAndStatus(receiver, requester, Status.WAITING) ||
                friendRepository.existsByRequesterAndMemberAndStatus(requester, receiver, Status.ACCEPTED)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND_REQUEST_EXCEPTION,
                    ErrorCode.ALREADY_FRIEND_REQUEST_EXCEPTION.getMessage());
        }

        Friend friendRequest = Friend.createFriendRequest(reqDto.receiverEmail(), receiver, requester);
        friendRepository.save(friendRequest);

        return ApiResponseTemplate.success(SuccessCode.REQUEST_FRIEND_SUCCESS, null);
    }

    public ApiResponseTemplate<Void> acceptFriendRequest(
            String friendEmail, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member currentMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Member friendMember = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        Friend friendRequest = friendRepository.findByRequesterAndMemberAndStatus(friendMember, currentMember, Status.WAITING)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST_EXCEPTION,
                        ErrorCode.NOT_FOUND_FRIEND_REQUEST_EXCEPTION.getMessage()));

        friendRequest.acceptRequest();
        friendRepository.save(friendRequest);

        Friend newFriendForCurrentMember = Friend.createFriendRequest(friendMember.getEmail(), friendMember, currentMember);
        newFriendForCurrentMember.acceptRequest();
        friendRepository.save(newFriendForCurrentMember);

        return ApiResponseTemplate.success(SuccessCode.ACCEPT_FRIEND_REQUEST, null);
    }

    public ApiResponseTemplate<Void> rejectFriendRequest(
            String friendEmail, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member currentMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Member friendMember = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        Friend friendRequest = friendRepository.findByRequesterAndMemberAndStatus(friendMember, currentMember, Status.WAITING)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST_EXCEPTION,
                        ErrorCode.NOT_FOUND_FRIEND_REQUEST_EXCEPTION.getMessage()));

        friendRequest.rejectedRequest();
        friendRepository.save(friendRequest);

        return ApiResponseTemplate.success(SuccessCode.REJECT_FRIEND_REQUEST, null);
    }

    public ApiResponseTemplate<Void> deleteFriend(
            String friendEmail, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member currentMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION,
                        ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Member friendMember = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION,
                        ErrorCode.NOT_FOUND_EMAIL_EXCEPTION.getMessage()));

        Optional<Friend> friendInMyList = friendRepository.findByRequesterAndMemberAndStatus(currentMember, friendMember, Status.ACCEPTED);
        Optional<Friend> friendInFriendList = friendRepository.findByRequesterAndMemberAndStatus(friendMember, currentMember, Status.ACCEPTED);

        if (friendInMyList.isPresent()) {
            friendRepository.delete(friendInMyList.get());
            friendInFriendList.ifPresent(friendRepository::delete);
            return ApiResponseTemplate.success(SuccessCode.DELETE_FRIEND_SUCCESS, null);
        } else {
            return ApiResponseTemplate.error(ErrorCode.NOT_FOUND_FRIEND_REQUEST_EXCEPTION);
        }
    }
}

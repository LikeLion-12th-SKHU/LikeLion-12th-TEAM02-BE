package com.skhu.moodfriend.app.service.friend;

import com.skhu.moodfriend.app.dto.friend.reqDto.FriendRequestDto;
import com.skhu.moodfriend.app.dto.friend.resDto.FriendListDto;
import com.skhu.moodfriend.app.dto.member.resDto.MemberInfoResDto;
import com.skhu.moodfriend.app.entity.friend.Friend;
import com.skhu.moodfriend.app.entity.friend.Status;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.tracker.diary.Diary;
import com.skhu.moodfriend.app.entity.tracker.diary.EmotionType;
import com.skhu.moodfriend.app.repository.FriendRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.dto.PagedResponse;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public ApiResponseTemplate<MemberInfoResDto> addNewFriend(String friendEmail, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member requester = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, "해당 사용자를 찾을 수 없습니다"));


        Member receiver = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL_EXCEPTION, "해당 이메일의 사용자를 찾을 수 없습니다."));


        boolean requestExists = friendRepository.existsByRequesterAndMemberAndStatus(requester, receiver, Status.WAITING);
        boolean reverseRequestExists = friendRepository.existsByRequesterAndMemberAndStatus(receiver, requester, Status.WAITING);

        boolean duplicatedFriend = friendRepository.existsByRequesterAndMemberAndStatus(requester, receiver, Status.ACCEPTED);
        if (requestExists || reverseRequestExists) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND_REQUEST_EXCEPTION, "이미 친구 추가 요청 중입니다.");
        }
        if(duplicatedFriend){
            throw new CustomException(ErrorCode.ALREADY_FRIEND_REQUEST_EXCEPTION, "이미 친구 입니다");
        }

        Friend friendRequest = Friend.builder()
                .receiverEmail(friendEmail)
                .status(Status.WAITING)
                .member(receiver)
                .requester(requester)
                .build();

        friendRepository.save(friendRequest);

        return ApiResponseTemplate.success(SuccessCode.ADD_FRIEND_SUCCESS);
    }


    public ApiResponseTemplate<PagedResponse<FriendRequestDto>> fetchFriendRequest(int page, int size, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, "Member not found"));

        Page<Friend> friendRequests = friendRepository.findByMemberAndStatus(PageRequest.of(page, size), member.getMemberId(), Status.WAITING);


        List<FriendRequestDto> friendRequestList = friendRequests.stream()
                .map(friend -> FriendRequestDto.builder()
                        .senderMemberId(friend.getRequester().getMemberId().toString())
                        .senderEmail(friend.getRequester().getEmail())
                        .senderName(friend.getRequester().getName())
                        .friendId(friend.getFriendId())
                        .status(friend.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_FRIENDS_REQUEST_SUCCESS, new PagedResponse<>(
                friendRequestList,
                friendRequests.getNumber(),
                friendRequests.getSize(),
                friendRequests.getTotalElements(),
                friendRequests.getTotalPages()
        ));
    }


    @Transactional
    public ApiResponseTemplate<?> updateFriendRequest(Long friendMemberId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member currentUser = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, "Current user not found"));


        Member friendUser = memberRepository.findById(friendMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REQUESTED_MEMBER_EXCEPTION, "Friend user not found"));


        System.out.println(currentUser.getMemberId());
        System.out.println(friendUser.getMemberId());

        Friend friendRequest = friendRepository.findByRequesterAndMemberAndStatus(currentUser.getMemberId(),friendUser.getMemberId(),  Status.WAITING)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND, "친구 요청이 없거나 이미 존재합니다"));


        friendRequest.setStatus(Status.ACCEPTED);
        friendRepository.save(friendRequest);


        Friend newFriendForCurrentUser = Friend.builder()
                .requester(currentUser)
                .member(friendUser)
                .receiverEmail(friendUser.getEmail())
                .status(Status.ACCEPTED)
                .build();

        friendRepository.save(newFriendForCurrentUser);

        return ApiResponseTemplate.success(SuccessCode.UPDATE_FRIEND_REQUEST);

    }

    @Transactional(readOnly = true)
    public ApiResponseTemplate<PagedResponse<FriendListDto>> fetchFriendList(Integer page, Integer size, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("member.name")));


        Member currentUser = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, "Current user not found"));


        Page<Friend> friendsPage = friendRepository.findFriendsByMember(currentUser.getMemberId(), pageable);


        List<FriendListDto> friendDtos = friendsPage.getContent().stream()
                .map(friend -> {

                    EmotionType emotionType = EmotionType.JOY;

                    if (friend.getStatus() != Status.WAITING) {

                        Diary latestDiary = friend.getMember().getDiaries().stream()
                                .max(Comparator.comparing(Diary::getCreatedAt))
                                .orElse(null);

                        if (latestDiary != null) {
                            emotionType = latestDiary.getEmotionType();
                        }
                    }


                    return new FriendListDto(friend.getMember().getMemberId(), friend.getMember().getName(), friend.getMember().getEmail(), emotionType);
                })
                .collect(Collectors.toList());


        return ApiResponseTemplate.success(SuccessCode.GET_FRIENDS_SUCCESS, new PagedResponse<>(friendDtos, friendsPage.getNumber(), friendsPage.getSize(), friendsPage.getTotalElements(), friendsPage.getTotalPages()));
    }

    public ApiResponseTemplate<?> deleteFriend(Long friendMemberId, Principal principal) {
        Long currentMemberId = Long.parseLong(principal.getName());


        Optional<Friend> friendInMyList = friendRepository.findByMemberIdAndFriendId(currentMemberId, friendMemberId);
        Optional<Friend> friendInFriendList = friendRepository.findByMemberIdAndFriendId(friendMemberId, currentMemberId);

        if (friendInMyList.isPresent()) {

            friendRepository.delete(friendInMyList.get());

            friendInFriendList.ifPresent(friendRepository::delete);
            return ApiResponseTemplate.success(SuccessCode.DELETE_FRIEND_SUCCESS);
        } else {
            return ApiResponseTemplate.error(ErrorCode.FRIEND_REQUEST_NOT_FOUND, "친구가 존재하지 않습니다");
        }

    }
}

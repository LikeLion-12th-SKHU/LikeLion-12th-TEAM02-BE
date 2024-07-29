package com.skhu.moodfriend.app.controller.friend;


import com.skhu.moodfriend.app.dto.friend.reqDto.FriendRequestDto;
import com.skhu.moodfriend.app.dto.friend.resDto.FriendListDto;
import com.skhu.moodfriend.app.service.friend.FriendService;
import com.skhu.moodfriend.global.dto.PagedResponse;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "친구 관리", description = "친구 관리 api 그룹")
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("add")
    @Operation(
            summary = "친구 추가",
            description = "새로운 친구를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "친구 추가 완료 "),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구 입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )

    public ResponseEntity<ApiResponseTemplate<?>> addNewFriend(@RequestParam(name = "friendEmail") String friendEmail, Principal principal) {
        ApiResponseTemplate<?> data = friendService.addNewFriend(friendEmail, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("list/add")
    @Operation(
            summary = "친구 추가 요청 리스트 ",
            description = "요청된 리스트를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 추가 요청 리스트 조회 "),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구 입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<PagedResponse<FriendRequestDto>>> listFriendRequest(@RequestParam(defaultValue = "1") Integer page,
                                                                                                  @RequestParam(defaultValue = "10") Integer size,
                                                                                                  Principal principal) {
        ApiResponseTemplate<PagedResponse<FriendRequestDto>> data = friendService.fetchFriendRequest(page-1, size, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }


    @PutMapping("friend-request/accept")
    @Operation(
            summary = "친구 요청 수락 ",
            description = "요청된 친구 요청을 수락 합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 추가 요청 수락"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구 입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<?>> acceptFriendRequest(@RequestParam Long friendMemberId, Principal principal) {
        ApiResponseTemplate<?> data = friendService.updateFriendRequest(friendMemberId, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }


    @GetMapping
    @Operation(
            summary = "친구 리스트 조회",
            description = "친구 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 리스트 조회 완료 "),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구 입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<PagedResponse<FriendListDto>>> friendLists(@RequestParam(defaultValue = "1") Integer page,
                                                                                         @RequestParam(defaultValue = "10") Integer size,
                                                                                         Principal principal) {
        ApiResponseTemplate<PagedResponse<FriendListDto>> data = friendService.fetchFriendList(page-1,size,principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @DeleteMapping("delete")
    @Operation(
            summary = "친구 삭제",
            description = "친구를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 삭제 완료 "),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구 입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<?> deleteFriend(@RequestParam Long friendMemberId,
                                          Principal principal) {
        ApiResponseTemplate<?> data = friendService.deleteFriend(friendMemberId,principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

package com.skhu.moodfriend.app.controller.friend;

import com.skhu.moodfriend.app.dto.friend.reqDto.FriendReqDto;
import com.skhu.moodfriend.app.dto.friend.resDto.FriendResDto;
import com.skhu.moodfriend.app.service.friend.FriendService;
import com.skhu.moodfriend.app.service.friend.FriendDisplayService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "친구 관리", description = "친구 관리를 담당하는 api 그룹")
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;
    private final FriendDisplayService friendDisplayService;

    @PostMapping("/request")
    @Operation(
            summary = "친구 추가 요청",
            description = "친구 추가 요청을 보냅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 추가 요청 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 친구입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> sendFriendRequest(@RequestBody FriendReqDto friendReqDto, Principal principal) {
        ApiResponseTemplate<String> data = friendService.sendFriendRequest(friendReqDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/requests/display")
    @Operation(
            summary = "친구 추가 요청 리스트",
            description = "요청한 친구 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 추가 요청 리스트 조회"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<FriendReqDto>>> getFriendRequests(Principal principal) {
        ApiResponseTemplate<List<FriendReqDto>> data = friendDisplayService.getFriendRequests(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @PutMapping("/accept")
    @Operation(
            summary = "친구 요청 수락",
            description = "요청된 친구 요청을 수락합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 추가 요청 수락"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> acceptFriendRequest(@RequestParam String friendEmail, Principal principal) {
        ApiResponseTemplate<String> data = friendService.acceptFriendRequest(friendEmail, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/list")
    @Operation(
            summary = "친구 리스트 조회",
            description = "친구 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 리스트 조회 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<FriendResDto>>> getFriends(Principal principal) {
        ApiResponseTemplate<List<FriendResDto>> data = friendDisplayService.getFriends(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "친구 삭제",
            description = "친구를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 삭제 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<String>> deleteFriend(@RequestParam String friendEmail, Principal principal) {
        ApiResponseTemplate<String> data = friendService.deleteFriend(friendEmail, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}

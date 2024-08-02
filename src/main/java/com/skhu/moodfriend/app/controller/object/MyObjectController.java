package com.skhu.moodfriend.app.controller.object;

import com.skhu.moodfriend.app.dto.object.MemberObjectDto;
import com.skhu.moodfriend.app.dto.object.ObjectStoreDto;
import com.skhu.moodfriend.app.service.object.ObjectService;
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
@Tag(name = "마이 오브제", description = "마이 오브제 api 그룹")
@RequestMapping("/api/v1/object/my")
public class MyObjectController {

    private final ObjectService objectService;

    @PostMapping("/add")
    @Operation(
            summary = "마이 오브제 추가(구매) ",
            description = "마이 오브제 추가 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 추가 요청 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 등록한 오브제입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<?>> addNewObject(@RequestBody MemberObjectDto objectDto, Principal principal) {
        ApiResponseTemplate<?> data = objectService.addMyNewObject(objectDto, principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }


    @GetMapping("")
    @Operation(
            summary = "마이 오브제 리스트",
            description = "마이 오브제 리스트 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 리스트 요청 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 등록한 오브제입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<ObjectStoreDto>>> fetchObjectList(Principal principal) {
        ApiResponseTemplate< List<ObjectStoreDto>> data = objectService.fetchObjectList(principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    // 사용자 오브제 사용
    @PostMapping("/use")
    @Operation(
            summary = "오브제 업데이트",
            description = "오브제 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 업데이트합니다"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 등록한 오브제입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<Void>> useObjectUpdate(@RequestParam Long memberObjectId, Principal principal) {
        ApiResponseTemplate<Void> data = objectService.useMemberObject(memberObjectId,principal);
        return ResponseEntity.status(data.getStatus()).body(data);
    }



}

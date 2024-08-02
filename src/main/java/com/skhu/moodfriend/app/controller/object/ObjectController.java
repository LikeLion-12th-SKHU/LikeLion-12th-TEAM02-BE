package com.skhu.moodfriend.app.controller.object;

import com.skhu.moodfriend.app.domain.store.ObjectStore;
import com.skhu.moodfriend.app.dto.object.ObjectStoreDto;
import com.skhu.moodfriend.app.service.object.ObjectService;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.GET;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "오브제", description = "오브제 api 그룹")
@RequestMapping("/api/v1/object")
public class ObjectController {
    private final ObjectService objectService;
    // 새로운 오브제를 시스템에 추가
    @PostMapping("/add")
    @Operation(
            summary = "오브제 추가 ",
            description = "오브제 추가 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 추가 요청 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 등록한 오브제입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<Void>> addNewObject(@RequestBody ObjectStoreDto objectDto) {
        ApiResponseTemplate<Void> data = objectService.addNewObject(objectDto);
        return ResponseEntity.status(data.getStatus()).body(data);
    }


    @GET("")
    @Operation(
            summary = "전체 오브제 조회 ",
            description = "오브제 조회 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오브제 추가 요청 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 문제 or 관리자 문의"),
                    @ApiResponse(responseCode = "404", description = "오브제 정보를 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 등록한 오브제입니다"),
                    @ApiResponse(responseCode = "500", description = "서버 문제 or 관리자 문의")
            }
    )
    public ResponseEntity<ApiResponseTemplate<List<ObjectStore>>> listAllObjects() {
        ApiResponseTemplate<List<ObjectStore>> apiResponseTemplate = objectService.listAllObjects();
        return ResponseEntity.status(apiResponseTemplate.getStatus()).body(apiResponseTemplate);
    }


}

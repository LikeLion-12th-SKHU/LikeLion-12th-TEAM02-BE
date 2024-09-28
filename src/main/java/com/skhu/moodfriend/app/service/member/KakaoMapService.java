package com.skhu.moodfriend.app.service.member;

import com.skhu.moodfriend.app.dto.member.reqDto.HospitalReqDto;
import com.skhu.moodfriend.app.dto.member.resDto.HospitalResDto;
import com.skhu.moodfriend.app.repository.KakaoMapRepository;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapService {

    @Value("${kakao-map.client-id}")
    private String KAKAO_CLIENT_ID;

    private static final String KAKAO_AUTH_PREFIX = "KakaoAK ";

    private final KakaoMapRepository kakaoMapRepository;

    public ApiResponseTemplate<List<HospitalResDto>> retrieveByKeyword(HospitalReqDto reqDto) {
        Map<String, Object> response = kakaoMapRepository.searchByKeyword(
                KAKAO_AUTH_PREFIX + KAKAO_CLIENT_ID,
                reqDto.query(), reqDto.x(), reqDto.y(), reqDto.radius()
        );

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

        List<HospitalResDto> resDtos = documents.stream()
                .map(doc -> new HospitalResDto(
                        (String) doc.get("place_name"),
                        (String) doc.get("place_url"),
                        parseInteger(doc.get("distance")),
                        (String) doc.get("category_name")
                ))
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_HOSPITALS_SUCCESS, resDtos);
    }

    private Integer parseInteger(Object value) {
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }
}
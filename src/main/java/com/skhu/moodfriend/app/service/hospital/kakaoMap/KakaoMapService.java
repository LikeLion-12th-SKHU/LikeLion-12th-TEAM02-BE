package com.skhu.moodfriend.app.service.hospital.kakaoMap;

import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapDocument;
import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeyword;
import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeywordRequest;
import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeywordResDto;
import com.skhu.moodfriend.app.repository.KakaoMapRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapService {
    @Value("${kakao-map.client-id}")
    private String KAKAO_CLIENT_ID;

    private static final String KAKAO_AUTH_PREFIX = "KakaoAK ";

    private final KakaoMapRepository kakaoMapRepository;

    public List<KakaoMapKeywordResDto> retrieveByKeyword(KakaoMapKeywordRequest request) {
        KakaoMapKeyword response = kakaoMapRepository.searchByKeyword(
                KAKAO_AUTH_PREFIX + KAKAO_CLIENT_ID,
                request.query(),
                request.x(),
                request.y(),
                request.radius()
        );

        return response
                .documents()
                .stream()
                .sorted(Comparator.comparing(KakaoMapDocument::distance))
                .map(it -> new KakaoMapKeywordResDto(
                        it.categoryName(),
                        it.placeName(),
                        it.distance(),
                        it.placeUrl()
                ))
                .collect(Collectors.toList());
    }
}

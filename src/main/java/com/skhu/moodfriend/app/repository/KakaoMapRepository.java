package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeyword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakaomap-client",
        url = "https://dapi.kakao.com"
)
public interface KakaoMapRepository {
    @GetMapping(value = "/v2/local/search/keyword.json")
    KakaoMapKeyword searchByKeyword(
            @RequestHeader("Authorization") String auth,
            @RequestParam("query") String query,
            @RequestParam("x") String x,
            @RequestParam("y") String y,
            @RequestParam("radius") String radius
    );

}
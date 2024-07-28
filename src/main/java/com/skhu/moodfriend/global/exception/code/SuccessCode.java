package com.skhu.moodfriend.global.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    // 200 OK
    GET_TOKEN_SUCCESS(HttpStatus.OK, "Access 토큰을 성공적으로 가져왔습니다."),
    LOGIN_MEMBER_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    GET_MEMBER_INFO_SUCCESS(HttpStatus.OK, "사용자 정보 조회에 성공했습니다."),
    UPDATE_MEMBER_INFO_SUCCESS(HttpStatus.OK, "사용자 정보 수정에 성공했습니다."),
    GET_DIARY_SUCCESS(HttpStatus.OK, "일기 조회에 성공했습니다."),
    GET_ALL_DIARIES_SUCCESS(HttpStatus.OK, "모든 일기 조회에 성공했습니다."),
    UPDATE_DIARY_SUCCESS(HttpStatus.OK, "일기 수정에 성공했습니다."),
    GET_FRIENDS_SUCCESS(HttpStatus.OK, "친구 목록 조회에 성공했습니다."),
    ATTENDANCE_SUCCESS(HttpStatus.OK, "출석 및 마일리지 적립에 성공했습니다."),
    GET_MONTHLY_EMOTION_TYPES_SUCCESS(HttpStatus.OK, "월별 감정 리스트 조회에 성공했습니다."),
    GET_HOSPITALS_SUCCESS(HttpStatus.OK, "의료기관 조회에 성공했습니다."),
    GET_HOYA_SUCCESS(HttpStatus.OK, "챗봇 응답을 성공적으로 가져왔습니다."),
    GET_DIARY_SUMMARY_SUCCESS(HttpStatus.OK, "일기 AI 조회에 성공했습니다."),
    GET_ALL_DIARY_SUMMARIES_SUCCESS(HttpStatus.OK, "모든 일기 AI 조회에 성공했습니다."),
    ORDER_SAVE_SUCCESS(HttpStatus.OK, "주문에 성공했습니다."),

    // 201 Created
    CREATE_MEMBER_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다."),
    CREATE_DIARY_SUCCESS(HttpStatus.CREATED, "일기 작성에 성공했습니다."),
    ADD_FRIEND_SUCCESS(HttpStatus.OK, "친구 추가에 성공했습니다."),

    // 204 No Content
    DELETE_DIARY_SUCCESS(HttpStatus.NO_CONTENT, "일기 삭제에 성공했습니다."),
    DELETE_FRIEND_SUCCESS(HttpStatus.OK, "친구 삭제에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}

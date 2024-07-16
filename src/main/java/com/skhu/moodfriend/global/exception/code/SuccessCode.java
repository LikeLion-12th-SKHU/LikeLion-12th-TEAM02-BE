package com.skhu.moodfriend.global.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    // 200 OK
    LOGIN_MEMBER_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    GET_MEMBER_INFO_SUCCESS(HttpStatus.OK, "사용자 정보 조회에 성공했습니다."),
    UPDATE_MEMBER_INFO_SUCCESS(HttpStatus.OK, "사용자 정보 수정에 성공했습니다."),
    GET_DIARY_SUCCESS(HttpStatus.OK, "일기 조회에 성공했습니다."),
    GET_ALL_DIARIES_SUCCESS(HttpStatus.OK, "모든 일기 조회에 성공했습니다."),
    UPDATE_DIARY_SUCCESS(HttpStatus.OK, "일기 수정에 성공했습니다."),
    GET_FRIENDS_SUCCESS(HttpStatus.OK, "친구 목록 조회에 성공했습니다."),

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

package com.skhu.moodfriend.global.template;

import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class ApiResponseTemplate<T> {

    private final boolean success;
    private final int status;
    private final String message;
    private T data;

    public static <T> ApiResponseTemplate<T> success(SuccessCode successCode, T data) {
        return ApiResponseTemplate.<T>builder()
                .success(true)
                .status(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponseTemplate<T> error(ErrorCode errorCode) {
        return ApiResponseTemplate.<T>builder()
                .success(false)
                .status(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}

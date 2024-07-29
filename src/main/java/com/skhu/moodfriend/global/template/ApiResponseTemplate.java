package com.skhu.moodfriend.global.template;

import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ApiResponseTemplate<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private T data;
    private ErrorCode errorCode;

    public static <T> ApiResponseTemplate<T> success(SuccessCode successCode, T data) {
        return ApiResponseTemplate.<T>builder()
                .status(successCode.getHttpStatus().value())
                .success(true)
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponseTemplate<T> success(SuccessCode successCode) {
        return ApiResponseTemplate.<T>builder()
                .status(successCode.getHttpStatus().value())
                .success(true)
                .message(successCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiResponseTemplate<T> error(ErrorCode errorCode, String message) {
        return ApiResponseTemplate.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}

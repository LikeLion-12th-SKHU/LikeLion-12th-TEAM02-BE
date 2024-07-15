package com.skhu.moodfriend.app.entity.user;

import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    ROLE_USER("USER", "사용자"),
    ROLE_ADMIN("ADMIN", "관리자");

    private final String code;
    private final String displayName;

    public static RoleType getRoleTypeOfString(String roleType) {
        for (RoleType type : RoleType.values()) {
            if (type.code.equals(roleType)) {
                return type;
            }
        }

        throw new CustomException(ErrorCode.INVALID_ROLE_TYPE_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
    }
}

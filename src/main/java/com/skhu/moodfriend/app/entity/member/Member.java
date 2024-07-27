package com.skhu.moodfriend.app.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "MEMBER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "MEMBER_NAME", length = 10, nullable = false)
    private String name;

    @Column(name = "MILEAGE")
    private long mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOGIN_TYPE", nullable = false)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE", nullable = false)
    private RoleType roleType;

    @Builder
    private Member(String email, String password, String name, long mileage, LoginType loginType, RoleType roleType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mileage = mileage;
        this.loginType = loginType;
        this.roleType = roleType;
    }

    public void updateInfo(String name) {
        this.name = name;
    }

    public void updateMileage(long mileageIncrement) {
        this.mileage += mileageIncrement;
    }
}

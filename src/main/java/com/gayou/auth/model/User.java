package com.gayou.auth.model;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    private String email;

    private String phoneNumber;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private boolean isGender;

    private boolean isLocal;

    @Lob
    @Column(columnDefinition = "LONGTEXT") // 데이터베이스 컬럼 타입을 LONGTEXT로 설정
    private String profilePicture;

    private String description; // 사용자 설명 필드

    @Temporal(TemporalType.TIMESTAMP) // 시간과 날짜를 함께 저장
    private Date lastLoginTime; // 마지막 로그인 시간

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private AccountStatus status; // 계정 상태 필드 (ACTIVE, SUSPENDED 등)
}

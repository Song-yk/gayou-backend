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
    @Size(min = 2, max = 30) // 사용자 이름 길이 제한
    private String username;

    @NotBlank
    @Size(min = 2, max = 50) // 이름 길이 제한
    private String name;

    @NotBlank
    @Size(min = 6, max = 100) // 비밀번호 길이 제한
    private String password;

    @NotBlank
    @Email // 유효한 이메일 형식 검사
    private String email;

    @Pattern(regexp = "^(01[016789]{1})-?[0-9]{3,4}-?[0-9]{4}$") // 전화번호 형식 검사
    private String phoneNumber;

    @Past // 생일은 과거 날짜만 허용
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private Date birthday;

    private String gender; // 성별 필드 (MALE, FEMALE, OTHER 등)

    private boolean isLocal; // 지역 여부 필드

    @Lob // 대용량 데이터를 처리할 수 있도록 설정
    @Column(columnDefinition = "LONGTEXT") // 데이터베이스 컬럼 타입을 LONGTEXT로 설정
    private String profilePicture;

    private String description; // 사용자 설명 필드

    @Temporal(TemporalType.TIMESTAMP) // 시간과 날짜를 함께 저장
    private Date lastLoginTime; // 마지막 로그인 시간

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private AccountStatus status; // 계정 상태 필드 (ACTIVE, SUSPENDED 등)
}

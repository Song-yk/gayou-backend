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
    @Size(min = 2, max = 30, message = "크기가 2에서 30 사이여야 합니다")
    private String username;

    @NotBlank
    @Size(min = 2, max = 50, message = "크기가 2에서 50 사이여야 합니다")
    private String name;

    @NotBlank
    @Size(min = 6, max = 100, message = "크기가 6에서 100 사이여야 합니다")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^(01[016789]{1})-?[0-9]{3,4}-?[0-9]{4}$", message = "유효한 전화번호 형식이어야 합니다") // 전화번호 형식 검사
    private String phoneNumber;

    // @NotNull(message = "생일은 필수 항목입니다")
    @Past(message = "생일은 과거 날짜여야 합니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime; // 마지막 로그인 시간 추가

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // 계정 상태 필드 추가 (ACTIVE, SUSPENDED 등)
}

package com.gayou.auth.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 2, max = 30, message = "크기가 2에서 30 사이여야 합니다")
    private String username;

    @NotBlank
    @Size(min = 6, max = 100, message = "크기가 6에서 100 사이여야 합니다")
    private String password;

    @Email(message = "Email should be valid")
    private String email;
}

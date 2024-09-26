package com.gayou.auth.dto;

import java.util.Date;

import com.gayou.auth.model.AccountStatus;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	private String username;
	private String name;
	private String password;
	private String newPassword;
	private String email;
	private String phoneNumber;
	private Date birthday;
	private String gender;
	private Boolean isLocal;
	private String profilePicture;
	private String description;
	private AccountStatus status;
}

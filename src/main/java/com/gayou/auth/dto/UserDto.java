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
	private String email;
	private String phoneNumber;
	private Date birthday;
	private AccountStatus status;
}

package com.gayou.route.dto;

import java.util.Date;

import com.gayou.auth.dto.UserDto;

import lombok.Data;

@Data
public class RouteCommentDto {
    private Long id;
    private RouteHeadDto routeHead;
    private UserDto user;
    private String comment;
    private Date createDate;
}

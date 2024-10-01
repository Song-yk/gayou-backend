package com.gayou.route.dto;

import com.gayou.auth.dto.UserDto;

import lombok.Data;

@Data
public class RouteBookmarkDto {
    private Long id;
    private RouteHeadDto routeHead;
    private UserDto user;
}
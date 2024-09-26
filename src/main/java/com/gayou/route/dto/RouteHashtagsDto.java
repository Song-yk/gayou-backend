package com.gayou.route.dto;

import com.gayou.hashtag.dto.HashtagDto;

import lombok.Data;

@Data
public class RouteHashtagsDto {
    private Long id;
    private RouteHeadDto routeHead;
    private HashtagDto hashtag;
    private int orderNumber;
}

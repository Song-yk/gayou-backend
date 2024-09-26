package com.gayou.route.dto;

import lombok.Data;
import com.gayou.hashtag.dto.HashtagDto;

@Data
public class RouteHashtagsDto {
    private Long id;
    private RouteHeadDto routeHead;
    private HashtagDto hashtag;
    private int orderNumber;
}

package com.gayou.route.dto;

import java.util.List;

import lombok.Data;

@Data
public class RouteDto {
    private RouteHeadDto routeHead;
    private List<RouteItemDto> routeItems;
}

package com.gayou.route.dto;

import com.gayou.places.dto.PlacesDto;

import lombok.Data;

@Data
public class RouteItemDto {
    private Long id;
    private Long headId;
    private PlacesDto contentid;
}

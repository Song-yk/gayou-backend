package com.gayou.route.dto;

import lombok.Data;

import com.gayou.places.dto.PlacesDto;

@Data
public class RouteItemDto {
    private Long id;
    private Long headId;
    private PlacesDto contentid;
}

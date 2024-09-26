package com.gayou.route.dto;

import java.util.Date;
import java.util.List;

import com.gayou.hashtag.dto.HashtagDto;

import lombok.Data;

@Data
public class RouteHeadDto {
    private Long id;
    private Long userId;
    private String town;
    private String courseName;
    private Long totDistance;
    private Date createDate;
    private Date updateDate;
    private List<RouteItemDto> data;
    private List<String> tag;
    private String content;
    private Long totlike;
}

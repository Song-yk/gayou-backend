package com.gayou.route.dto;

import java.util.Date;
import java.util.List;

import com.gayou.auth.dto.UserDto;

import lombok.Data;

@Data
public class RouteHeadDto {
    private Long id;
    private UserDto userId;
    private String town;
    private String courseName;
    private Long totDistance;
    private Date createDate;
    private Date updateDate;
    private List<RouteItemDto> data;
    private List<String> tag;
    private String content;
    private Long totlike;
    private boolean isPublic;
    private RouteBookmarkDto bookmark;
    private RouteLikeDto like;
    private List<RouteCommentDto> comments;
    private int totComment;
}

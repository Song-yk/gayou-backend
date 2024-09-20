package com.gayou.places.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PlacesDto {
    private Integer contentid;
    private String title;
    private String addr1;
    private String addr2;
    private Integer areacode;
    private Double booktour;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contenttypeid;
    private Date createdtime;
    private String firstimage;
    private String firstimage2;
    private Double mapx;
    private Double mapy;
    private Date modifiedtime;
    private String tel;
    private String overview;
    private Date lastUpdated;

    public PlacesDto(Integer contentid) {
        this.contentid = contentid;
    }
}

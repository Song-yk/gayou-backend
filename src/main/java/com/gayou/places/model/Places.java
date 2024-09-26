package com.gayou.places.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Places {
    @Id
    private Integer contentid;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String addr1;

    @Column(columnDefinition = "TEXT")
    private String addr2;

    private Integer areacode;

    private Double booktour;

    @Column(columnDefinition = "TEXT")
    private String cat1;

    @Column(columnDefinition = "TEXT")
    private String cat2;

    @Column(columnDefinition = "TEXT")
    private String cat3;

    @Column(columnDefinition = "TEXT")
    private String contenttypeid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdtime;

    @Column(columnDefinition = "TEXT")
    private String firstimage;

    @Column(columnDefinition = "TEXT")
    private String firstimage2;

    private Double mapx;

    private Double mapy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedtime;

    @Column(columnDefinition = "TEXT")
    private String tel;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(name = "last_updated", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}

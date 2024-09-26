package com.gayou.route.model;

import com.gayou.hashtag.model.Hashtag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "route_hashtags")
public class RouteHashtags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_head_id", nullable = false)
    private RouteHead routeHead;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;

    private int orderNumber;
}

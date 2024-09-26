package com.gayou.hashtag.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.gayou.route.model.RouteHashtags;

@Entity
@Getter
@Setter
@Table(name = "hashtag")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tagName;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<RouteHashtags> routeHashtags;
}

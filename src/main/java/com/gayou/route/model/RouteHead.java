package com.gayou.route.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.gayou.auth.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RouteHead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private String town;

    private String courseName;

    private Long totDistance;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    @OneToMany(mappedBy = "routeHead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteItem> data;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long totlike = 0L;

    @OneToMany(mappedBy = "routeHead", cascade = CascadeType.ALL)
    private List<RouteHashtags> routeHashtags;

    private boolean isPublic;

    @OneToMany(mappedBy = "routeHead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteBookmark> bookmarks = new ArrayList<>();
}

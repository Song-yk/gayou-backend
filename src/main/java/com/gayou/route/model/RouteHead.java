package com.gayou.route.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import com.gayou.auth.model.User;

@Entity
@Getter
@Setter
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

    @NotBlank
    private String courseName;

    private Long totDistance;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    @OneToMany(mappedBy = "routeHead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteItem> data;
}

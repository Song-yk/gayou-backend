package com.gayou.route.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gayou.places.model.Places;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RouteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "head_id", nullable = false)
    private RouteHead routeHead;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contentid", nullable = false)
    private Places place;
}

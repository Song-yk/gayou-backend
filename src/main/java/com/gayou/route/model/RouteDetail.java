package com.gayou.route.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "route_detail")
public class RouteDetail {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private Long item_id;

    @NotBlank
    private Long content_id;
}

package com.gayou.route.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "route_item")
public class RouteItem {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private Long head_id;

    @NotBlank
    private int days;

    @NotBlank
    private Long tot_distance;
}

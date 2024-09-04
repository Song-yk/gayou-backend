package com.gayou.route.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "route_head")
public class RouteHead {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private Long user_id;

    @NotBlank
    private String town;

    @NotBlank
    private String course_name;

    @NotBlank
    private Long tot_distance;

    @NotBlank
    private String create_date;

    @NotBlank
    private String update_date;
}

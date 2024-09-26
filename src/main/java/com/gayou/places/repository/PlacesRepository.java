package com.gayou.places.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gayou.places.model.Places;

@Repository
public interface PlacesRepository extends JpaRepository<Places, Integer> {
    Optional<Places> findByContentid(Integer contentid);
}

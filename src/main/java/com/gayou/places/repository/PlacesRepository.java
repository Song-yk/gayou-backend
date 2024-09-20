package com.gayou.places.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gayou.places.model.Places;

import java.util.Optional;

@Repository
public interface PlacesRepository extends JpaRepository<Places, Integer> {
    Optional<Places> findByContentid(Integer contentid);
}

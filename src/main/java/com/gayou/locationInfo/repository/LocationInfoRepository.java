package com.gayou.locationInfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gayou.locationInfo.model.LocationInfo;

@Repository
public interface LocationInfoRepository extends JpaRepository<LocationInfo, Long> {

    @Query("SELECT li.neighborhood FROM LocationInfo li WHERE li.city = :city AND li.district = :district")
    List<String> findNeighborhoodsByCityAndDistrict(@Param("city") String city, @Param("district") String district);

    @Query("SELECT li.neighborhood FROM LocationInfo li WHERE li.city = :city AND li.neighborhood != '전체'")
    List<String> findNeighborhoodsByCity(@Param("city") String city);

    // List<LocationInfo> findByCityAndDistrict(String city, String district);
}

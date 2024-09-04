package com.gayou.locationInfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.gayou.locationInfo.entity.LocationInfo;
import com.gayou.locationInfo.repository.LocationInfoRepository;

@Service
public class LocationInfoService {

    @Autowired
    private LocationInfoRepository locationInfoRepository;
    
    public List<String> getNeighborhoodsByCityAndDistrict(String city, String district) {
        try {
            return locationInfoRepository.findNeighborhoodsByCityAndDistrict(city, district);
        } catch (Exception e) {
            throw new RuntimeException("Error neighborhood list", e);
        }
    }
    
    public List<String> getNeighborhoodsByCity(String city) {
        try {
            return locationInfoRepository.findNeighborhoodsByCity(city);
        } catch (Exception e) {
               throw new RuntimeException("Error neighborhood list", e);
        }
    }
}

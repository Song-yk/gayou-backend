package com.gayou.locationInfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gayou.locationInfo.service.LocationInfoService;

@RestController
public class LocationInfoController {

    @Autowired
    private LocationInfoService locationInfoService;

    // 지역과 구를 기준으로 동네 목록 조회
    @GetMapping("/locations/district")
    public ResponseEntity<List<String>> getNeighborhoodsByCityAndDistrict(
            @RequestParam(value="city") String city, 
            @RequestParam(value="district") String district) {
        try {
            List<String> locationInfos = locationInfoService.getNeighborhoodsByCityAndDistrict(city, district);
            if (locationInfos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            return new ResponseEntity<>(locationInfos, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    // 도시를 기준으로 동네 목록 조회
    @GetMapping("/locations/city")
    public ResponseEntity<List<String>> getNeighborhoodsByCity(
            @RequestParam(value="city") String city) {
        try {
            List<String> locationInfos = locationInfoService.getNeighborhoodsByCity(city);
            if (locationInfos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            return new ResponseEntity<>(locationInfos, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
}

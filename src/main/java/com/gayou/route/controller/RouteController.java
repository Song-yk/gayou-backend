package com.gayou.route.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/locations")
    public List<AreaBasedList> getAreaBasedLists() {
        List<AreaBasedList> locations = new ArrayList<>();
        locations.add(new AreaBasedList(36.37527816563488, 127.38137948400885,
                "http://tong.visitkorea.or.kr/cms/resource/30/3062830_image2_1.JPG",
                "http://tong.visitkorea.or.kr/cms/resource/30/3062830_image3_1.JPG", "신세계"));
        locations.add(new AreaBasedList(36.37001208152014, 127.38798395419047,
                "http://tong.visitkorea.or.kr/cms/resource/35/3051735_image2_1.JPG",
                "http://tong.visitkorea.or.kr/cms/resource/35/3051735_image3_1.JPG", "한밭수목원"));
        locations.add(new AreaBasedList(36.3505388634306, 127.38484598695104,
                "http://tong.visitkorea.or.kr/cms/resource/78/3032478_image2_1.JPG",
                "http://tong.visitkorea.or.kr/cms/resource/78/3032478_image3_1.JPG", "대전시청"));
        locations.add(new AreaBasedList(36.35182179346913, 127.37817910651867,
                "http://tong.visitkorea.or.kr/cms/resource/83/2866683_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/83/2866683_image3_1.jpg", "갤러리아"));
        locations.add(new AreaBasedList(36.327515534946336, 127.426803087373,
                "http://tong.visitkorea.or.kr/cms/resource/68/2912768_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/68/2912768_image3_1.jpg", "성심당"));
        return locations;
    }

}

class AreaBasedList {
    private double mapx;
    private double mapy;
    private String firstimage;
    private String firstimage2;
    private String title;

    public AreaBasedList(double mapx, double mapy, String firstimage, String firstimage2, String title) {
        this.mapx = mapx;
        this.mapy = mapy;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.title = title;
    }

    public double getMapx() {
        return mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public String getTitle() {
        return title;
    }
}

package com.gayou.route.service;

import com.gayou.route.dto.RouteHeadDto;
import com.gayou.route.dto.RouteItemDto;
import com.gayou.places.dto.PlacesDto;
import com.gayou.auth.model.User;
import com.gayou.route.model.RouteHead;
import com.gayou.route.model.RouteItem;
import com.gayou.places.model.Places;
import com.gayou.places.repository.PlacesRepository;
import com.gayou.auth.repository.UserRepository;
import com.gayou.route.repository.RouteHeadRepository;
import com.gayou.route.repository.RouteItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RouteService {

    private final RouteHeadRepository routeHeadRepository;
    private final RouteItemRepository routeItemRepository;
    private final PlacesRepository placesRepository;
    private final UserRepository userRepository;

    public RouteService(RouteHeadRepository routeHeadRepository, RouteItemRepository routeItemRepository,
            PlacesRepository placesRepository,
            UserRepository userRepository) {
        this.routeHeadRepository = routeHeadRepository;
        this.routeItemRepository = routeItemRepository;
        this.placesRepository = placesRepository;
        this.userRepository = userRepository;
    }

    /**
     * 사용자의 경로(Route)를 저장하는 메서드
     *
     * @param routeDTO - 저장할 경로 정보 (RouteHeadDto)
     * @param email    - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return 저장된 경로의 ID
     */
    @Transactional
    public Long saveRoute(RouteHeadDto routeDTO, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        RouteHead routeHead = new RouteHead();
        routeHead.setUser(user);
        routeHead.setCourseName(routeDTO.getCourseName());
        routeHead.setCreateDate(new Date());
        routeHead.setTown(routeDTO.getTown());
        routeHead.setTotDistance(routeDTO.getTotDistance());

        RouteHead savedRouteHead = routeHeadRepository.save(routeHead);

        List<RouteItemDto> datas = routeDTO.getData();

        List<RouteItem> routeItems = new ArrayList<>();

        for (RouteItemDto data : datas) {
            Places place = placesRepository.findByContentid(data.getContentid().getContentid())
                    .orElseThrow(
                            () -> new RuntimeException("Place with contentid " + data.getContentid() + " not found"));
            RouteItem routeItem = new RouteItem();
            routeItem.setRouteHead(savedRouteHead);
            routeItem.setPlace(place);
            routeItems.add(routeItem);
        }

        routeItemRepository.saveAll(routeItems);

        return savedRouteHead.getId();
    }

    /**
     * 현재 사용자가 저장한 경로 목록을 가져오는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return 사용자가 저장한 경로 목록 (RouteHeadDto 리스트)
     */
    @Transactional
    public List<RouteHeadDto> getMyRoute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RouteHead> getRouteHead = routeHeadRepository.findAllByUserId(user.getId());

        List<RouteHeadDto> dtoData = new ArrayList<>();
        for (RouteHead head : getRouteHead) {
            RouteHeadDto routeHeadDto = new RouteHeadDto();
            routeHeadDto.setId(head.getId());
            routeHeadDto.setTown(head.getTown());
            routeHeadDto.setCourseName(head.getCourseName());
            routeHeadDto.setTotDistance(head.getTotDistance());
            routeHeadDto.setCreateDate(head.getCreateDate());
            routeHeadDto.setUpdateDate(head.getUpdateDate());

            List<RouteItemDto> dtoItemList = new ArrayList<>();
            List<RouteItem> routeItemList = head.getData();
            for (RouteItem item : routeItemList) {
                RouteItemDto routeItemDto = new RouteItemDto();
                routeItemDto.setId(item.getId());

                Places places = item.getPlace();
                PlacesDto placesDto = new PlacesDto(places.getContentid());
                placesDto.setTitle(places.getTitle());
                placesDto.setAddr1(places.getAddr1());
                placesDto.setAddr2(places.getAddr2());
                placesDto.setAreacode(places.getAreacode());
                placesDto.setBooktour(places.getBooktour());
                placesDto.setCat1(places.getCat1());
                placesDto.setCat2(places.getCat2());
                placesDto.setCat3(places.getCat3());
                placesDto.setContenttypeid(places.getContenttypeid());
                placesDto.setCreatedtime(places.getCreatedtime());
                placesDto.setFirstimage(places.getFirstimage());
                placesDto.setFirstimage2(places.getFirstimage2());
                placesDto.setMapx(places.getMapx());
                placesDto.setMapy(places.getMapy());
                placesDto.setModifiedtime(places.getModifiedtime());
                placesDto.setTel(places.getTel());
                placesDto.setOverview(places.getOverview());
                placesDto.setLastUpdated(places.getLastUpdated());

                routeItemDto.setContentid(placesDto);
                dtoItemList.add(routeItemDto);
            }

            routeHeadDto.setData(dtoItemList);

            dtoData.add(routeHeadDto);
        }
        return dtoData;
    }
}
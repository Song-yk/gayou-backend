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
import com.gayou.settings.provider.JwtProvider;
import com.gayou.route.repository.RouteHeadRepository;
import com.gayou.route.repository.RouteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public void saveRoute(RouteHeadDto routeDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

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
    }

    @Transactional
    public List<RouteHeadDto> getMyRoute(String username) {
        // 유저 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 유저 ID로 RouteHead 목록 가져오기
        List<RouteHead> getRouteHead = routeHeadRepository.findAllByUserId(user.getId());

        // DTO 변환
        List<RouteHeadDto> dtoData = new ArrayList<>();
        for (RouteHead head : getRouteHead) {
            RouteHeadDto routeHeadDto = new RouteHeadDto();
            routeHeadDto.setId(head.getId());
            routeHeadDto.setTown(head.getTown());
            routeHeadDto.setCourseName(head.getCourseName());
            routeHeadDto.setTotDistance(head.getTotDistance());
            routeHeadDto.setCreateDate(head.getCreateDate());
            routeHeadDto.setUpdateDate(head.getUpdateDate());

            // RouteItem을 RouteItemDto로 변환
            List<RouteItemDto> dtoItemList = new ArrayList<>();
            List<RouteItem> routeItemList = head.getData();
            for (RouteItem item : routeItemList) {
                RouteItemDto routeItemDto = new RouteItemDto();
                routeItemDto.setId(item.getId());

                // Places 엔티티를 PlacesDto로 변환
                Places places = item.getPlace(); // RouteItem의 Places 엔티티 가져오기
                PlacesDto placesDto = new PlacesDto(places.getContentid()); // PlacesDto로 변환
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

                // RouteItemDto에 PlacesDto 설정
                routeItemDto.setContentid(placesDto);
                dtoItemList.add(routeItemDto);
            }

            // RouteHeadDto에 RouteItemDto 리스트 설정
            routeHeadDto.setData(dtoItemList);

            // 최종적으로 RouteHeadDto 리스트에 추가
            dtoData.add(routeHeadDto);
        }
        return dtoData;
    }
}

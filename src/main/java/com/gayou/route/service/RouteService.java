package com.gayou.route.service;

import com.gayou.route.dto.RouteHeadDto;
import com.gayou.route.dto.RouteItemDto;
import com.gayou.auth.model.User;
import com.gayou.route.model.RouteHead;
import com.gayou.route.model.RouteItem;
import com.gayou.auth.repository.UserRepository;
import com.gayou.route.repository.RouteHeadRepository;
import com.gayou.route.repository.RouteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteHeadRepository routeHeadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteItemRepository routeItemRepository;

    @Transactional
    public void saveRoute(RouteHeadDto routeDTO) {
        Long userId = routeDTO.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        RouteHead routeHead = new RouteHead();
        routeHead.setUser(user);
        routeHead.setCourseName(routeDTO.getCourseName());
        routeHead.setCreateDate(new Date());
        routeHead.setTown(routeDTO.getTown());
        routeHead.setTotDistance(routeDTO.getTotDistance());

        RouteHead savedRouteHead = routeHeadRepository.save(routeHead);

        List<RouteItemDto> datas = routeDTO.getData();

        for (RouteItemDto data : datas) {
            RouteItem routeItem = new RouteItem();
            routeItem.setRouteHead(savedRouteHead);
            routeItem.setContentid(data.getContentid());
            routeItemRepository.save(routeItem);
        }
    }
}

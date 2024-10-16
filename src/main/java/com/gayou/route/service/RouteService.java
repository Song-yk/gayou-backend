package com.gayou.route.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gayou.auth.dto.UserDto;
import com.gayou.auth.model.User;
import com.gayou.auth.repository.UserRepository;
import com.gayou.hashtag.model.Hashtag;
import com.gayou.hashtag.repository.HashtagRepository;
import com.gayou.places.dto.PlacesDto;
import com.gayou.places.model.Places;
import com.gayou.places.repository.PlacesRepository;
import com.gayou.route.dto.RouteBookmarkDto;
import com.gayou.route.dto.RouteCommentDto;
import com.gayou.route.dto.RouteHeadDto;
import com.gayou.route.dto.RouteItemDto;
import com.gayou.route.dto.RouteLikeDto;
import com.gayou.route.model.RouteBookmark;
import com.gayou.route.model.RouteComment;
import com.gayou.route.model.RouteHashtags;
import com.gayou.route.model.RouteHead;
import com.gayou.route.model.RouteItem;
import com.gayou.route.model.RouteLike;
import com.gayou.route.repository.RouteBookmarkRepository;
import com.gayou.route.repository.RouteCommentRepository;
import com.gayou.route.repository.RouteHashtagsRepository;
import com.gayou.route.repository.RouteHeadRepository;
import com.gayou.route.repository.RouteItemRepository;
import com.gayou.route.repository.RouteLikeRepository;

@Service
public class RouteService {

    private final RouteHeadRepository routeHeadRepository;
    private final RouteItemRepository routeItemRepository;
    private final PlacesRepository placesRepository;
    private final HashtagRepository hashtagRepository;
    private final RouteHashtagsRepository routeHashtagsRepository;
    private final RouteBookmarkRepository routeBookmarkRepository;
    private final RouteLikeRepository routeLikeRepository;
    private final RouteCommentRepository routeCommentRepository;
    private final UserRepository userRepository;

    public RouteService(RouteHeadRepository routeHeadRepository, RouteItemRepository routeItemRepository,
            PlacesRepository placesRepository, RouteHashtagsRepository routeHashtagsRepository,
            HashtagRepository hashtagRepository, RouteBookmarkRepository routeBookmarkRepository,
            RouteLikeRepository routeLikeRepository, RouteCommentRepository routeCommentRepository,
            UserRepository userRepository) {
        this.routeHeadRepository = routeHeadRepository;
        this.routeItemRepository = routeItemRepository;
        this.placesRepository = placesRepository;
        this.hashtagRepository = hashtagRepository;
        this.routeHashtagsRepository = routeHashtagsRepository;
        this.routeBookmarkRepository = routeBookmarkRepository;
        this.routeLikeRepository = routeLikeRepository;
        this.routeCommentRepository = routeCommentRepository;
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
        routeHead.setCreateDate(new Date());
        routeHead.setTown(routeDTO.getTown());
        routeHead.setTotDistance(routeDTO.getTotDistance());
        routeHead.setPublic(false);

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
    public List<RouteHeadDto> getMyRoute(String email, boolean flag) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RouteHead> headList = new ArrayList<>();
        if (flag) {
            headList = routeHeadRepository.findAllByUserIdAndCourseNameIsNotNull(user.getId(),
                    Sort.by("id").descending());
        } else {
            headList = routeHeadRepository.findAllByUserIdAndCourseNameIsNull(user.getId(),
                    Sort.by("id").descending());
        }

        List<RouteHeadDto> routeHeadDtoList = new ArrayList<>();

        for (RouteHead head : headList) {
            RouteHeadDto routeHeadDto = new RouteHeadDto();
            routeHeadDto.setId(head.getId());
            routeHeadDto.setTown(head.getTown());
            routeHeadDto.setCourseName(head.getCourseName());
            routeHeadDto.setTotDistance(head.getTotDistance());
            routeHeadDto.setContent(head.getContent());
            routeHeadDto.setTotlike(head.getTotlike());
            routeHeadDto.setCreateDate(head.getCreateDate());
            routeHeadDto.setUpdateDate(head.getUpdateDate());
            routeHeadDto.setPublic(head.isPublic());

            List<String> hashtagList = new ArrayList<>();
            for (RouteHashtags routeHashtag : head.getRouteHashtags()) {
                hashtagList.add(routeHashtag.getHashtag().getTagName());
            }

            routeHeadDto.setTag(hashtagList);

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
            routeHeadDtoList.add(routeHeadDto);
        }

        return routeHeadDtoList;
    }

    @Transactional
    public void routeDeleteLocations(Long id) {
        RouteHead head = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("route not found"));
        routeHeadRepository.delete(head);
    }

    @Transactional
    public RouteHeadDto getRoute(String email, Long id) {
        RouteHead head = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("route not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        RouteHeadDto routeHeadDto = new RouteHeadDto();
        routeHeadDto.setId(head.getId());
        routeHeadDto.setTown(head.getTown());
        routeHeadDto.setCourseName(head.getCourseName());
        routeHeadDto.setTotDistance(head.getTotDistance());
        routeHeadDto.setContent(head.getContent());
        routeHeadDto.setTotlike(head.getTotlike());
        routeHeadDto.setCreateDate(head.getCreateDate());
        routeHeadDto.setUpdateDate(head.getUpdateDate());
        routeHeadDto.setPublic(head.isPublic());

        UserDto userIdDto = new UserDto();
        userIdDto.setId(user.getId());
        userIdDto.setProfilePicture(user.getProfilePicture());
        routeHeadDto.setUserId(userIdDto);

        RouteBookmark routeBookmark = routeBookmarkRepository.findByRouteHeadAndUser(head, user);
        RouteBookmarkDto routeBookmarkDto = new RouteBookmarkDto();
        if (routeBookmark != null) {
            routeBookmarkDto.setId(routeBookmark.getId());
        }
        routeHeadDto.setBookmark(routeBookmarkDto);

        RouteLike routeLike = routeLikeRepository.findByRouteHeadAndUser(head, user);
        RouteLikeDto routeLikeDto = new RouteLikeDto();
        if (routeLike != null) {
            routeLikeDto.setId(routeLike.getId());
        }
        routeHeadDto.setLike(routeLikeDto);

        List<String> hashtagList = new ArrayList<>();
        for (RouteHashtags routeHashtag : head.getRouteHashtags()) {
            hashtagList.add(routeHashtag.getHashtag().getTagName());
        }

        routeHeadDto.setTag(hashtagList);

        List<RouteComment> routeComments = routeCommentRepository.findAllByRouteHead(head,
                Sort.by("createDate").descending());
        List<RouteCommentDto> routeCommentDtos = new ArrayList<>();
        for (RouteComment routeComment : routeComments) {
            RouteCommentDto routeCommentDto = new RouteCommentDto();
            routeCommentDto.setId(routeComment.getId());
            routeCommentDto.setComment(routeComment.getComment());
            routeCommentDto.setCreateDate(routeComment.getCreateDate());
            User userComment = routeComment.getUser();
            UserDto userDto = new UserDto();
            userDto.setId(userComment.getId());
            userDto.setProfilePicture(userComment.getProfilePicture());
            userDto.setName(userComment.getName());
            routeCommentDto.setUser(userDto);
            routeCommentDtos.add(routeCommentDto);
        }

        routeHeadDto.setComments(routeCommentDtos);
        routeHeadDto.setTotComment(routeComments.size());

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

        return routeHeadDto;
    }

    @Transactional
    public List<RouteHeadDto> getRoutes(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        List<RouteHead> headList = routeHeadRepository.findByIsPublicAndUserIdNot(true, user.getId(),
                Sort.by(Sort.Order.desc("createDate")));
        List<RouteHeadDto> routeHeadDtoList = new ArrayList<>();

        for (RouteHead head : headList) {
            RouteHeadDto routeHeadDto = new RouteHeadDto();
            routeHeadDto.setId(head.getId());
            routeHeadDto.setTown(head.getTown());
            routeHeadDto.setCourseName(head.getCourseName());
            routeHeadDto.setTotDistance(head.getTotDistance());
            routeHeadDto.setContent(head.getContent());
            routeHeadDto.setTotlike(head.getTotlike());
            routeHeadDto.setCreateDate(head.getCreateDate());
            routeHeadDto.setUpdateDate(head.getUpdateDate());
            User routeUser = head.getUser();
            UserDto routeUserDto = new UserDto();
            routeUserDto.setId(routeUser.getId());
            routeUserDto.setProfilePicture(routeUser.getProfilePicture());
            routeUserDto.setName(routeUser.getName());
            routeHeadDto.setUserId(routeUserDto);
            routeHeadDto.setPublic(head.isPublic());
            RouteBookmark routeBookmark = routeBookmarkRepository.findByRouteHeadAndUser(head, user);
            RouteBookmarkDto routeBookmarkDto = new RouteBookmarkDto();
            if (routeBookmark != null) {
                routeBookmarkDto.setId(routeBookmark.getId());
            }
            routeHeadDto.setBookmark(routeBookmarkDto);

            RouteLike routeLike = routeLikeRepository.findByRouteHeadAndUser(head, user);
            RouteLikeDto routeLikeDto = new RouteLikeDto();
            if (routeLike != null) {
                routeLikeDto.setId(routeLike.getId());
            }
            routeHeadDto.setLike(routeLikeDto);

            List<String> hashtagList = new ArrayList<>();
            for (RouteHashtags routeHashtag : head.getRouteHashtags()) {
                hashtagList.add(routeHashtag.getHashtag().getTagName());
            }

            routeHeadDto.setTag(hashtagList);

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
            routeHeadDtoList.add(routeHeadDto);
        }

        return routeHeadDtoList;
    }

    @Transactional
    public void updateRouteHead(RouteHeadDto routeHeadDto) {
        RouteHead routeHead = routeHeadRepository.findById(routeHeadDto.getId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        routeHead.setCourseName(routeHeadDto.getCourseName());
        routeHead.setContent(routeHeadDto.getContent());
        routeHead.setPublic(true);

        List<String> tagList = routeHeadDto.getTag() != null ? routeHeadDto.getTag() : Collections.emptyList();

        List<String> existingTags = hashtagRepository.findExistingTags(tagList);

        List<String> nonDuplicateTags = tagList.stream()
                .filter(tag -> !existingTags.contains(tag))
                .collect(Collectors.toList());

        List<Hashtag> newHashtags = nonDuplicateTags.stream()
                .map(tag -> {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setTagName(tag);
                    return hashtag;
                }).collect(Collectors.toList());

        hashtagRepository.saveAll(newHashtags);

        List<Hashtag> allTags = hashtagRepository.findByTagNameIn(tagList);
        List<RouteHashtags> routeHashtags = allTags.stream()
                .map(hashtag -> {
                    RouteHashtags routeHashtag = new RouteHashtags();
                    routeHashtag.setRouteHead(routeHead);
                    routeHashtag.setHashtag(hashtag);
                    return routeHashtag;
                }).collect(Collectors.toList());

        routeHashtagsRepository.deleteByRouteHeadId(routeHead.getId());

        routeHashtagsRepository.saveAll(routeHashtags);

        routeHead.setRouteHashtags(routeHashtags);
        routeHeadRepository.save(routeHead);
    }

    @Transactional
    public void updateLike(RouteHeadDto routeHeadDto) {
        RouteHead routeHead = routeHeadRepository.findById(routeHeadDto.getId())
                .orElseThrow(() -> new RuntimeException("route not found"));

        routeHead.setTotlike(routeHeadDto.getTotlike());
        routeHeadRepository.save(routeHead);

    }

    @Transactional
    public void updateIsPublic(Long id, boolean isPublic) {
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("route not found"));
        routeHead.setPublic(isPublic);
        routeHeadRepository.save(routeHead);
    }

    @Transactional
    public List<RouteHeadDto> routeGetBookmark(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<RouteBookmark> bookmarkList = routeBookmarkRepository.findAllByUser(user);
        List<RouteHeadDto> headList = new ArrayList<>();
        for (RouteBookmark bookmark : bookmarkList) {
            RouteHead head = bookmark.getRouteHead();
            RouteHeadDto headDto = new RouteHeadDto();
            headDto.setId(head.getId());
            headDto.setTown(head.getTown());
            headDto.setCourseName(head.getCourseName());
            headDto.setTotDistance(head.getTotDistance());
            headDto.setContent(head.getContent());
            headDto.setTotlike(head.getTotlike());
            headDto.setCreateDate(head.getCreateDate());
            headDto.setUpdateDate(head.getUpdateDate());
            User routeUser = head.getUser();
            UserDto routeUserDto = new UserDto();
            routeUserDto.setId(routeUser.getId());
            routeUserDto.setProfilePicture(routeUser.getProfilePicture());
            routeUserDto.setName(routeUser.getName());
            headDto.setUserId(routeUserDto);
            headDto.setPublic(head.isPublic());
            RouteBookmark routeBookmark = routeBookmarkRepository.findByRouteHeadAndUser(head, user);
            RouteBookmarkDto routeBookmarkDto = new RouteBookmarkDto();

            if (routeBookmark != null) {
                routeBookmarkDto.setId(routeBookmark.getId());
            }

            headDto.setBookmark(routeBookmarkDto);

            List<String> hashtagList = new ArrayList<>();
            for (RouteHashtags routeHashtag : head.getRouteHashtags()) {
                hashtagList.add(routeHashtag.getHashtag().getTagName());
            }

            headDto.setTag(hashtagList);

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

            headDto.setData(dtoItemList);
            headList.add(headDto);
        }

        return headList;
    }

    @Transactional
    public void routePostBookmark(String email, Long id) {
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("route not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        RouteBookmark routebookmark = new RouteBookmark();
        routebookmark.setRouteHead(routeHead);
        routebookmark.setUser(user);
        routeBookmarkRepository.save(routebookmark);
    }

    @Transactional
    public void routeDeleteBookmark(String email, Long id) {
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("route not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        RouteBookmark routebookmark = routeBookmarkRepository.findByRouteHeadAndUser(routeHead, user);

        routeBookmarkRepository.delete(routebookmark);
    }

    @Transactional
    public List<RouteHeadDto> routeGetLike(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<RouteLike> likeList = routeLikeRepository.findAllByUser(user);
        List<RouteHeadDto> headList = new ArrayList<>();

        for (RouteLike like : likeList) {
            RouteHead head = like.getRouteHead();
            RouteHeadDto headDto = new RouteHeadDto();
            headDto.setId(head.getId());
            headDto.setTown(head.getTown());
            headDto.setCourseName(head.getCourseName());
            headDto.setTotDistance(head.getTotDistance());
            headDto.setContent(head.getContent());
            headDto.setTotlike(head.getTotlike());
            headDto.setCreateDate(head.getCreateDate());
            headDto.setUpdateDate(head.getUpdateDate());

            // Route 작성자 정보 설정
            User routeUser = head.getUser();
            UserDto routeUserDto = new UserDto();
            routeUserDto.setId(routeUser.getId());
            routeUserDto.setProfilePicture(routeUser.getProfilePicture());
            routeUserDto.setName(routeUser.getName());
            headDto.setUserId(routeUserDto);
            headDto.setPublic(head.isPublic());

            // 좋아요 상태 설정
            RouteLike routeLike = routeLikeRepository.findByRouteHeadAndUser(head, user);
            RouteLikeDto routeLikeDto = new RouteLikeDto();

            if (routeLike != null) {
                routeLikeDto.setId(routeLike.getId());
            }

            headDto.setLike(routeLikeDto);

            // 해시태그 리스트 설정
            List<String> hashtagList = new ArrayList<>();
            for (RouteHashtags routeHashtag : head.getRouteHashtags()) {
                hashtagList.add(routeHashtag.getHashtag().getTagName());
            }

            headDto.setTag(hashtagList);

            // 경로 아이템 설정
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

            headDto.setData(dtoItemList);
            headList.add(headDto);
        }

        return headList;
    }

    @Transactional
    public void routePostLike(String email, Long id) {
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RouteLike routeLike = new RouteLike();
        routeLike.setRouteHead(routeHead);
        routeLike.setUser(user);
        routeLikeRepository.save(routeLike);
    }

    @Transactional
    public void routeDeleteLike(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        RouteLike routeLike = routeLikeRepository.findByRouteHeadAndUser(routeHead, user);

        routeLikeRepository.delete(routeLike);
    }

    @Transactional
    public void routePutLike(String email, long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        routeHead.setTotlike(routeHead.getTotlike() - 1);
        routeHeadRepository.save(routeHead);
    }

    @Transactional
    public void routePutLikee(String email, long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        routeHead.setTotlike(routeHead.getTotlike() + 1);
        routeHeadRepository.save(routeHead);
    }

    @Transactional
    public RouteCommentDto routePostComment(String email, Long id, String comment) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RouteHead routeHead = routeHeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        RouteComment routeComment = new RouteComment();
        routeComment.setUser(user);
        routeComment.setRouteHead(routeHead);
        routeComment.setComment(comment);
        RouteComment saveData = routeCommentRepository.save(routeComment);
        RouteCommentDto routeCommentDto = new RouteCommentDto();
        routeCommentDto.setId(saveData.getId());
        routeCommentDto.setComment(saveData.getComment());
        routeCommentDto.setCreateDate(saveData.getCreateDate());
        User saveUser = saveData.getUser();
        UserDto saveUserDto = new UserDto();
        saveUserDto.setId(saveUser.getId());
        saveUserDto.setName(saveUser.getName());
        saveUserDto.setProfilePicture(saveUser.getProfilePicture());
        routeCommentDto.setUser(saveUserDto);
        return routeCommentDto;
    }

    @Transactional
    public void routeDeleteComment(Long id) {
        routeCommentRepository.deleteById(id);
    }
}
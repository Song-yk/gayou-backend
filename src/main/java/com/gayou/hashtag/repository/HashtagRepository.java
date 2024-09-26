package com.gayou.hashtag.repository;

import com.gayou.hashtag.model.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("SELECT h.tagName FROM Hashtag h WHERE h.tagName IN :tagNames")
    List<String> findExistingTags(@Param("tagNames") List<String> tagNames);

    List<Hashtag> findByTagNameIn(List<String> tagNames);
}

package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.musicplayer.entity.UserHistorySong;

@Repository
public interface UserHistorySongRepository extends JpaRepository<UserHistorySong, Long> {
    List<UserHistorySong> findByUserIdAndDurationListenedGreaterThanOrderByListenedAtDesc(Long userId, Integer duration);
}
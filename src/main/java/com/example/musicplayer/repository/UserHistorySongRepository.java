package com.example.musicplayer.repository;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.entity.UserHistorySong;

@Repository
public interface UserHistorySongRepository extends JpaRepository<UserHistorySong, Long> {
    List<UserHistorySong> findByUserIdOrderByListenedAtDesc(Long userId);

    @Query("SELECT h.song FROM UserHistorySong h " +
            "WHERE h.userId = :userId " +
            "AND h.listenedAt >= :since " +
            "GROUP BY h.song " +
            "ORDER BY COUNT(h) DESC")
    List<Song> findTopSongsThisMonth(@Param("userId") Long userId, @Param("since") LocalDateTime since,
            Pageable pageable);
}
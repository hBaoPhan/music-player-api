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

    @Query("SELECT h FROM UserHistorySong h " +
            "WHERE h.user.id = :userId " +
            "AND h.listenedAt = (" +
            "   SELECT MAX(h2.listenedAt) FROM UserHistorySong h2 " +
            "   WHERE h2.user.id = h.user.id " +
            "   AND h2.song = h.song " +
            "   AND FUNCTION('date', h2.listenedAt) = FUNCTION('date', h.listenedAt)" +
            ") " +
            "ORDER BY h.listenedAt DESC")
    List<UserHistorySong> findUniqueDailyHistoryByUserId(@Param("userId") Long userId);

    @Query("SELECT h.song FROM UserHistorySong h " +
            "WHERE h.user.id = :userId " +
            "AND h.listenedAt >= :since " +
            "GROUP BY h.song " +
            "ORDER BY COUNT(h) DESC")
    List<Song> findTopSongsThisMonth(@Param("userId") Long userId, @Param("since") LocalDateTime since,
            Pageable pageable);
}
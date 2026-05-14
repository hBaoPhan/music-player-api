package com.example.musicplayer.repository;

import java.time.LocalDateTime;
import java.util.List;

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

    // Dashboard: tổng số lượt nghe toàn hệ thống
    long count();

    // Dashboard: top 10 bài hát được nghe nhiều nhất trong 7 ngày qua
    @Query("SELECT h.song.id, h.song.title, h.song.artist.name, h.song.album.coverUrl, COUNT(h) as plays " +
           "FROM UserHistorySong h " +
           "WHERE h.listenedAt >= :since " +
           "GROUP BY h.song.id, h.song.title, h.song.artist.name, h.song.album.coverUrl " +
           "ORDER BY plays DESC")
    List<Object[]> findTop10TrendingSongsSince(@Param("since") LocalDateTime since, Pageable pageable);

    // Dashboard: lượt nghe mỗi ngày trong 7 ngày gần nhất
    @Query("SELECT FUNCTION('DATE', h.listenedAt) as day, COUNT(h) as cnt " +
           "FROM UserHistorySong h " +
           "WHERE h.listenedAt >= :since " +
           "GROUP BY FUNCTION('DATE', h.listenedAt) " +
           "ORDER BY day ASC")
    List<Object[]> countStreamsByDaySince(@Param("since") LocalDateTime since);

    // Charts: top trending artists by play count (last 7 days)
    @Query("SELECT h.song.artist.id, h.song.artist.name, h.song.artist.avatarUrl, COUNT(h) as plays " +
           "FROM UserHistorySong h " +
           "WHERE h.listenedAt >= :since AND h.song.active = true AND h.song.artist.active = true " +
           "GROUP BY h.song.artist.id, h.song.artist.name, h.song.artist.avatarUrl " +
           "ORDER BY plays DESC")
    List<Object[]> findTopTrendingArtistsSince(@Param("since") LocalDateTime since, Pageable pageable);
}
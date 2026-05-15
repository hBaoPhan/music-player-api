package com.example.musicplayer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.musicplayer.entity.UserFavorite;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    @Query("SELECT f FROM UserFavorite f WHERE f.user.id = :userId ORDER BY f.addedAt DESC")
    List<UserFavorite> findByUserIdOrderByAddedAtDesc(@org.springframework.data.repository.query.Param("userId") Long userId);

    Optional<UserFavorite> findByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserId(Long userId);

    // Dashboard: top 10 bài hát được yêu thích nhiều nhất
    @Query("SELECT f.song.id, f.song.title, f.song.artist.name, f.song.album.coverUrl, COUNT(f) as favs, f.song.audioUrl " +
            "FROM UserFavorite f " +
            "GROUP BY f.song.id, f.song.title, f.song.artist.name, f.song.album.coverUrl, f.song.audioUrl " +
            "ORDER BY favs DESC")
    List<Object[]> findTop10FavoriteSongs(Pageable pageable);
}

package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Soft delete queries
    List<Album> findAllByActiveTrue();

    List<Album> findByArtistIdAndActiveTrue(Long artistId);
}

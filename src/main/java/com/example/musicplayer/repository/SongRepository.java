package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistId(Long artistId);
    List<Song> findByAlbumId(Long albumId);

    List<Song> findByTitle(String title);
}

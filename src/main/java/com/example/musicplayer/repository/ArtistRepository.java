package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByName(String name);

    // Soft delete queries
    List<Artist> findAllByActiveTrue();
}

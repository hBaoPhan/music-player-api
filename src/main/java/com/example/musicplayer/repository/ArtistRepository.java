package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.service.ArtistService;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    // Soft delete: chỉ trả về artist đang active
    java.util.List<Artist> findAllByActiveTrue();

    Artist findByName(String name);

    // Dashboard: tổng số artist active
    long countByActiveTrue();
}

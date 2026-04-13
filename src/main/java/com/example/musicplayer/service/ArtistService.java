package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.repository.ArtistRepository;
import com.example.musicplayer.repository.SongRepository;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;
    
    public List<Artist> getAllArtists() {
        return artistRepository.findAllByIsActiveTrue();
    }
    
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }
    
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }
    
    public Optional<Artist> updateArtist(Long id, Artist artistDetails) {
        return artistRepository.findById(id)
                .map(artist -> {
                    artist.setName(artistDetails.getName());
                    artist.setBio(artistDetails.getBio());
                    artist.setAvatarUrl(artistDetails.getAvatarUrl());
                    if (artistDetails.getAlbums() != null) {
                        artist.setAlbums(artistDetails.getAlbums());
                    }
                    if (artistDetails.getSongs() != null) {
                        artist.setSongs(artistDetails.getSongs());
                    }
                    return artistRepository.save(artist);
                });
    }
    
    @Transactional
    public boolean deleteArtist(Long id) {
        return artistRepository.findById(id)
                .map(artist -> {
                    // Cascade: deactivate tất cả song của artist
                    songRepository.deactivateByArtistId(id);
                    artist.setActive(false);
                    artistRepository.save(artist);
                    return true;
                })
                .orElse(false);
    }
    
    public Artist findByName(String name) {
        return artistRepository.findByName(name);
    }
}

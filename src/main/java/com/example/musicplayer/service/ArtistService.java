package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.repository.ArtistRepository;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
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
    
    public boolean deleteArtist(Long id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Artist findByName(String name) {
        return artistRepository.findByName(name);
    }
}

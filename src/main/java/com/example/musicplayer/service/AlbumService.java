package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Album;
import com.example.musicplayer.repository.AlbumRepository;
import com.example.musicplayer.repository.SongRepository;

@Service
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;
    
    public List<Album> getAllAlbums() {
        return albumRepository.findAllByIsActiveTrue();
    }
    
    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }
    
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public Optional<Album> updateAlbum(Long id, Album albumDetails) {
        return albumRepository.findById(id)
                .map(album -> {
                    album.setTitle(albumDetails.getTitle());
                    album.setArtist(albumDetails.getArtist());
                    if (albumDetails.getArtist() != null) {
                        album.setArtistId(albumDetails.getArtist().getId());
                    }
                    album.setCoverUrl(albumDetails.getCoverUrl());
                    album.setReleaseDate(albumDetails.getReleaseDate());
                    if (albumDetails.getSongs() != null) {
                        album.setSongs(albumDetails.getSongs());
                    }
                    return albumRepository.save(album);
                });
    }
    
    @Transactional
    public boolean deleteAlbum(Long id) {
        return albumRepository.findById(id)
                .map(album -> {
                    // Cascade: deactivate tất cả song thuộc album
                    songRepository.deactivateByAlbumId(id);
                    album.setActive(false);
                    albumRepository.save(album);
                    return true;
                })
                .orElse(false);
    }
    
    public List<Album> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistIdAndIsActiveTrue(artistId);
    }
}

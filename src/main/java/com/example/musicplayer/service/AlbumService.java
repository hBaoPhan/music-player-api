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
        return albumRepository.findAllByActiveTrue();
    }

    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public Album createAlbum(Album album) {
        if (album.getArtistId() == null && album.getArtist() != null) {
            album.setArtistId(album.getArtist().getId());
        }
        return albumRepository.save(album);
    }

    public Optional<Album> updateAlbum(Long id, Album albumDetails) {
        return albumRepository.findById(id)
                .map(album -> {
                    if (albumDetails.getTitle() != null && !albumDetails.getTitle().isBlank()) {
                        album.setTitle(albumDetails.getTitle());
                    }
                    Long newArtistId = albumDetails.getArtistId();
                    if (newArtistId == null && albumDetails.getArtist() != null) {
                        newArtistId = albumDetails.getArtist().getId();
                    }
                    if (newArtistId != null) {
                        album.setArtistId(newArtistId);
                    }
                    if (albumDetails.getCoverUrl() != null) {
                        album.setCoverUrl(albumDetails.getCoverUrl());
                    }
                    if (albumDetails.getReleaseDate() != null) {
                        album.setReleaseDate(albumDetails.getReleaseDate());
                    }
                    if (albumDetails.getType() != null) {
                        album.setType(albumDetails.getType());
                    }
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

                    songRepository.deactivateByAlbumId(id);
                    album.setActive(false);
                    albumRepository.save(album);
                    return true;
                })
                .orElse(false);
    }

    public List<Album> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistIdAndActiveTrue(artistId);
    }
}

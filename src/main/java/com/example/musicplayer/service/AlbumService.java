package com.example.musicplayer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Album;
import com.example.musicplayer.dto.AlbumDTO;
import com.example.musicplayer.repository.AlbumRepository;
import com.example.musicplayer.repository.SongRepository;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Cacheable(value = "albumsList")
    public List<AlbumDTO> getAllAlbums() {
        return albumRepository.findAllByActiveTrue().stream()
                .map(AlbumDTO::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "album", key = "#id", unless = "#result == null")
    public AlbumDTO getAlbumById(Long id) {
        return albumRepository.findById(id).map(AlbumDTO::new).orElse(null);
    }

    @CacheEvict(value = {"albumsList", "artistAlbums"}, allEntries = true)
    public AlbumDTO createAlbum(Album album) {
        if (album.getArtistId() == null && album.getArtist() != null) {
            album.setArtistId(album.getArtist().getId());
        }
        return new AlbumDTO(albumRepository.save(album));
    }

    @CacheEvict(value = {"albumsList", "artistAlbums"}, allEntries = true)
    @CachePut(value = "album", key = "#id", unless = "#result == null")
    public AlbumDTO updateAlbum(Long id, Album albumDetails) {
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
                    return new AlbumDTO(albumRepository.save(album));
                })
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = {"album", "albumsList", "artistAlbums"}, allEntries = true)
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

    @Cacheable(value = "artistAlbums", key = "#artistId")
    public List<AlbumDTO> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistIdAndActiveTrue(artistId).stream()
                .map(AlbumDTO::new)
                .collect(Collectors.toList());
    }
}

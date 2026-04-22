package com.example.musicplayer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.repository.SongRepository;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;
    
    @Cacheable(value = "songsList")
    public List<SongDTO> getAllSongs() {
        return songRepository.findAllByActiveTrue().stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = "song", key = "#id", unless = "#result == null")
    public SongDTO getSongById(Long id) {
        return songRepository.findById(id).map(SongDTO::new).orElse(null);
    }
    
    @CacheEvict(value = {"songsList", "artistSongs", "albumSongs"}, allEntries = true)
    public SongDTO createSong(Song song) {
        Song savedSong = songRepository.save(song);
        return new SongDTO(savedSong);
    }
    
    @CacheEvict(value = {"songsList", "artistSongs", "albumSongs"}, allEntries = true)
    @CachePut(value = "song", key = "#id", unless = "#result == null")
    public SongDTO updateSong(Long id, Song songDetails) {
        return songRepository.findById(id)
                .map(song -> {
                    song.setTitle(songDetails.getTitle());
                    song.setArtist(songDetails.getArtist());
                    song.setAlbum(songDetails.getAlbum());
                    song.setAudioUrl(songDetails.getAudioUrl());
                    song.setDuration(songDetails.getDuration());
                    song.setPlayCount(songDetails.getPlayCount());
                    song.setGenre(songDetails.getGenre());
                    return new SongDTO(songRepository.save(song));
                })
                .orElse(null);
    }
    
    @CacheEvict(value = {"song", "songsList", "artistSongs", "albumSongs"}, allEntries = true)
    public boolean deleteSong(Long id) {
        return songRepository.findById(id)
                .map(song -> {
                    song.setActive(false);
                    songRepository.save(song);
                    return true;
                })
                .orElse(false);
    }
    
    @Cacheable(value = "artistSongs", key = "#artistId")
    public List<SongDTO> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistIdAndActiveTrue(artistId).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "albumSongs", key = "#albumId")
    public List<SongDTO> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumIdAndActiveTrue(albumId).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsByTitle(String title) {
        return songRepository.findByTitleAndActiveTrue(title).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }
}

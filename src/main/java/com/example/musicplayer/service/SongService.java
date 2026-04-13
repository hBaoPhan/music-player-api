package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.repository.SongRepository;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;
    
    public List<Song> getAllSongs() {
        return songRepository.findAllByIsActiveTrue();
    }
    
    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }
    
    public Song createSong(Song song) {
        return songRepository.save(song);
    }
    
    public Optional<Song> updateSong(Long id, Song songDetails) {
        return songRepository.findById(id)
                .map(song -> {
                    song.setTitle(songDetails.getTitle());
                    song.setArtist(songDetails.getArtist());
                    song.setAlbum(songDetails.getAlbum());
                    song.setAudioUrl(songDetails.getAudioUrl());
                    song.setDuration(songDetails.getDuration());
                    song.setPlayCount(songDetails.getPlayCount());
                    song.setGenre(songDetails.getGenre());
                    return songRepository.save(song);
                });
    }
    
    public boolean deleteSong(Long id) {
        return songRepository.findById(id)
                .map(song -> {
                    song.setActive(false);
                    songRepository.save(song);
                    return true;
                })
                .orElse(false);
    }
    
    public List<Song> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistIdAndIsActiveTrue(artistId);
    }

    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumIdAndIsActiveTrue(albumId);
    }

    public List<Song> getSongsByTitle(String title) {
        return songRepository.findByTitleAndIsActiveTrue(title);
    }
}

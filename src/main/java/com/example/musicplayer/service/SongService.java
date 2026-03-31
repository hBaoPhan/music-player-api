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
        return songRepository.findAll();
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
                    song.setCategory(songDetails.getCategory());
                    return songRepository.save(song);
                });
    }
    
    public boolean deleteSong(Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Song> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistId(artistId);
    }
    
    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }
    
    public List<Song> getSongsByTitle(String title) {
        return songRepository.findByTitle(title);
    }
}

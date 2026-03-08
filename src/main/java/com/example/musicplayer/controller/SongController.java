package com.example.musicplayer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.repository.SongRepository;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    
    @Autowired
    private SongRepository songRepository;
    
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return songRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song savedSong = songRepository.save(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songRepository.findById(id)
                .map(song -> {
                    song.setTitle(songDetails.getTitle());
                    song.setArtistId(songDetails.getArtistId());
                    song.setAlbumId(songDetails.getAlbumId());
                    song.setAudioUrl(songDetails.getAudioUrl());
                    song.setDuration(songDetails.getDuration());
                    song.setPlayCount(songDetails.getPlayCount());
                    return ResponseEntity.ok(songRepository.save(song));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/artist/{artistId}")
    public List<Song> getSongsByArtist(@PathVariable Long artistId) {
        return songRepository.findByArtistId(artistId);
    }
    
    @GetMapping("/album/{albumId}")
    public List<Song> getSongsByAlbum(@PathVariable Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }
    
    @GetMapping("/title/{title}")
    public List<Song> getSongsByTitle(@PathVariable String title) {
        return songRepository.findByTitle(title);
    }
}

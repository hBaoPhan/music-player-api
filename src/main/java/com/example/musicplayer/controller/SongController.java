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
import com.example.musicplayer.service.SongService;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    
    @Autowired
    private SongService songService;
    
    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return songService.getSongById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song savedSong = songService.createSong(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songService.updateSong(id, songDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (songService.deleteSong(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/artist/{artistId}")
    public List<Song> getSongsByArtist(@PathVariable Long artistId) {
        return songService.getSongsByArtist(artistId);
    }
    
    @GetMapping("/album/{albumId}")
    public List<Song> getSongsByAlbum(@PathVariable Long albumId) {
        return songService.getSongsByAlbum(albumId);
    }
    
    @GetMapping("/title/{title}")
    public List<Song> getSongsByTitle(@PathVariable String title) {
        return songService.getSongsByTitle(title);
    }
}

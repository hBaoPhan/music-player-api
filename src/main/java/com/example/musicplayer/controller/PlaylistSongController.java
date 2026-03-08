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

import com.example.musicplayer.entity.PlaylistSong;
import com.example.musicplayer.repository.PlaylistSongRepository;

@RestController
@RequestMapping("/api/playlist-songs")
public class PlaylistSongController {
    
    @Autowired
    private PlaylistSongRepository playlistSongRepository;
    
    @GetMapping
    public List<PlaylistSong> getAllPlaylistSongs() {
        return playlistSongRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistSong> getPlaylistSongById(@PathVariable Long id) {
        return playlistSongRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PlaylistSong> createPlaylistSong(@RequestBody PlaylistSong playlistSong) {
        PlaylistSong savedPlaylistSong = playlistSongRepository.save(playlistSong);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaylistSong);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistSong> updatePlaylistSong(@PathVariable Long id, @RequestBody PlaylistSong playlistSongDetails) {
        return playlistSongRepository.findById(id)
                .map(playlistSong -> {
                    playlistSong.setPlaylistId(playlistSongDetails.getPlaylistId());
                    playlistSong.setSongId(playlistSongDetails.getSongId());
                    playlistSong.setAddedAt(playlistSongDetails.getAddedAt());
                    return ResponseEntity.ok(playlistSongRepository.save(playlistSong));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylistSong(@PathVariable Long id) {
        if (playlistSongRepository.existsById(id)) {
            playlistSongRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/playlist/{playlistId}")
    public List<PlaylistSong> getPlaylistSongsByPlaylist(@PathVariable Long playlistId) {
        return playlistSongRepository.findByPlaylistId(playlistId);
    }
    
    @DeleteMapping("/playlist/{playlistId}/song/{songId}")
    public ResponseEntity<Void> deletePlaylistSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistSongRepository.deleteByPlaylistIdAndSongId(playlistId, songId);
        return ResponseEntity.noContent().build();
    }
}

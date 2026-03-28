package com.example.musicplayer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musicplayer.entity.PlaylistSong;
import com.example.musicplayer.service.PlaylistSongService;

@RestController
@RequestMapping("/api/playlist-songs")
@CrossOrigin(origins = "http://localhost:5173")
public class PlaylistSongController {

    @Autowired
    private PlaylistSongService playlistSongService;

    @GetMapping
    public List<PlaylistSong> getAllPlaylistSongs() {
        return playlistSongService.getAllPlaylistSongs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistSong> getPlaylistSongById(@PathVariable Long id) {
        return playlistSongService.getPlaylistSongById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlaylistSong> createPlaylistSong(@RequestBody PlaylistSong playlistSong) {
        PlaylistSong savedPlaylistSong = playlistSongService.createPlaylistSong(playlistSong);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaylistSong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistSong> updatePlaylistSong(@PathVariable Long id,
            @RequestBody PlaylistSong playlistSongDetails) {
        return playlistSongService.updatePlaylistSong(id, playlistSongDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylistSong(@PathVariable Long id) {
        if (playlistSongService.deletePlaylistSong(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/playlist/{playlistId}")
    public List<PlaylistSong> getPlaylistSongsByPlaylist(@PathVariable Long playlistId) {
        return playlistSongService.getPlaylistSongsByPlaylist(playlistId);
    }

    @DeleteMapping("/playlist/{playlistId}/song/{songId}")
    public ResponseEntity<Void> deletePlaylistSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistSongService.deletePlaylistSongByPlaylistAndSong(playlistId, songId);
        return ResponseEntity.noContent().build();
    }
}

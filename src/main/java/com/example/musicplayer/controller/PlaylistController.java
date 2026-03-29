package com.example.musicplayer.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.example.musicplayer.dto.PlaylistDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.entity.Playlist;
import com.example.musicplayer.service.PlaylistService;

@RestController
@RequestMapping("/api/playlists")

public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistService.getAllPlaylists().stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id)
                .map(PlaylistDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestBody Playlist playlist) {
        Playlist savedPlaylist = playlistService.createPlaylist(playlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PlaylistDTO(savedPlaylist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlistDetails) {
        return playlistService.updatePlaylist(id, playlistDetails)
                .map(PlaylistDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        if (playlistService.deletePlaylist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public List<PlaylistDTO> getPlaylistsByUser(@PathVariable Long userId) {
        return playlistService.getPlaylistsByUser(userId).stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getPlaylistSongs(@PathVariable Long id) {
        return playlistService.getPlaylistById(id)
                .map(playlist -> ResponseEntity.ok(
                        playlist.getSongs().stream()
                                .map(SongDTO::new)
                                .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<PlaylistDTO> addSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(new PlaylistDTO(updatedPlaylist));
    }
}

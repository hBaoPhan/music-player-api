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
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.musicplayer.dto.PlaylistDTO;
import com.example.musicplayer.dto.PlaylistRequestDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.service.PlaylistService;
import com.example.musicplayer.service.PlaylistSongService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/playlists")

public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistSongService playlistSongService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id) {
        PlaylistDTO playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistRequestDTO dto) {
        PlaylistDTO savedPlaylist = playlistService.createPlaylist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaylist);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @playlistService.isOwner(#id, principal.user.id)")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable Long id,
            @Valid @RequestBody PlaylistRequestDTO dto) {
        PlaylistDTO updatedPlaylist = playlistService.updatePlaylist(id, dto);
        if (updatedPlaylist != null) {
            return ResponseEntity.ok(updatedPlaylist);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @playlistService.isOwner(#id, principal.user.id)")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        if (playlistService.deletePlaylist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public List<PlaylistDTO> getPlaylistsByUser(@PathVariable Long userId) {
        return playlistService.getPlaylistsByUser(userId);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getPlaylistSongs(@PathVariable Long id) {
        PlaylistDTO playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            return ResponseEntity.ok(playlist.getSongs());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    @PreAuthorize("hasRole('ADMIN') or @playlistService.isOwner(#playlistId, principal.user.id)")
    public ResponseEntity<PlaylistDTO> addSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        PlaylistDTO updatedPlaylist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    @PreAuthorize("hasRole('ADMIN') or @playlistService.isOwner(#playlistId, principal.user.id)")
    public ResponseEntity<Void> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        if (playlistSongService.deleteSongFromPlayListSongByPlayListIdAndSongId(playlistId, songId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

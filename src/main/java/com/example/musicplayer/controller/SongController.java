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

import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.service.SongService;

@RestController
@RequestMapping("/api/songs")

public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping
    public List<SongDTO> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long id) {
        SongDTO song = songService.getSongById(id);
        if (song != null) {
            return ResponseEntity.ok(song);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SongDTO> createSong(@RequestBody Song song) {
        SongDTO savedSong = songService.createSong(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SongDTO> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        SongDTO updatedSong = songService.updateSong(id, songDetails);
        if (updatedSong != null) {
            return ResponseEntity.ok(updatedSong);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (songService.deleteSong(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/artist/{artistId}")
    public List<SongDTO> getSongsByArtist(@PathVariable Long artistId) {
        return songService.getSongsByArtist(artistId);
    }

    @GetMapping("/album/{albumId}")
    public List<SongDTO> getSongsByAlbum(@PathVariable Long albumId) {
        return songService.getSongsByAlbum(albumId);
    }

    @GetMapping("/title/{title}")
    public List<SongDTO> getSongsByTitle(@PathVariable String title) {
        return songService.getSongsByTitle(title);
    }
}

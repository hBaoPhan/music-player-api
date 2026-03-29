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
        return songService.getAllSongs().stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long id) {
        return songService.getSongById(id)
                .map(SongDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SongDTO> createSong(@RequestBody Song song) {
        Song savedSong = songService.createSong(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SongDTO(savedSong));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongDTO> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songService.updateSong(id, songDetails)
                .map(SongDTO::new)
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
    public List<SongDTO> getSongsByArtist(@PathVariable Long artistId) {
        return songService.getSongsByArtist(artistId).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/album/{albumId}")
    public List<SongDTO> getSongsByAlbum(@PathVariable Long albumId) {
        return songService.getSongsByAlbum(albumId).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/title/{title}")
    public List<SongDTO> getSongsByTitle(@PathVariable String title) {
        return songService.getSongsByTitle(title).stream()
                .map(SongDTO::new)
                .collect(Collectors.toList());
    }
}

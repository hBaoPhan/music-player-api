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
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.musicplayer.dto.AlbumDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.entity.Album;
import com.example.musicplayer.service.AlbumService;
import com.example.musicplayer.service.SongService;

@RestController
@RequestMapping("/api/albums")

public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @GetMapping
    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        AlbumDTO album = albumService.getAlbumById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody Album album) {
        AlbumDTO savedAlbum = albumService.createAlbum(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Long id, @RequestBody Album albumDetails) {
        AlbumDTO updatedAlbum = albumService.updateAlbum(id, albumDetails);
        if (updatedAlbum != null) {
            return ResponseEntity.ok(updatedAlbum);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        if (albumService.deleteAlbum(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/artist/{artistId}")
    public List<AlbumDTO> getAlbumsByArtist(@PathVariable Long artistId) {
        return albumService.getAlbumsByArtist(artistId);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getAlbumSongs(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongsByAlbum(id));
    }
}

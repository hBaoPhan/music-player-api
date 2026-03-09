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

import com.example.musicplayer.entity.Album;
import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.service.AlbumService;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @GetMapping
    public List<Album> getAllAlbums() {
        return albumService.getAllAlbums();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album savedAlbum = albumService.createAlbum(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album albumDetails) {
        return albumService.updateAlbum(id, albumDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        if (albumService.deleteAlbum(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/artist/{artistId}")
    public List<Album> getAlbumsByArtist(@PathVariable Long artistId) {
        return albumService.getAlbumsByArtist(artistId);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getAlbumSongs(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(album -> ResponseEntity.ok(album.getSongs()))
                .orElse(ResponseEntity.notFound().build());
    }
}

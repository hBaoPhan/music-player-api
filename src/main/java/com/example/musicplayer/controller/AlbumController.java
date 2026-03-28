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

import com.example.musicplayer.entity.Album;
import com.example.musicplayer.dto.AlbumDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.service.AlbumService;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @GetMapping
    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums().stream()
                .map(AlbumDTO::new)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(AlbumDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody Album album) {
        Album savedAlbum = albumService.createAlbum(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AlbumDTO(savedAlbum));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Long id, @RequestBody Album albumDetails) {
        return albumService.updateAlbum(id, albumDetails)
                .map(AlbumDTO::new)
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
    public List<AlbumDTO> getAlbumsByArtist(@PathVariable Long artistId) {
        return albumService.getAlbumsByArtist(artistId).stream()
                .map(AlbumDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getAlbumSongs(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(album -> ResponseEntity.ok(
                        album.getSongs().stream()
                             .map(SongDTO::new)
                             .collect(Collectors.toList())
                ))
                .orElse(ResponseEntity.notFound().build());
    }
}

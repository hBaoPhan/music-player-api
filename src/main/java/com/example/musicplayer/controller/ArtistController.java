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
import com.example.musicplayer.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    
    @Autowired
    private ArtistService artistService;
    
    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist savedArtist = artistService.createArtist(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artistDetails) {
        return artistService.updateArtist(id, artistDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (artistService.deleteArtist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Artist> getArtistByName(@PathVariable String name) {
        Artist artist = artistService.findByName(name);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getArtistAlbums(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> ResponseEntity.ok(artist.getAlbums()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getArtistSongs(@PathVariable Long id) {
        return artistService.getArtistById(id)
                .map(artist -> ResponseEntity.ok(artist.getSongs()))
                .orElse(ResponseEntity.notFound().build());
    }
}

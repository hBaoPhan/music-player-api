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

import com.example.musicplayer.dto.AlbumDTO;
import com.example.musicplayer.dto.ArtistDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.dto.TrendingArtistDTO;
import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.service.AlbumService;
import com.example.musicplayer.service.ArtistService;
import com.example.musicplayer.service.SongService;

@RestController
@RequestMapping("/api/artists")

public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @GetMapping
    public List<ArtistDTO> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        ArtistDTO artist = artistService.getArtistById(id);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody Artist artist) {
        ArtistDTO savedArtist = artistService.createArtist(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id, @RequestBody Artist artistDetails) {
        ArtistDTO updatedArtist = artistService.updateArtist(id, artistDetails);
        if (updatedArtist != null) {
            return ResponseEntity.ok(updatedArtist);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (artistService.deleteArtist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ArtistDTO> getArtistByName(@PathVariable String name) {
        ArtistDTO artist = artistService.findByName(name);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<List<AlbumDTO>> getArtistAlbums(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumsByArtist(id));
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getArtistSongs(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongsByArtist(id));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<TrendingArtistDTO>> getTrendingArtists(
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(artistService.getTopTrendingArtists(limit));
    }
}

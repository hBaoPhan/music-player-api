package com.example.musicplayer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.dto.ArtistDTO;
import com.example.musicplayer.dto.TrendingArtistDTO;
import com.example.musicplayer.repository.ArtistRepository;
import com.example.musicplayer.repository.SongRepository;
import com.example.musicplayer.repository.UserHistorySongRepository;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserHistorySongRepository historyRepository;
    
    @Cacheable(value = "artistsList")
    public List<ArtistDTO> getAllArtists() {
        return artistRepository.findAllByActiveTrue().stream()
                .map(ArtistDTO::new)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = "artist", key = "#id", unless = "#result == null")
    public ArtistDTO getArtistById(Long id) {
        return artistRepository.findById(id).map(ArtistDTO::new).orElse(null);
    }
    
    @CacheEvict(value = "artistsList", allEntries = true)
    public ArtistDTO createArtist(Artist artist) {
        return new ArtistDTO(artistRepository.save(artist));
    }
    
    @CacheEvict(value = "artistsList", allEntries = true)
    @CachePut(value = "artist", key = "#id", unless = "#result == null")
    public ArtistDTO updateArtist(Long id, Artist artistDetails) {
        return artistRepository.findById(id)
                .map(artist -> {
                    artist.setName(artistDetails.getName());
                    artist.setBio(artistDetails.getBio());
                    artist.setAvatarUrl(artistDetails.getAvatarUrl());
                    if (artistDetails.getAlbums() != null) {
                        artist.setAlbums(artistDetails.getAlbums());
                    }
                    if (artistDetails.getSongs() != null) {
                        artist.setSongs(artistDetails.getSongs());
                    }
                    return new ArtistDTO(artistRepository.save(artist));
                })
                .orElse(null);
    }
    
    @Transactional
    @CacheEvict(value = {"artist", "artistsList"}, allEntries = true)
    public boolean deleteArtist(Long id) {
        return artistRepository.findById(id)
                .map(artist -> {
                    // Cascade: deactivate tất cả song của artist
                    songRepository.deactivateByArtistId(id);
                    artist.setActive(false);
                    artistRepository.save(artist);
                    return true;
                })
                .orElse(false);
    }
    
    public ArtistDTO findByName(String name) {
        Artist artist = artistRepository.findByName(name);
        return artist != null ? new ArtistDTO(artist) : null;
    }

    public List<TrendingArtistDTO> getTopTrendingArtists(int limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        List<Object[]> rows = historyRepository.findTopTrendingArtistsSince(since, PageRequest.of(0, limit));
        return rows.stream()
                .map(r -> new TrendingArtistDTO(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        (String) r[2],
                        ((Number) r[3]).longValue()
                ))
                .collect(Collectors.toList());
    }
}

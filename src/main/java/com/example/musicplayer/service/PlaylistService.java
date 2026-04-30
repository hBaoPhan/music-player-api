package com.example.musicplayer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.musicplayer.dto.PlaylistDTO;
import com.example.musicplayer.dto.PlaylistRequestDTO;
import com.example.musicplayer.entity.CustomUserDetails;
import com.example.musicplayer.entity.Playlist;
import com.example.musicplayer.entity.PlaylistSong;
import com.example.musicplayer.repository.PlaylistRepository;
import com.example.musicplayer.repository.PlaylistSongRepository;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.repository.SongRepository;
import com.example.musicplayer.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Cacheable(value = "playlistsList")
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "playlist", key = "#id", unless = "#result == null")
    public PlaylistDTO getPlaylistById(Long id) {
        return playlistRepository.findById(id).map(PlaylistDTO::new).orElse(null);
    }

    @CacheEvict(value = { "playlistsList", "userPlaylists" }, allEntries = true)
    public PlaylistDTO createPlaylist(PlaylistRequestDTO dto) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User userRef = userRepository.getReferenceById(userId);
        Playlist playlist = new Playlist(dto.getName(), userRef, LocalDateTime.now());
        return new PlaylistDTO(playlistRepository.save(playlist));
    }

    @CacheEvict(value = { "playlistsList", "userPlaylists" }, allEntries = true)
    @CachePut(value = "playlist", key = "#id", unless = "#result == null")
    public PlaylistDTO updatePlaylist(Long id, PlaylistRequestDTO dto) {
        return playlistRepository.findById(id)
                .map(playlist -> {
                    playlist.setName(dto.getName());
                    return new PlaylistDTO(playlistRepository.save(playlist));
                })
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = { "playlist", "playlistsList", "userPlaylists" }, allEntries = true)
    public boolean deletePlaylist(Long id) {
        return playlistRepository.findById(id)
                .map(playlist -> {
                    playlist.setActive(false);
                    playlistRepository.save(playlist);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    @CacheEvict(value = { "playlist", "playlistsList", "userPlaylists" }, allEntries = true)
    public void deactivatePlaylistsByUserId(Long userId) {
        playlistRepository.deactivateByUserId(userId);
    }

    @Deprecated
    @Transactional
    public void deletePlaylistsByUserId(Long userId) {
        deactivatePlaylistsByUserId(userId);
    }

    @Cacheable(value = "userPlaylists", key = "#userId")
    public List<PlaylistDTO> getPlaylistsByUser(Long userId) {
        return playlistRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = { "playlist", "playlistsList", "userPlaylists" }, allEntries = true)
    public PlaylistDTO addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Playlist"));

        songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát"));

        if (!playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            Playlist playlistRef = playlistRepository.getReferenceById(playlistId);
            Song songRef = songRepository.getReferenceById(songId);
            PlaylistSong mapping = new PlaylistSong(
                    playlistRef, songRef, LocalDateTime.now());
            playlistSongRepository.save(mapping);
        }
        return new PlaylistDTO(playlist);
    }

    public boolean isOwner(Long playlistId, Long userId) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> playlist.getUser().getId().equals(userId))
                .orElse(false);
    }
}

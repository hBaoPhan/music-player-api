package com.example.musicplayer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.Playlist;
import com.example.musicplayer.entity.PlaylistSong;
import com.example.musicplayer.repository.PlaylistRepository;
import com.example.musicplayer.repository.PlaylistSongRepository;
import com.example.musicplayer.repository.SongRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> updatePlaylist(Long id, Playlist playlistDetails) {
        return playlistRepository.findById(id)
                .map(playlist -> {
                    playlist.setName(playlistDetails.getName());
                    playlist.setUser(playlistDetails.getUser());
                    if (playlistDetails.getUser() != null) {
                        playlist.setUserId(playlistDetails.getUser().getId());
                    }
                    playlist.setCreatedAt(playlistDetails.getCreatedAt());
                    return playlistRepository.save(playlist);
                });
    }

    @Transactional
    public boolean deletePlaylist(Long id) {
        if (playlistRepository.existsById(id)) {
            playlistSongRepository.deleteByPlaylistId(id);
            playlistRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deletePlaylistsByUserId(Long userId) {
        playlistSongRepository.deleteByUserId(userId);
        playlistRepository.deleteByUserId(userId);
    }

    public List<Playlist> getPlaylistsByUser(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    @Transactional
    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        // Tìm song av playlist
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Playlist"));

        songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát"));

        if (!playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            PlaylistSong mapping = new PlaylistSong(
                    playlistId, songId, LocalDateTime.now());
            playlistSongRepository.save(mapping);
        }
        return playlist;
    }

    public boolean isOwner(Long playlistId, Long userId) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> playlist.getUserId().equals(userId))
                .orElse(false);
    }
}

package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.Playlist;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.repository.PlaylistRepository;
import com.example.musicplayer.repository.SongRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaylistService {
    
    @Autowired
    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;
    
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
    
    public boolean deletePlaylist(Long id) {
        if (playlistRepository.existsById(id)) {
            playlistRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Playlist> getPlaylistsByUser(Long userId) {
        return playlistRepository.findByUserId(userId);
    }
    @Transactional 
    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        // Tìm song av playlist 
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Playlist"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát"));

        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
        }
        return playlistRepository.save(playlist);
    }
}

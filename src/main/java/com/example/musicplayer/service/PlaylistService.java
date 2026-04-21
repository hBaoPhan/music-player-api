package com.example.musicplayer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.musicplayer.dto.PlaylistRequestDTO;
import com.example.musicplayer.entity.CustomUserDetails;
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
        // Trả về tất cả playlist active — dùng nội bộ (admin)
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    public Playlist createPlaylist(PlaylistRequestDTO dto) {
        // Lấy userId từ SecurityContext — không tin tưởng client
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        Playlist playlist = new Playlist(dto.getName(), userId, LocalDateTime.now());
        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> updatePlaylist(Long id, PlaylistRequestDTO dto) {
        return playlistRepository.findById(id)
                .map(playlist -> {
                    // Chỉ cho phép cập nhật name — không thể thay đổi userId hay createdAt
                    playlist.setName(dto.getName());
                    return playlistRepository.save(playlist);
                });
    }

    @Transactional
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
    public void deactivatePlaylistsByUserId(Long userId) {
        // Soft delete: ẩn playlist, không xóa dự liệu
        playlistRepository.deactivateByUserId(userId);
    }

    /**
     * @deprecated Sử dụng deactivatePlaylistsByUserId() thay thế
     */
    @Deprecated
    @Transactional
    public void deletePlaylistsByUserId(Long userId) {
        deactivatePlaylistsByUserId(userId);
    }

    public List<Playlist> getPlaylistsByUser(Long userId) {
        return playlistRepository.findByUserIdAndActiveTrue(userId);
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

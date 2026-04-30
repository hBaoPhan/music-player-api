package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.PlaylistSong;
import com.example.musicplayer.repository.PlaylistSongRepository;
import jakarta.transaction.Transactional;

@Service
public class PlaylistSongService {

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    public List<PlaylistSong> getAllPlaylistSongs() {
        return playlistSongRepository.findAll();
    }

    public Optional<PlaylistSong> getPlaylistSongById(Long id) {
        return playlistSongRepository.findById(id);
    }

    public PlaylistSong createPlaylistSong(PlaylistSong playlistSong) {
        return playlistSongRepository.save(playlistSong);
    }

    public Optional<PlaylistSong> updatePlaylistSong(Long id, PlaylistSong playlistSongDetails) {
        return playlistSongRepository.findById(id)
                .map(playlistSong -> {
                    playlistSong.setPlaylist(playlistSongDetails.getPlaylist());
                    playlistSong.setSong(playlistSongDetails.getSong());
                    playlistSong.setAddedAt(playlistSongDetails.getAddedAt());
                    return playlistSongRepository.save(playlistSong);
                });
    }

    public boolean deletePlaylistSong(Long id) {
        if (playlistSongRepository.existsById(id)) {
            playlistSongRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PlaylistSong> getPlaylistSongsByPlaylist(Long playlistId) {
        return playlistSongRepository.findByPlaylistId(playlistId);
    }

    public void deletePlaylistSongByPlaylistAndSong(Long playlistId, Long songId) {
        playlistSongRepository.deleteByPlaylistIdAndSongId(playlistId, songId);
    }

    @Transactional
    public boolean deleteSongFromPlayListSongByPlayListIdAndSongId(Long playlistId, Long songId) {
        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            playlistSongRepository.deleteByPlaylistIdAndSongId(playlistId, songId);
            return true;
        }
        return false;
    }
}

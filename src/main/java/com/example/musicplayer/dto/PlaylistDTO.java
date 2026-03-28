package com.example.musicplayer.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.musicplayer.entity.Playlist;

public class PlaylistDTO {
    private Long id;
    private String name;
    private Long userId;
    private LocalDateTime createdAt;
    private List<SongDTO> songs;

    public PlaylistDTO() {}

    public PlaylistDTO(Playlist playlist) {
        if (playlist != null) {
            this.id = playlist.getId();
            this.name = playlist.getName();
            this.userId = playlist.getUserId();
            this.createdAt = playlist.getCreatedAt();
            if (playlist.getSongs() != null) {
                this.songs = playlist.getSongs().stream()
                        .map(SongDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}

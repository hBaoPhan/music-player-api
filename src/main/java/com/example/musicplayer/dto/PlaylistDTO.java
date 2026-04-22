package com.example.musicplayer.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.musicplayer.entity.Playlist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long userId;
    private LocalDateTime createdAt;
    private List<SongDTO> songs;

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
}

package com.example.musicplayer.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    public Playlist(String name, User user, LocalDateTime createdAt) {
        this.name = name;
        this.user = user;
        this.createdAt = createdAt;
    }

    public List<Song> getSongs() {
        return playlistSongs.stream()
                .map(PlaylistSong::getSong)
                .collect(Collectors.toList());
    }

    // Compatibility method — dùng PlaylistSongService để thêm bài
    public void setSongs(List<Song> songs) {
    }
}

package com.example.musicplayer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "playlist_songs")
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private Playlist playlist;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @ManyToOne
    @JoinColumn(name = "song_id", insertable = false, updatable = false)
    private Song song;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    public PlaylistSong(Long playlistId, Long songId, LocalDateTime addedAt) {
        this.playlistId = playlistId;
        this.songId = songId;
        this.addedAt = addedAt;
    }
}

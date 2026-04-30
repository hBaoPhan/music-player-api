package com.example.musicplayer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "user_history_song")
public class UserHistorySong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "listened_at", nullable = false)
    private LocalDateTime listenedAt;

    @Column(name = "duration_listened", nullable = false)
    private Integer durationListened; // Duration in seconds

    public UserHistorySong(User user, Song song, LocalDateTime listenedAt, Integer durationListened) {
        this.user = user;
        this.song = song;
        this.listenedAt = listenedAt;
        this.durationListened = durationListened;
    }
}

package com.example.musicplayer.dto;

import java.time.LocalDateTime;
import com.example.musicplayer.entity.UserHistorySong;

public class UserHistorySongDTO {
    private Long id;
    private Long userId;
    private SongDTO song;
    private LocalDateTime listenedAt;
    private Integer durationListened;

    public UserHistorySongDTO() {}

    public UserHistorySongDTO(UserHistorySong history) {
        this.id = history.getId();
        this.userId = history.getUserId();
        if (history.getSong() != null) {
            this.song = new SongDTO(history.getSong());
        }
        this.listenedAt = history.getListenedAt();
        this.durationListened = history.getDurationListened();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SongDTO getSong() {
        return song;
    }

    public void setSong(SongDTO song) {
        this.song = song;
    }

    public LocalDateTime getListenedAt() {
        return listenedAt;
    }

    public void setListenedAt(LocalDateTime listenedAt) {
        this.listenedAt = listenedAt;
    }

    public Integer getDurationListened() {
        return durationListened;
    }

    public void setDurationListened(Integer durationListened) {
        this.durationListened = durationListened;
    }
}

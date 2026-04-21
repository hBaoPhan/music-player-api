package com.example.musicplayer.dto;

import java.time.LocalDateTime;

import com.example.musicplayer.entity.UserHistorySong;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserHistorySongDTO {

    private Long id;
    private Long userId;
    private SongDTO song;
    private LocalDateTime listenedAt;
    private Integer durationListened;

    public UserHistorySongDTO(UserHistorySong history) {
        this.id = history.getId();
        this.userId = history.getUserId();
        if (history.getSong() != null) {
            this.song = new SongDTO(history.getSong());
        }
        this.listenedAt = history.getListenedAt();
        this.durationListened = history.getDurationListened();
    }
}

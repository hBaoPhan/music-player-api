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
    private UserDTO user;
    private SongDTO song;
    private LocalDateTime listenedAt;
    private Integer durationListened;

    public UserHistorySongDTO(UserHistorySong history) {
        this.id = history.getId();
        if (history.getUser() != null) {
            this.user = new UserDTO();
            this.user.setId(history.getUser().getId());
            this.user.setUsername(history.getUser().getUsername());
            this.user.setEmail(history.getUser().getEmail());
            this.user.setCreatedAt(history.getUser().getCreatedAt());
        }
        if (history.getSong() != null) {
            this.song = new SongDTO(history.getSong());
        }
        this.listenedAt = history.getListenedAt();
        this.durationListened = history.getDurationListened();
    }
}

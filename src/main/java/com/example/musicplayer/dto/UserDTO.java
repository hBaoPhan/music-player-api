package com.example.musicplayer.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.musicplayer.entity.AuthProvider;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private Role role;
    private AuthProvider provider;
    private List<PlaylistDTO> playlists;
    private List<SongDTO> favoriteSongs;
    private java.time.LocalDateTime createdAt;

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.provider = user.getProvider();
            this.createdAt = user.getCreatedAt();
            if (user.getPlaylists() != null) {
                this.playlists = user.getPlaylists().stream()
                        .map(PlaylistDTO::new)
                        .collect(Collectors.toList());
            }
            if (user.getFavoriteSongs() != null) {
                this.favoriteSongs = user.getFavoriteSongs().stream()
                        .map(SongDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }
}

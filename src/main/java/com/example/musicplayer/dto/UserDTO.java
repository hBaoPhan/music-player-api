package com.example.musicplayer.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.musicplayer.entity.AuthProvider;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private AuthProvider provider;
    private List<PlaylistDTO> playlists;
    private List<SongDTO> favoriteSongs;

    public UserDTO() {}

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.provider = user.getProvider();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public List<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public List<SongDTO> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(List<SongDTO> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }
}

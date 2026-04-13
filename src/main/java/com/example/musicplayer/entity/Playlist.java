package com.example.musicplayer.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
@Table(name = "playlists")
public class Playlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    public Playlist() {}

    public Playlist(String name, Long userId, LocalDateTime createdAt) {
        this.name = name;
        this.userId = userId;
        this.createdAt = createdAt;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PlaylistSong> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(List<PlaylistSong> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public List<Song> getSongs() {
        return playlistSongs.stream()
                .map(PlaylistSong::getSong)
                .collect(Collectors.toList());
    }

    public void setSongs(List<Song> songs) {
        // This method is kept for compatibility but doesn't allow direct setting 
        // without mapping to PlaylistSong.
        // It's better to use PlaylistSongService to add songs.
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}

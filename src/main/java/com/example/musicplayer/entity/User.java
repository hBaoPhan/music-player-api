package com.example.musicplayer.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String username;

    @Column(columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Playlist> playlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavorite> favorites = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.active = true;
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public List<Song> getFavoriteSongs() {
        return favorites.stream()
                .map(UserFavorite::getSong)
                .collect(Collectors.toList());
    }

    // Compatibility method — không set trực tiếp, dùng UserFavorite
    public void setFavoriteSongs(List<Song> favoriteSongs) {
    }
}

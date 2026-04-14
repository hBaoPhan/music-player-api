package com.example.musicplayer.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "albums")
public class Album {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;
    
    @Column(name = "artist_id", nullable = false)
    private Long artistId;
    
    @ManyToOne
    @JoinColumn(name = "artist_id", insertable = false, updatable = false)
    private Artist artist;
    
    @Column(name = "cover_url", columnDefinition = "TEXT")
    private String coverUrl;
    
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ALBUM'")
    private AlbumType type = AlbumType.ALBUM;

    @OneToMany(mappedBy = "album")
    private List<Song> songs;

    public Album() {}

    public Album(String title, Long artistId, String coverUrl, LocalDate releaseDate, AlbumType type) {
        this.title = title;
        this.artistId = artistId;
        this.coverUrl = coverUrl;
        this.releaseDate = releaseDate;
        if (type != null) {
            this.type = type;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public AlbumType getType() {
        return type;
    }

    public void setType(AlbumType type) {
        this.type = type;
    }
}

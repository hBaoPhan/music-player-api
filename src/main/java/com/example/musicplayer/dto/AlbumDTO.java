package com.example.musicplayer.dto;

import java.time.LocalDate;
import com.example.musicplayer.entity.Album;

public class AlbumDTO {
    private Long id;
    private String title;
    private Long artistId;
    private ArtistDTO artist;
    private String coverUrl;
    private LocalDate releaseDate;

    public AlbumDTO() {}

    public AlbumDTO(Album album) {
        if (album != null) {
            this.id = album.getId();
            this.title = album.getTitle();
            this.artistId = album.getArtistId();
            this.coverUrl = album.getCoverUrl();
            this.releaseDate = album.getReleaseDate();
            if (album.getArtist() != null) {
                this.artist = new ArtistDTO(album.getArtist());
            }
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

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
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
}

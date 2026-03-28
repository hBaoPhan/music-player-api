package com.example.musicplayer.dto;

import com.example.musicplayer.entity.Artist;

public class ArtistDTO {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;

    public ArtistDTO() {}

    public ArtistDTO(Artist artist) {
        if (artist != null) {
            this.id = artist.getId();
            this.name = artist.getName();
            this.bio = artist.getBio();
            this.avatarUrl = artist.getAvatarUrl();
        }
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

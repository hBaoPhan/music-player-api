package com.example.musicplayer.dto;

import com.example.musicplayer.entity.Song;

public class SongDTO {
    private Long id;
    private String title;
    private String audioUrl;
    private Integer duration;
    private Integer playCount;
    private String category;
    private ArtistDTO artist;
    private AlbumDTO album;

    public SongDTO() {}

    public SongDTO(Song song) {
        if (song != null) {
            this.id = song.getId();
            this.title = song.getTitle();
            this.audioUrl = song.getAudioUrl();
            this.duration = song.getDuration();
            this.playCount = song.getPlayCount();
            this.category = song.getCategory();
            if (song.getArtist() != null) {
                this.artist = new ArtistDTO(song.getArtist());
            }
            if (song.getAlbum() != null) {
                this.album = new AlbumDTO(song.getAlbum());
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

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

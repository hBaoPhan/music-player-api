package com.example.musicplayer.dto;

import com.example.musicplayer.entity.Genre;
import com.example.musicplayer.entity.Song;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SongDTO {

    private Long id;
    private String title;
    private String audioUrl;
    private Integer duration;
    private Integer playCount;
    private Genre genre;
    private ArtistDTO artist;
    private AlbumDTO album;

    public SongDTO(Song song) {
        if (song != null) {
            this.id = song.getId();
            this.title = song.getTitle();
            this.audioUrl = song.getAudioUrl();
            this.duration = song.getDuration();
            this.playCount = song.getPlayCount();
            this.genre = song.getGenre();
            if (song.getArtist() != null) {
                this.artist = new ArtistDTO(song.getArtist());
            }
            if (song.getAlbum() != null) {
                this.album = new AlbumDTO(song.getAlbum());
            }
        }
    }
}

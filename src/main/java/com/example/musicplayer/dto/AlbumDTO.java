package com.example.musicplayer.dto;

import java.time.LocalDate;

import com.example.musicplayer.entity.Album;
import com.example.musicplayer.entity.AlbumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlbumDTO {

    private Long id;
    private String title;
    private Long artistId;
    private ArtistDTO artist;
    private String coverUrl;
    private LocalDate releaseDate;
    private AlbumType type;

    public AlbumDTO(Album album) {
        if (album != null) {
            this.id = album.getId();
            this.title = album.getTitle();
            this.artistId = album.getArtistId();
            this.coverUrl = album.getCoverUrl();
            this.releaseDate = album.getReleaseDate();
            this.type = album.getType();
            if (album.getArtist() != null) {
                this.artist = new ArtistDTO(album.getArtist());
            }
        }
    }
}

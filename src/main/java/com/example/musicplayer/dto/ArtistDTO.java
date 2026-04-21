package com.example.musicplayer.dto;

import com.example.musicplayer.entity.Artist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArtistDTO {

    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;

    public ArtistDTO(Artist artist) {
        if (artist != null) {
            this.id = artist.getId();
            this.name = artist.getName();
            this.bio = artist.getBio();
            this.avatarUrl = artist.getAvatarUrl();
        }
    }
}

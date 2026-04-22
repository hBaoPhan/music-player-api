package com.example.musicplayer.dto;

import com.example.musicplayer.entity.Artist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ArtistDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

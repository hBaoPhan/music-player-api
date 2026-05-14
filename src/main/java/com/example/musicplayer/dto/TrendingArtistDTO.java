package com.example.musicplayer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrendingArtistDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    private Long playCount;
}

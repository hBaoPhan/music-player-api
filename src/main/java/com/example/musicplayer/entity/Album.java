package com.example.musicplayer.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ALBUM'")
    private AlbumType type = AlbumType.ALBUM;

    @OneToMany(mappedBy = "album")
    private List<Song> songs;

    public Album(String title, Long artistId, String coverUrl, LocalDate releaseDate, AlbumType type) {
        this.title = title;
        this.artistId = artistId;
        this.coverUrl = coverUrl;
        this.releaseDate = releaseDate;
        if (type != null) {
            this.type = type;
        }
    }
}

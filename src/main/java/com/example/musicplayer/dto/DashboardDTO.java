package com.example.musicplayer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private long totalUsers;
    private long usersOnline;
    private long newUsersToday;
    private long totalSongs;
    private long totalArtists;
    private long totalStreams;

    private List<DailyCountDTO> userGrowthLast7Days;
    private List<DailyCountDTO> streamsLast7Days;

    private List<TrendingSongDTO> top10TrendingSongs;
    private List<TrendingSongDTO> top10FavoriteSongs;
    private List<GenreDistributionDTO> genreDistribution;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyCountDTO {
        private String date;
        private long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendingSongDTO {
        private Long songId;
        private String title;
        private String artistName;
        private String coverUrl;
        private long playCount;
        private String audioUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreDistributionDTO {
        private String genre;
        private long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendingArtistDTO {
        private Long id;
        private String name;
        private String avatarUrl;
        private Long playCount;
    }
}

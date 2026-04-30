package com.example.musicplayer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.musicplayer.dto.DashboardDTO;
import com.example.musicplayer.dto.DashboardDTO.DailyCountDTO;
import com.example.musicplayer.dto.DashboardDTO.GenreDistributionDTO;
import com.example.musicplayer.dto.DashboardDTO.TrendingSongDTO;
import com.example.musicplayer.repository.ArtistRepository;
import com.example.musicplayer.repository.SongRepository;
import com.example.musicplayer.repository.UserFavoriteRepository;
import com.example.musicplayer.repository.UserHistorySongRepository;
import com.example.musicplayer.repository.UserRepository;

@Service
public class DashboardService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SongRepository songRepository;

        @Autowired
        private ArtistRepository artistRepository;

        @Autowired
        private UserHistorySongRepository userHistorySongRepository;

        @Autowired
        private UserFavoriteRepository userFavoriteRepository;

        @Autowired
        private UserPresenceService userPresenceService;

        private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public DashboardDTO getFullDashboard() {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime sevenDaysAgo = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

                long totalUsers = userRepository.countByActiveTrue();
                long newToday = userRepository.countNewUsersToday(startOfDay, endOfDay);
                long totalSongs = songRepository.countByActiveTrue();
                long totalArtists = artistRepository.countByActiveTrue();
                long totalStreams = userHistorySongRepository.count();

                List<DailyCountDTO> userGrowth = buildDailySeries(
                                userRepository.countNewUsersByDaySince(sevenDaysAgo), sevenDaysAgo, 7);

                List<DailyCountDTO> streamGrowth = buildDailySeries(
                                userHistorySongRepository.countStreamsByDaySince(sevenDaysAgo), sevenDaysAgo, 7);

                List<TrendingSongDTO> top10Trending = userHistorySongRepository
                                .findTop10TrendingSongsSince(sevenDaysAgo, PageRequest.of(0, 10))
                                .stream()
                                .map(row -> new TrendingSongDTO(
                                                ((Number) row[0]).longValue(),
                                                (String) row[1],
                                                row[2] != null ? (String) row[2] : "Unknown",
                                                row[3] != null ? (String) row[3] : null,
                                                ((Number) row[4]).longValue()))
                                .collect(Collectors.toList());

                List<TrendingSongDTO> top10Favorites = userFavoriteRepository
                                .findTop10FavoriteSongs(PageRequest.of(0, 10))
                                .stream()
                                .map(row -> new TrendingSongDTO(
                                                ((Number) row[0]).longValue(),
                                                (String) row[1],
                                                row[2] != null ? (String) row[2] : "Unknown",
                                                row[3] != null ? (String) row[3] : null,
                                                ((Number) row[4]).longValue()))
                                .collect(Collectors.toList());

                List<GenreDistributionDTO> genreDist = songRepository.countSongsByGenre()
                                .stream()
                                .map(row -> new GenreDistributionDTO(
                                                row[0] != null ? row[0].toString() : "OTHER",
                                                ((Number) row[1]).longValue()))
                                .collect(Collectors.toList());

                return DashboardDTO.builder()
                                .totalUsers(totalUsers)
                                .usersOnline(userPresenceService.getOnlineCount())
                                .newUsersToday(newToday)
                                .totalSongs(totalSongs)
                                .totalArtists(totalArtists)
                                .totalStreams(totalStreams)
                                .userGrowthLast7Days(userGrowth)
                                .streamsLast7Days(streamGrowth)
                                .top10TrendingSongs(top10Trending)
                                .top10FavoriteSongs(top10Favorites)
                                .genreDistribution(genreDist)
                                .build();
        }

        private List<DailyCountDTO> buildDailySeries(List<Object[]> rawRows, LocalDateTime since, int days) {
                Map<String, Long> lookup = rawRows.stream()
                                .collect(Collectors.toMap(
                                                row -> row[0].toString(),
                                                row -> ((Number) row[1]).longValue()));

                List<DailyCountDTO> series = new ArrayList<>();
                LocalDate startDate = since.toLocalDate();
                for (int i = 0; i < days; i++) {
                        String dateStr = startDate.plusDays(i).format(DATE_FMT);
                        series.add(new DailyCountDTO(dateStr, lookup.getOrDefault(dateStr, 0L)));
                }
                return series;
        }
}

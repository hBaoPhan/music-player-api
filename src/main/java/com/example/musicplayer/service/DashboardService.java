package com.example.musicplayer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
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

        // Self-inject via @Lazy to route @Async calls through the Spring AOP proxy
        @Lazy
        @Autowired
        private DashboardService self;

        private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Async("dashboardExecutor")
        public CompletableFuture<Long> fetchTotalUsers() {
                return CompletableFuture.completedFuture(userRepository.countByActiveTrue());
        }

        @Async("dashboardExecutor")
        public CompletableFuture<Long> fetchNewUsersToday(LocalDateTime start, LocalDateTime end) {
                return CompletableFuture.completedFuture(userRepository.countNewUsersToday(start, end));
        }

        @Async("dashboardExecutor")
        public CompletableFuture<Long> fetchTotalSongs() {
                return CompletableFuture.completedFuture(songRepository.countByActiveTrue());
        }

        @Async("dashboardExecutor")
        public CompletableFuture<Long> fetchTotalArtists() {
                return CompletableFuture.completedFuture(artistRepository.countByActiveTrue());
        }

        @Async("dashboardExecutor")
        public CompletableFuture<Long> fetchTotalStreams() {
                return CompletableFuture.completedFuture(userHistorySongRepository.count());
        }

        @Async("dashboardExecutor")
        public CompletableFuture<List<DailyCountDTO>> fetchUserGrowth(LocalDateTime since) {
                List<Object[]> raw = userRepository.countNewUsersByDaySince(since);
                return CompletableFuture.completedFuture(buildDailySeries(raw, since, 7));
        }

        @Async("dashboardExecutor")
        public CompletableFuture<List<DailyCountDTO>> fetchStreamGrowth(LocalDateTime since) {
                List<Object[]> raw = userHistorySongRepository.countStreamsByDaySince(since);
                return CompletableFuture.completedFuture(buildDailySeries(raw, since, 7));
        }

        @Async("dashboardExecutor")
        public CompletableFuture<List<TrendingSongDTO>> fetchTop10Trending(LocalDateTime since) {
                List<TrendingSongDTO> result = userHistorySongRepository
                                .findTop10TrendingSongsSince(since, PageRequest.of(0, 10))
                                .stream()
                                .map(row -> new TrendingSongDTO(
                                                ((Number) row[0]).longValue(),
                                                (String) row[1],
                                                row[2] != null ? (String) row[2] : "Unknown",
                                                row[3] != null ? (String) row[3] : null,
                                                ((Number) row[4]).longValue(),
                                                row[5] != null ? (String) row[5] : null))
                                .collect(Collectors.toList());
                return CompletableFuture.completedFuture(result);
        }

        @Async("dashboardExecutor")
        public CompletableFuture<List<TrendingSongDTO>> fetchTop10Favorites() {
                List<TrendingSongDTO> result = userFavoriteRepository
                                .findTop10FavoriteSongs(PageRequest.of(0, 10))
                                .stream()
                                .map(row -> new TrendingSongDTO(
                                                ((Number) row[0]).longValue(),
                                                (String) row[1],
                                                row[2] != null ? (String) row[2] : "Unknown",
                                                row[3] != null ? (String) row[3] : null,
                                                ((Number) row[4]).longValue(),
                                                row[5] != null ? (String) row[5] : null))
                                .collect(Collectors.toList());
                return CompletableFuture.completedFuture(result);
        }

        @Async("dashboardExecutor")
        public CompletableFuture<List<GenreDistributionDTO>> fetchGenreDistribution() {
                List<GenreDistributionDTO> result = songRepository.countSongsByGenre()
                                .stream()
                                .map(row -> new GenreDistributionDTO(
                                                row[0] != null ? row[0].toString() : "OTHER",
                                                ((Number) row[1]).longValue()))
                                .collect(Collectors.toList());
                return CompletableFuture.completedFuture(result);
        }

        public DashboardDTO getFullDashboard() {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime sevenDaysAgo = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

                // Fire all 10 queries concurrently via proxy (required for @Async to work)
                CompletableFuture<Long> fTotalUsers = self.fetchTotalUsers();
                CompletableFuture<Long> fNewToday = self.fetchNewUsersToday(startOfDay, endOfDay);
                CompletableFuture<Long> fTotalSongs = self.fetchTotalSongs();
                CompletableFuture<Long> fTotalArtists = self.fetchTotalArtists();
                CompletableFuture<Long> fTotalStreams = self.fetchTotalStreams();
                CompletableFuture<List<DailyCountDTO>> fUserGrowth = self.fetchUserGrowth(sevenDaysAgo);
                CompletableFuture<List<DailyCountDTO>> fStreamGrowth = self.fetchStreamGrowth(sevenDaysAgo);
                CompletableFuture<List<TrendingSongDTO>> fTrending = self.fetchTop10Trending(sevenDaysAgo);
                CompletableFuture<List<TrendingSongDTO>> fFavorites = self.fetchTop10Favorites();
                CompletableFuture<List<GenreDistributionDTO>> fGenre = self.fetchGenreDistribution();

                // Wait for all to finish
                CompletableFuture.allOf(
                                fTotalUsers, fNewToday, fTotalSongs, fTotalArtists, fTotalStreams,
                                fUserGrowth, fStreamGrowth, fTrending, fFavorites, fGenre).join();

                return DashboardDTO.builder()
                                .totalUsers(fTotalUsers.join())
                                .usersOnline(userPresenceService.getOnlineCount())
                                .newUsersToday(fNewToday.join())
                                .totalSongs(fTotalSongs.join())
                                .totalArtists(fTotalArtists.join())
                                .totalStreams(fTotalStreams.join())
                                .userGrowthLast7Days(fUserGrowth.join())
                                .streamsLast7Days(fStreamGrowth.join())
                                .top10TrendingSongs(fTrending.join())
                                .top10FavoriteSongs(fFavorites.join())
                                .genreDistribution(fGenre.join())
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

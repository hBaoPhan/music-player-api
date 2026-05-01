package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import com.example.musicplayer.dto.UserHistorySongDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.entity.UserFavorite;
import com.example.musicplayer.entity.UserHistorySong;
import com.example.musicplayer.repository.PlaylistRepository;
import com.example.musicplayer.repository.SongRepository;

import com.example.musicplayer.repository.UserFavoriteRepository;
import com.example.musicplayer.repository.UserHistorySongRepository;
import com.example.musicplayer.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private UserHistorySongRepository userHistorySongRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAllByActiveTrue();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdAndActiveTrue(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getUsername() != null && !userDetails.getUsername().trim().isEmpty()) {
                        user.setUsername(userDetails.getUsername());
                    }

                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }

                    if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
                        user.setEmail(userDetails.getEmail());
                    }

                    return userRepository.save(user);
                });
    }

    @Transactional
    public Optional<User> updateRole(Long id, Role role) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(role);
                    return userRepository.save(user);
                });
    }

    @Transactional
    public boolean deactivateUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    playlistRepository.deactivateByUserId(id);
                    return true;
                })
                .orElse(false);
    }

    // @Transactional
    // public void reactivateUser(Long id) {
    // userRepository.findById(id).ifPresent(user -> {
    // user.setActive(true);
    // userRepository.save(user);
    // playlistRepository.reactivateByUserId(id);
    // });
    // }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
        });
    }

    @Transactional
    public void toggleFavorite(Long userId, Long songId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy User");
        }
        if (!songRepository.existsById(songId)) {
            throw new RuntimeException("Không tìm thấy Bài hát");
        }

        userFavoriteRepository.findByUserIdAndSongId(userId, songId)
                .ifPresentOrElse(
                        userFavoriteRepository::delete,
                        () -> {
                            User userRef = userRepository.getReferenceById(userId);
                            Song songRef = songRepository.getReferenceById(songId);
                            userFavoriteRepository.save(new UserFavorite(userRef, songRef, LocalDateTime.now()));
                        });
    }

    public List<Song> getFavoriteSongs(Long userId) {
        return userFavoriteRepository.findByUserId(userId).stream()
                .map(UserFavorite::getSong)
                .collect(Collectors.toList());
    }

    public List<UserHistorySongDTO> getHistorySong(Long userId) {
        return userHistorySongRepository.findUniqueDailyHistoryByUserId(userId).stream()
                .map(UserHistorySongDTO::new)
                .collect(Collectors.toList());
    }

    public List<Song> getTopSongsThisMonth(Long userId) {
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return userHistorySongRepository.findTopSongsThisMonth(userId, startOfMonth, PageRequest.of(0, 10));
    }

    @Transactional
    public void addHistorySong(Long userId, Long songId, Integer duration) { //// sau này sẽ phát triển history dựa trên
                                                                             //// duration
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy User");
        }
        if (!songRepository.existsById(songId)) {
            throw new RuntimeException("Không tìm thấy Bài hát");
        }
        User userRef = userRepository.getReferenceById(userId);
        Song songRef = songRepository.getReferenceById(songId);
        UserHistorySong history = new UserHistorySong(userRef, songRef, LocalDateTime.now(), duration);
        userHistorySongRepository.save(history);
    }

    public boolean isOwner(Long userId, String principal) {
        return userRepository.findByIdAndActiveTrue(userId)
                .map(user -> user.getUsername().equals(principal))
                .orElse(false);
    }
}

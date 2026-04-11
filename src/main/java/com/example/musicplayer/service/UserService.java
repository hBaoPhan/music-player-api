package com.example.musicplayer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.entity.UserFavorite;
import com.example.musicplayer.repository.SongRepository;
import com.example.musicplayer.repository.PlaylistSongRepository;
import com.example.musicplayer.repository.UserFavoriteRepository;
import com.example.musicplayer.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final PlaylistService playlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    UserService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
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
                        user.setPassword(userDetails.getPassword());
                    }

                    if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
                        user.setEmail(userDetails.getEmail());
                    }

                    if (userDetails.getRole() != null) {
                        user.setRole(userDetails.getRole());
                    }

                    // if (userDetails.getProvider() != null) {
                    // user.setProvider(userDetails.getProvider());
                    // }

                    if (userDetails.getPlaylists() != null) {
                        user.setPlaylists(userDetails.getPlaylists());
                    }
                    return userRepository.save(user);
                });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {

            playlistSongRepository.deleteByUserId(id);

            playlistService.deletePlaylistsByUserId(id);

            userFavoriteRepository.deleteByUserId(id);

            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

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
                        () -> userFavoriteRepository.save(new UserFavorite(userId, songId, LocalDateTime.now())));
    }

    public List<Song> getFavoriteSongs(Long userId) {
        return userFavoriteRepository.findByUserId(userId).stream()
                .map(UserFavorite::getSong)
                .collect(Collectors.toList());
    }
}

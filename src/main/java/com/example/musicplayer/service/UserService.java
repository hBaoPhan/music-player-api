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
import com.example.musicplayer.repository.UserFavoriteRepository;
import com.example.musicplayer.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

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
                    user.setUsername(userDetails.getUsername());
                    user.setPassword(userDetails.getPassword());
                    user.setEmail(userDetails.getEmail());
                    user.setRole(userDetails.getRole());
                    if (userDetails.getPlaylists() != null) {
                        user.setPlaylists(userDetails.getPlaylists());
                    }
                    if (userDetails.getFavoriteSongs() != null) {
                        user.setFavoriteSongs(userDetails.getFavoriteSongs());
                    }
                    return userRepository.save(user);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
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
                    () -> userFavoriteRepository.save(new UserFavorite(userId, songId, LocalDateTime.now()))
                );
    }

    public List<Song> getFavoriteSongs(Long userId) {
        return userFavoriteRepository.findByUserId(userId).stream()
                .map(UserFavorite::getSong)
                .collect(Collectors.toList());
    }
}

package com.example.musicplayer.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.musicplayer.dto.PlaylistDTO;
import com.example.musicplayer.dto.SongDTO;
import com.example.musicplayer.dto.UserDTO;
import com.example.musicplayer.dto.UserHistorySongDTO;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.Song;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.entity.UserHistorySong;
import com.example.musicplayer.service.PlaylistService;
import com.example.musicplayer.service.UserService;
import com.example.musicplayer.security.JwtTokenProvider;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, principal.username)")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, principal.username)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails,
            Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return userService.updateUser(id, userDetails, isAdmin)
                .map(user -> {
                    String newToken = tokenProvider.generateTokenFromUsername(user.getUsername());
                    Map<String, Object> response = new HashMap<>();
                    response.put("user", new UserDTO(user));
                    response.put("token", newToken);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String roleStr = body.get("role");
        if (roleStr == null || roleStr.isBlank()) {
            return ResponseEntity.badRequest().body("Role không được để trống.");
        }

        Role newRole;
        try {
            newRole = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Role không hợp lệ: " + roleStr);
        }

        UserDetails principal = (UserDetails) authentication
                .getPrincipal();
        User caller = userService.findByUsername(principal.getUsername());
        if (caller.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không thể thay đổi role của chính mình.");
        }

        Role finalNewRole = newRole;
        User dummy = new User();
        dummy.setRole(finalNewRole);
        return userService.updateUser(id, dummy, true)
                .map(u -> ResponseEntity.ok((Object) new UserDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deactivateUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/playlists")
    public ResponseEntity<List<PlaylistDTO>> getUserPlaylists(@PathVariable Long id) {
        List<PlaylistDTO> userPlaylists = playlistService.getPlaylistsByUser(id);
        return ResponseEntity.ok(userPlaylists);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(new UserDTO(user));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{userId}/favorites")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, principal.username)")
    public ResponseEntity<List<SongDTO>> getFavoriteSongs(@PathVariable Long userId) {
        try {
            List<Song> favorites = userService.getFavoriteSongs(userId);
            List<SongDTO> favoriteDTOs = favorites.stream()
                    .map(SongDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(favoriteDTOs);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/favorites/{songId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, principal.username)")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long userId, @PathVariable Long songId) {
        try {
            userService.toggleFavorite(userId, songId);
            return ResponseEntity.ok("Đã cập nhật danh sách yêu thích!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/history")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, principal.username)")
    public ResponseEntity<?> getHistorySong(@PathVariable Long userId) {
        try {
            List<UserHistorySong> history = userService.getHistorySong(userId);
            List<UserHistorySongDTO> historyDtos = history.stream()
                    .map(UserHistorySongDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(historyDtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/history/{songId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, principal.username)")
    public ResponseEntity<?> addHistorySong(
            @PathVariable Long userId,
            @PathVariable Long songId,
            @RequestParam(defaultValue = "0") Integer duration) {
        try {
            userService.addHistorySong(userId, songId, duration);
            return ResponseEntity.ok("Đã lưu vào lịch sử nghe nhạc!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/history/top-this-month")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, principal.username)")
    public ResponseEntity<List<SongDTO>> getTopSongsThisMonth(@PathVariable Long userId) {
        try {
            List<Song> songs = userService.getTopSongsThisMonth(userId);
            List<SongDTO> songDtos = songs.stream()
                    .map(SongDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(songDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

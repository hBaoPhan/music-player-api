package com.example.musicplayer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistRequestDTO {

    @NotBlank(message = "Tên danh sách phát không được rỗng")
    @Size(max = 255, message = "Tên danh sách phát không được vượt quá 255 kí tự")
    private String name;
}

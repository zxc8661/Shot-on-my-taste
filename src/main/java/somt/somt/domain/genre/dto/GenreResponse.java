package somt.somt.domain.genre.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.entity.Genre;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse {
    private Long id;

    private String name;


    private LocalDateTime createAt;


    public GenreResponse(Genre genre){
        this.id =genre.getId();
        this.name = genre.getName();
        this.createAt = genre.getCreateAt();
    }
}

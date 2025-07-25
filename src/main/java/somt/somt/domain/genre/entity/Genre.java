package somt.somt.domain.genre.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.dto.GenreRequest;
import somt.somt.domain.genreProduct.entity.GenreProduct;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    


    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "modify_at",nullable = false)
    private LocalDateTime modifyAt;

    public static Genre create(GenreRequest genreCreateRequest){
        Genre genre = new Genre();
        genre.createAt=LocalDateTime.now();
        genre.name = genreCreateRequest.getName();
        genre.modifyAt=LocalDateTime.now();
        return genre;
    }

    public static Genre create(String name){
        Genre genre = new Genre();
        genre.createAt=LocalDateTime.now();
        genre.modifyAt=LocalDateTime.now();
        return genre;
    }


    public void modify(String newName){

        this.name = newName;
        this.modifyAt = LocalDateTime.now();
    }


    // 단위 테스트 용 코드
    public Genre(Long id, String name){
        this.id=id;
        this.name = name;
        this.createAt=LocalDateTime.now();
        this.modifyAt=LocalDateTime.now();
    }

}

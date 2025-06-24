package somt.somt.domain.genre.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.dto.GenreRequest;

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

    @OneToMany(mappedBy = "genre",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<GenreProduct> genreProductList = new ArrayList<>();


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

    public void modify(String newName){

        this.name = newName;
        this.modifyAt = LocalDateTime.now();
    }
}

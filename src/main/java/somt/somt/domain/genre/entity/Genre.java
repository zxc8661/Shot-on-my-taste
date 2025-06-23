package somt.somt.domain.genre.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<GenreProduct> genreProductList;


    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;
}

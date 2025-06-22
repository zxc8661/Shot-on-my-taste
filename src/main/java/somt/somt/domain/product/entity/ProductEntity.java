package somt.somt.domain.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long product_genre_id;

    @Column(name = "price" ,nullable = false)
    private MediaType price;

    @Column(name = "number" ,nullable = false)
    private Integer number;


    private String img1;

    private String img2;

    private String img3;
    private String img4;

    private String img5;

    @Column(name = "content" ,nullable = false)
    private String content;

    @Column(name = "create_at" ,nullable = false)
    private LocalDateTime createAt;

    @Column(name = "modify_at" ,nullable = false)
    private LocalDateTime modifyAt;
}

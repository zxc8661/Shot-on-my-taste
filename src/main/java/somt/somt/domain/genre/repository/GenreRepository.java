package somt.somt.domain.genre.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.genre.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
    boolean existsByName(String name);

    Genre findByName(String name);
}

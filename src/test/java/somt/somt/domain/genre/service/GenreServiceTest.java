package somt.somt.domain.genre.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import somt.somt.common.exception.CustomException;
import somt.somt.domain.genre.dto.GenreRequest;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genre.repository.GenreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class GenreServiceTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;


    private static GenreRequest genreRequest;

    @BeforeEach
    void setUp(){
        genreRepository.deleteAll();
        genreRequest = new GenreRequest("액션");
        genreService.create(genreRequest);

    }




    @Test
    @DisplayName("생성 기본")
    void create() {


        List<Genre> genreList = genreRepository.findAll();
        assertEquals(1,genreList.size());
        assertEquals("액션",genreList.get(0).getName());
        assertNotNull(genreList.get(0).getCreateAt());
    }



    @Test
    void getGenreList() {
        genreService.create(new GenreRequest("스포츠"));

        List<Genre> genreList = genreRepository.findAll();
        assertEquals(2,genreList.size());
        assertEquals("액션",genreList.get(0).getName());
        assertEquals("스포츠",genreList.get(1).getName());


    }

    @Test
    void modify() {
        List<Genre> genreList = genreRepository.findAll();
        Long id =genreList.get(0).getId();

        genreService.modify(id,new GenreRequest("스포츠"));

        List<Genre> genreList2 = genreRepository.findAll();
        assertEquals(genreList2.get(0).getName(),"스포츠");
    }

    @Test
    void delete() {
        List<Genre> genreList = genreRepository.findAll();
        Long id =genreList.get(0).getId();
        genreService.delete(id);

        List<Genre> genreList2 = genreRepository.findAll();
        assertEquals(genreList2.size(),0);
    }
}
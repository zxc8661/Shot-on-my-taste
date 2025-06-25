package somt.somt.domain.genre.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.domain.genre.dto.GenreRequest;
import somt.somt.domain.genre.dto.GenreResponse;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genre.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;


    @Transactional
    public void create(GenreRequest genreCreateRequest){
        alreadyExist(genreCreateRequest.getName());

        Genre newGenre = Genre.create(genreCreateRequest);

        genreRepository.save(newGenre);

    }

    @Transactional
    public void create(String name){
        alreadyExist(name);

        Genre newGenre = new Genre();

    }



    public List<GenreResponse> getGenreList(){
        List<Genre> genreList = genreRepository.findAll();

        return genreList.stream().map(GenreResponse::new).collect(Collectors.toList());
    }

    public Genre getGere(String name){
        return genreRepository.findByName(name);
    }




    @Transactional
    public void modify(Long genreId, GenreRequest genreRequest) {

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_GENRE));

        alreadyExist(genreRequest.getName());

        genre.modify(genreRequest.getName());

        genreRepository.save(genre);


    }


    @Transactional
    public void delete(Long genreId) {
        Genre genre = genreRepository.findById(genreId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_GENRE));

        genreRepository.delete(genre);
    }

    private void alreadyExist(String name){
        if(genreRepository.existsByName(name))
        {
            throw new CustomException(ErrorCode.GENRE_EXIST);
        }
    }

    private GenreResponse exchange(Genre genre){
        return new GenreResponse(genre);
    }

}

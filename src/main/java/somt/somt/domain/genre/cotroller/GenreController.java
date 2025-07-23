package somt.somt.domain.genre.cotroller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.genre.dto.GenreRequest;
import somt.somt.domain.genre.dto.GenreResponse;
import somt.somt.domain.genre.service.GenreService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GenreController {

    private final GenreService genreService;


    @PostMapping("/admin/genre")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid GenreRequest genreRequest){


        genreService.create(genreRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success("장르 추가 완료"));
    }

    @GetMapping("/public/genres")
    public ResponseEntity<?> genres(){

        List<GenreResponse> responseList = genreService.getGenreList();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(responseList,"장르 조회 성공"));
    }

    @PutMapping("/admin/genres/{genreId}")
    public ResponseEntity<?> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable(name = "genreId") Long genreId,
                                    @RequestBody @Valid GenreRequest genreRequest){


        genreService.modify(genreId,genreRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("장르 수정 성공"));
    }

    @DeleteMapping("/admin/genres/{genreId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable(name = "genreId") Long genreId){



        genreService.delete(genreId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("장르 삭제 성공 "));
    }
}

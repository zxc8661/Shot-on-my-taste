package somt.somt.domain.genre.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import somt.somt.domain.genre.dto.GenreCreateRequest;
import somt.somt.domain.genre.service.GenreService;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;


    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody GenreCreateRequest createRequest){
        return ResponseEntity.ok("good");
    }

    @GetMapping
    public ResponseEntity<?> genres(@RequestParam(name = "page") int page,
                                    @RequestParam(name = "size") int size){
        return ResponseEntity.ok("good");
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<?> modify(@PathVariable(name = "genreId") Long genreId){
        return ResponseEntity.ok("good");
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> delete(@PathVariable(name = "genreId") Long genreId){
        return ResponseEntity.ok("good");
    }
}

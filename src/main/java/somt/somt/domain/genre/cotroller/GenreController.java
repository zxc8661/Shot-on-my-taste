package somt.somt.domain.genre.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "genre API",description = "장르 API")
public class GenreController {

    private final GenreService genreService;


    @PostMapping("/admin/genre")
    @Operation(summary = "장르 생성",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201",description = "장르 생성 완료", content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid GenreRequest genreRequest){


        genreService.create(genreRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success("장르 추가 완료"));
    }

    @GetMapping("/public/genres")
    @Operation(summary= "장르 조회 ")
    @ApiResponse(responseCode = "200",description = "장르 조회 성공", content = @Content(schema = @Schema(implementation = GenreListResponse.class)))
    public ResponseEntity<?> genres(){

        List<GenreResponse> responseList = genreService.getGenreList();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(responseList,"장르 조회 성공"));
    }

    @PutMapping("/admin/genres/{genreId}")
    @Operation(summary = "장르 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "장르 수정 성공",content = @Content(schema = @Schema(implementation = GenreStringResponse.class)))
    public ResponseEntity<?> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable(name = "genreId") Long genreId,
                                    @RequestBody @Valid GenreRequest genreRequest){


        genreService.modify(genreId,genreRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("장르 수정 성공"));
    }

    @DeleteMapping("/admin/genres/{genreId}")
    @Operation(summary = "장르 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "장르 삭제 성공", content = @Content(schema = @Schema(implementation = GenreIdResponse.class)))
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable(name = "genreId") Long genreId){



        genreService.delete(genreId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("장르 삭제 성공 "));
    }
}


@Schema(name = "장르 응답 List")
class GenreListResponse extends CustomResponse<List<String>>{};

@Schema(name =  "장르 응답 String")
class GenreStringResponse extends CustomResponse<String>{};

@Schema(name = "장르 응답 id")
class GenreIdResponse extends  CustomResponse<Long>{};
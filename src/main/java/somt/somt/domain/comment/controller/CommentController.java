package somt.somt.domain.comment.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.comment.dto.CommentModifyRequest;
import somt.somt.domain.comment.dto.CommentRequest;
import somt.somt.domain.comment.dto.CommentResponse;
import somt.somt.domain.comment.service.CommentService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Comment API", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/user/comments")
    @Operation(summary = "댓글 생성",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "상품 생성 성공", content = @Content(schema = @Schema(implementation = CommentIdResponse.class)))
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid CommentRequest commentRequest) {
        Long id = commentService.create(customUserDetails,commentRequest);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success(id,"댓글 생성 성공"));
    }

    @GetMapping("/public/comments/{productId}")
    @Operation(summary = "댓글 목록 조회")
    @ApiResponse(responseCode = "200",description = "댓글 조회 성공",content = @Content(schema = @Schema(implementation = CommentListResponse.class)))
    public ResponseEntity<?> getCommentList(@PathVariable(name = "productId") Long productId,
                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                            @RequestParam(name = "size",defaultValue = "10") int size){
        CustomPageResponse<CommentResponse> response = commentService.getCommentList(productId,page,size);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"댓글 조회 성공"));
    }



    @PutMapping("/user/comments/{commentId}")
    @Operation(summary = "댓글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentIdResponse.class)))
    public ResponseEntity<?> modifyComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid CommentModifyRequest commentModifyRequest,
                                           @PathVariable(name = "commentId") Long commentId) {
        Long response =commentService.modify(customUserDetails,commentModifyRequest,commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"댓글 수정 성공"));
    }


    @DeleteMapping("/user/comments/{commentId}")
    @Operation(summary = "댓글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "댓글 수정 성공",content = @Content(schema = @Schema(implementation = CommentStringResponse.class)))
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable(name = "commentId") Long commentId){
        commentService.delete(customUserDetails,commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("댓글 삭제 성공"));
    }
}

@Schema(description = "댓글 응답 Id")
class CommentIdResponse extends CustomResponse<Long>{};

@Schema(description = "댓글 응답 String")
class CommentStringResponse extends CustomResponse<String>{};

@Schema(description = "댓글 목록 응답")
class CommentListResponse extends CustomResponse<CustomPageResponse<CommentResponse>>{};


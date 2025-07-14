package somt.somt.domain.comment.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/user/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid CommentRequest commentRequest) {
        Long id = commentService.create(customUserDetails,commentRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomResponse<>(true, "댓글 생성 성공" , "commentId",id));
    }

    @GetMapping("/public/comments/{productId}")
    public ResponseEntity<?> getCommentList(@PathVariable(name = "productId") Long productId,
                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                            @RequestParam(name = "size",defaultValue = "10") int size){
        Map<String,Object> response = commentService.getCommentList(productId,page,size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse<>(true,"댓글 조회 성공","commentData",response));
    }

//    @GetMapping("/user/comments/{commentId}/replies")
//    public ResponseEntity<?> getSonCommentList(@PathVariable(name = "commentId") Long commentId){
//        List<CommentResponse> response = commentService.getSonCommentList(commentId);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


    @PutMapping("/user/comments/{commentId}")
    public ResponseEntity<?> modifyComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid CommentModifyRequest commentModifyRequest,
                                           @PathVariable(name = "commentId") Long commentId) {
        Long commentId2 =commentService.modify(customUserDetails,commentModifyRequest,commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse<>(true,"댓글 수정 성공","commentId",commentId2));
    }


    @DeleteMapping("/user/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable(name = "commentId") Long commentId){
        commentService.delete(customUserDetails,commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse<>(true,"댓글 삭제 성공","Not data",null));    }
}

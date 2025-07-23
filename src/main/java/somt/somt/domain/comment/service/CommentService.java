package somt.somt.domain.comment.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.comment.dto.CommentModifyRequest;
import somt.somt.domain.comment.dto.CommentRequest;
import somt.somt.domain.comment.dto.CommentResponse;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.comment.repository.CommentRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final MemberService memberService;

    @Transactional
    public Long create(CustomUserDetails customUserDetails, CommentRequest commentRequest) {
        Product product = productService.getProduct(commentRequest.getProductId());
        Member member = memberService.getMember(customUserDetails.getMemberId());

        Comment comment;

        if(commentRequest.getProductId()==null) {
            comment = new Comment(member, product, commentRequest.getContent(), commentRequest.getGrade());
        }else{
            comment = new Comment(member, product, commentRequest.getContent(), commentRequest.getGrade(),commentRequest.getParentId());
        }
        comment = commentRepository.save(comment);
        return comment.getId();
    }

    public CustomPageResponse<CommentResponse> getCommentList(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<Comment> pages = commentRepository.findByProductIdAndParentIdIsNull(productId,pageable);

        List<CommentResponse> commentResponseList = pages.getContent().stream()
                .map(comment -> {
                    if(commentRepository.existsById(comment.getId())){
                        List<Comment> tmp = commentRepository.findByParentId(comment.getId());
                        List<CommentResponse> tmp2 = tmp.stream()
                                .map(CommentResponse::new)
                                .toList();
                        return new CommentResponse(comment,tmp2);
                    }else{
                        return new CommentResponse(comment);
                    }
                })
                .toList();

        return CustomPageResponse.of(
                commentResponseList,
                pages.getTotalPages(),
                pages.getTotalElements(),
                pages.getNumber()
        );
    }



    @Transactional
    public Long modify(CustomUserDetails customUserDetails, CommentModifyRequest commentModifyRequest, Long commentId) {
        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if(!comment.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.modify(commentModifyRequest);
        return comment.getId();
    }



    @Transactional
    public void delete(CustomUserDetails customUserDetails, Long commentId) {
        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if(!comment.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);

    }
}

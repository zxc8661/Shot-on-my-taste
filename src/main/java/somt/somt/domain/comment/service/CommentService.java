package somt.somt.domain.comment.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
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
    public void create(CustomUserDetails customUserDetails, CommentRequest commentRequest) {
        Product product = productService.getProduct(commentRequest.getProductId());
        Member member = memberService.getMember(customUserDetails.getMemberId());

        Comment comment;

        if(commentRequest.getProductId()==null) {
            comment = new Comment(member, product, commentRequest.getContent(), commentRequest.getGrade());
        }else{
            comment = new Comment(member, product, commentRequest.getContent(), commentRequest.getGrade(),commentRequest.getParentId());
        }
        commentRepository.save(comment);
    }

    public Map<String, Object> getCommentList(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<Comment> pages = commentRepository.findByProductIdAndParentIdIsNull(productId,pageable);

        List<CommentResponse> list = pages.stream()
                .map(c->{
                    return new CommentResponse(
                            c.getId(),
                            c.getMember().getId(),
                            c.getMember().getNickName(),
                            c.getContent(),
                            c.getModifyAt()
                    );
                })
                .toList();

        Map<String,Object> response = new HashMap<>();
        response.put("content",list);
        response.put("totalPageCount",pages.getTotalPages());
        response.put("totalElementCount",pages.getTotalElements());
        response.put("currentPage",pages.getNumber());


        return response;
    }

    public List<CommentResponse> getSonCommentList(Long commentId) {
        return commentRepository.findByParentId(commentId)
                .stream()
                .map(p->new CommentResponse(
                        p.getId(),
                        p.getMember().getId(),
                        p.getMember().getNickName(),                        p.getContent(),
                        p.getModifyAt()
                        ))
                .toList();


    }

    @Transactional
    public void modify(CustomUserDetails customUserDetails, CommentRequest commentRequest, Long commentId) {
        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        if(!comment.getMember().getId().equals(customUserDetails.getMemberId())){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.modify(commentRequest);
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

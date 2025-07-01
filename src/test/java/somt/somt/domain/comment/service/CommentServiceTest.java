package somt.somt.domain.comment.service;

import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.comment.dto.CommentRequest;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.comment.repository.CommentRepository;
import somt.somt.domain.comment.dto.CommentResponse;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("CommentService 통합 테스트")
class CommentServiceIntegrationTest {

    @Autowired CommentService      commentService;
    @Autowired CommentRepository   commentRepository;
    @Autowired MemberRepository    memberRepository;
    @Autowired ProductRepository   productRepository;

    private Member             member;
    private Product            product;
    private CustomUserDetails  userDetails;

    @BeforeEach
    void setUp() {
        // 1) 회원 저장
        member = Member.create("userA", "pw", "nickA", "userA@example.com", "ROLE_USER");
        member = memberRepository.save(member);

        // 2) 상품 저장
        product = Product.create(
               new ProductRequest(
                        "testProduct",
                        new BigDecimal("1000"),
                        10,
                        "테스트 상품",
                        List.of("장르1","장르2")
                )
        );
        product = productRepository.save(product);

        // 3) CustomUserDetails 준비
        userDetails = new CustomUserDetails(
                new CustomUserData(
                        member.getId(),
                        member.getUserName(),
                        member.getRole(),
                        member.getPassword(),
                        member.getNickName()
                )
        );
    }

    @Test
    @DisplayName("create(): 최상위 댓글이 저장된다")
    void create_rootComment() {
        CommentRequest req = new CommentRequest(
                product.getId(), "hello", 5, null
        );

        commentService.create(userDetails, req);

        List<Comment> all = commentRepository.findAll();
        assertThat(all)
                .hasSize(1)
                .first()
                .satisfies(c -> {
                    assertThat(c.getContent()).isEqualTo("hello");
                    assertThat(c.getGrade()).isEqualTo(5);
                    assertThat(c.getParentId()).isNull();
                });
    }

    @Test
    @DisplayName("create(): 답글 생성 시 ParentId가 저장된다")
    void create_childComment() {
        // 루트 댓글
        commentService.create(userDetails, new CommentRequest(
                product.getId(), "root", 4, null
        ));
        Comment parent = commentRepository.findAll().get(0);

        // 답글
        commentService.create(userDetails, new CommentRequest(
                product.getId(), "reply", 3, parent.getId()
        ));

        List<Comment> all = commentRepository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all.stream().filter(c -> c.getParentId()!=null))
                .hasSize(1)
                .first()
                .satisfies(c -> assertThat(c.getParentId()).isEqualTo(parent.getId()));
    }

    @Test
    @DisplayName("getCommentList(): 페이징된 댓글 목록 및 메타데이터 반환")
    void getCommentList_paging() {
        // 15개의 루트 댓글 생성
        for (int i = 0; i < 15; i++) {
            commentService.create(userDetails,
                    new CommentRequest(product.getId(), "c"+i, i%5+1, null));
        }

        Map<String,Object> res = commentService.getCommentList(
                product.getId(), 0, 10
        );

        @SuppressWarnings("unchecked")
        List<CommentResponse> content =
                (List<CommentResponse>) res.get("content");

        assertThat(content).hasSize(10);
        assertThat(res.get("totalPageCount")).isEqualTo(2);
        assertThat(res.get("totalElementCount")).isEqualTo(15L);
        assertThat(res.get("currentPage")).isEqualTo(0);
    }

    @Test
    @DisplayName("getSonCommentList(): 특정 댓글의 답글만 반환")
    void getSonCommentList_onlyReplies() {
        // 루트 댓글 + 답글
        commentService.create(userDetails,
                new CommentRequest(product.getId(), "root", 5, null));
        Comment parent = commentRepository
                .findByProductIdAndParentIdIsNull(
                        product.getId(), PageRequest.of(0,10))
                .getContent().get(0);

        commentService.create(userDetails,
                new CommentRequest(product.getId(), "reply", 4, parent.getId()));

        List<CommentResponse> replies =
                commentService.getSonCommentList(parent.getId());

        assertThat(replies).hasSize(1);
        assertThat(replies.get(0).getContent()).isEqualTo("reply");
    }

    @Test
    @DisplayName("modify(): 본인이 작성한 댓글은 수정된다")
    void modify_ownComment() {
        commentService.create(userDetails,
                new CommentRequest(product.getId(), "old", 2, null));
        Comment saved = commentRepository.findAll().get(0);

        CommentRequest update = new CommentRequest(
                product.getId(), "new", 5, null
        );
        commentService.modify(userDetails, update, saved.getId());

        Comment updated = commentRepository.findById(saved.getId()).get();
        assertThat(updated.getContent()).isEqualTo("new");
        assertThat(updated.getGrade()).isEqualTo(5);
    }

    @Test
    @DisplayName("modify(): 타인의 댓글 수정 시 ACCESS_DENIED 예외")
    void modify_notOwner_throws() {
        commentService.create(userDetails,
                new CommentRequest(product.getId(), "orig", 3, null));
        Comment saved = commentRepository.findAll().get(0);

        var other = new CustomUserDetails(new CustomUserData(
                999L, "u","ROLE_USER","pw","nick"
        ));

        assertThatThrownBy(() ->
                commentService.modify(other,
                        new CommentRequest(product.getId(),"x",1,null),
                        saved.getId())
        ).isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test
    @DisplayName("delete(): 본인이 작성한 댓글은 삭제된다")
    void delete_ownComment() {
        commentService.create(userDetails,
                new CommentRequest(product.getId(), "tbd", 1, null));
        Comment saved = commentRepository.findAll().get(0);

        commentService.delete(userDetails, saved.getId());
        assertThat(commentRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    @DisplayName("delete(): 타인의 댓글 삭제 시 ACCESS_DENIED 예외")
    void delete_notOwner_throws() {
        commentService.create(userDetails,
                new CommentRequest(product.getId(), "orig", 3, null));
        Comment saved = commentRepository.findAll().get(0);

        var other = new CustomUserDetails(new CustomUserData(
                999L, "u","ROLE_USER","pw","nick"
        ));

        assertThatThrownBy(() ->
                commentService.delete(other, saved.getId())
        ).isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test
    @DisplayName("modify/delete(): 없는 댓글 접근 시 NOT_FOUND_COMMENT 예외")
    void notFound_throws() {
        var ex1 = assertThatThrownBy(() ->
                commentService.modify(userDetails,
                        new CommentRequest(product.getId(),"x",1,null),
                        12345L))
                .isInstanceOf(CustomException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_COMMENT);

        var ex2 = assertThatThrownBy(() ->
                commentService.delete(userDetails, 54321L))
                .isInstanceOf(CustomException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_COMMENT);
    }
}

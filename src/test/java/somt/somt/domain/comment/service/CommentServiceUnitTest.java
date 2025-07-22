package somt.somt.domain.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.comment.dto.CommentRequest;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.comment.repository.CommentRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    ProductService productService;
    @Mock
    MemberService memberService;
    @InjectMocks
    CommentService commentService;


    @Test
    public void create(){
        //given
        CustomUserDetails mockCustomUserDetails =
                new CustomUserDetails(
                        new CustomUserData(
                                1L,
                                "member",
                                "ADMIN",
                                "",
                                "nickname")
                );
        CommentRequest MockCommentRequest = new CommentRequest(10L,"comment",5,null);

        Product mockProduct = new Product(20L,"mock",new BigDecimal("1000"),10,"mock");
        Member mockMember = new Member(mockCustomUserDetails);
        Comment comment = new Comment(30L,mockProduct,mockMember,"mock",5,null,new HashSet<>(), LocalDateTime.now(),LocalDateTime.now());


        when(productService.getProduct(anyLong())).thenReturn(mockProduct);
        when(memberService.getMember(anyLong())).thenReturn(mockMember);
        when(commentRepository.save(any())).thenReturn(comment);

        /*
        Mockito에서 제공하는 유틸클래스, mock 객체의 메서드가 호출될 대 전달된 인자를 저장할 수 있게 해준다
         */
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        //when
        Long result = commentService.create(mockCustomUserDetails,MockCommentRequest);



        //then
        assertThat(result).isEqualTo(30L);
        verify(productService).getProduct(anyLong());
        verify(memberService).getMember(anyLong());
        verify(commentRepository).save(commentArgumentCaptor.capture());

        Comment captor = commentArgumentCaptor.getValue();

        assertThat(captor.getContent()).isEqualTo("comment");
    }

    @Test
    public void getCommentList(){
        //given
        //when
        //then
    }




}

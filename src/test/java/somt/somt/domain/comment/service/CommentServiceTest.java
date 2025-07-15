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


}

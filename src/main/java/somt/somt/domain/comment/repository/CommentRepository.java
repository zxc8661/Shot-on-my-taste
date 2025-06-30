package somt.somt.domain.comment.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.comment.dto.CommentResponse;
import somt.somt.domain.comment.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByProductId(Long productId, Pageable pageable);

    Page<Comment> findByProductIdAndParentIdIsNull(Long productId, Pageable pageable);

    List<Comment> findByParentId(Long id);
}

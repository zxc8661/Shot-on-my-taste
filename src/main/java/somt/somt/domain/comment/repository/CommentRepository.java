package somt.somt.domain.comment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}

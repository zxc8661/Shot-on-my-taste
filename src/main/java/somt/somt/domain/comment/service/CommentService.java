package somt.somt.domain.comment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ProductRepository productRepository;
}

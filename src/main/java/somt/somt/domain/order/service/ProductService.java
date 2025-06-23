package somt.somt.domain.order.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
}

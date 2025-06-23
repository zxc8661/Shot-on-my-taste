package somt.somt.domain.cart.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.domain.cart.repository.CartRepository;
import somt.somt.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
}

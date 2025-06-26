package somt.somt.domain.cart.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartResponse;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.repository.CartRepository;
import somt.somt.domain.product.dto.reponse.ProductDTO;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;
import somt.somt.domain.product.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public List<CartResponse> getCarts(CustomUserDetails userDetails) {
        List<Cart> carts = cartRepository.findAllByMemberId(userDetails.getMemberId());

        List<CartResponse> cartResponses = carts.stream()
                .map(CartResponse::new)
                .toList();
        return cartResponses;
    }
}

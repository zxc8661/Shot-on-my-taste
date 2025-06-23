package somt.somt.domain.notification.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ProductRepository productRepository;
}

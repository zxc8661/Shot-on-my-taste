package somt.somt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/products/manage")
    public String productManage() {
        return "products/manage";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart/cart";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders/history";
    }

    @GetMapping("/orders/checkout")
    public String orderCheckout() {
        return "orders/checkout";
    }

    @GetMapping("/orders/{orderId}")
    public String orderDetail() {
        return "orders/detail";
    }
}

package somt.somt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import somt.somt.domain.product.controller.ProductController;
import somt.somt.domain.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProductController controller;


    @GetMapping("/")
    public String index(Model model) {

        return "index";
    }

}
package com.omar.remittance_management.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {
    @GetMapping("/api")
    public String redirectToSwagger() {

        return "redirect:/swagger-ui.html";

    }
}

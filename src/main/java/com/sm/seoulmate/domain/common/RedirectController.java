package com.sm.seoulmate.domain.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/android")
    public String getAndroidList() {
        return "forward:/android.html";
    }
}

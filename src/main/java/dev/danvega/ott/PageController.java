package dev.danvega.ott;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("")
    public String home() {
        return "index";
    }

    @GetMapping("/ott/sent")
    public String ottSent() {
        return "sent";
    }

}

package web2.tec.laboratorio_2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() { return "home"; }

    @GetMapping("/s")
    public String s() { return "principleS"; }

    @GetMapping("/o")
    public String o() { return "principleO"; }

    @GetMapping("/l")
    public String l() { return "principleL"; }

    @GetMapping("/i")
    public String i() { return "principleI"; }

    @GetMapping("/d")
    public String d() { return "principleD"; }


}


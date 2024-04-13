package com.coinflip.dungeon.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TemplateController {

    @GetMapping
    String getPeople(Model model){
        model.addAttribute("something", "coming from the controller");
        return "index";
    }
}


package com.example.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {


    @GetMapping("/index")
    public String inde(){
        return "index";
    }

}

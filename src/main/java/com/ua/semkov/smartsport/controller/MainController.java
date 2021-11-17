package com.ua.semkov.smartsport.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Semkov.S
 */
@Controller
@AllArgsConstructor
public class MainController {


    @GetMapping("/home")
    ModelAndView home() {
        return new ModelAndView("/main/home");
    }


}

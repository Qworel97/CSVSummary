package com.shevchenko.csvsummary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }
}


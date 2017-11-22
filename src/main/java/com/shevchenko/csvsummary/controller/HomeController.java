package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.component.impl.ParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class HomeController {

    private static final String TOKEN = "SFN_TOKEN";

    private Parser parser;

    @Autowired
    public HomeController(Parser parser) {
        this.parser = parser;
    }

    @GetMapping
    public String index(@CookieValue(TOKEN) String token) {
        return "index";
    }
}


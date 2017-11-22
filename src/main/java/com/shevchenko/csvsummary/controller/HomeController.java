package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class HomeController {

    private static final String TOKEN = "SFN_TOKEN";

    private Parser parser;

    @Value("${temporary.folder}")
    private String uploadFolder;

    @Autowired
    public HomeController(Parser parser) {
        this.parser = parser;
    }

    @GetMapping
    public String index(@CookieValue(name = TOKEN, defaultValue = "") String token, Model model) {
        if (!token.isEmpty()) {
            File file = Paths.get(uploadFolder + token).toFile();
            try {
                model.addAttribute(ModelAttributeNames.HEADERS.getName(), parser.parseHeaders(file));
            } catch (IOException e) {
                model.addAttribute(ModelAttributeNames.ERROR.getName(), Messages.ERROR_WHILE_PARSING.getText());
            }
        }
        return "index";
    }
}


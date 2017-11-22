package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.util.CookieUtils;
import com.shevchenko.csvsummary.util.NameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class SummaryController {

    private Parser parser;

    @Autowired
    public SummaryController(Parser parser) {
        this.parser = parser;
    }

    @PostMapping("/summarize")
    public String singleFileUpload() {

        return "redirect:/index";
    }
}

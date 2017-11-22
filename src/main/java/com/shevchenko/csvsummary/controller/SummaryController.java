package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class SummaryController {

    private static final String UPLOADED_FOLDER = "/temp//";

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private static final String ERROR_MODEL_ATTRIBUTE_NAME = "error";
    private static final String SUMMARY_MODEL_ATTRIBUTE_NAME = "summary";

    private static final String ERROR_WHILE_PARSING = "Error while uploading";
    private Parser parser;

    @Autowired
    public SummaryController(Parser parser) {
        this.parser = parser;
    }

    @PostMapping("/summarize")
    public String getSummary(@CookieValue(TOKEN) String token, @RequestParam String header,
                             RedirectAttributes redirectAttributes, HttpServletResponse response) {
        File file = new File(Paths.get(UPLOADED_FOLDER + token).toUri());
        try {
            redirectAttributes.addFlashAttribute(SUMMARY_MODEL_ATTRIBUTE_NAME, parser.summarize(file, header));
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute(ERROR_MODEL_ATTRIBUTE_NAME, ERROR_WHILE_PARSING);
        }
        return "redirect:/index";
    }
}

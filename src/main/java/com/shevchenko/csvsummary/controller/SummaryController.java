package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import com.shevchenko.csvsummary.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private Parser parser;

    @Value("${temporary.folder}")
    private String uploadFolder;

    @Autowired
    public SummaryController(Parser parser) {
        this.parser = parser;
    }

    @PostMapping("/summarize")
    public String getSummary(@CookieValue(name = TOKEN, defaultValue = "") String token, @RequestParam String header,
                             RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (!token.isEmpty()) {
            File file = Paths.get(uploadFolder + token).toFile();
            try {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.SUMMARY.getName(), parser.summarize(file, header));
                response.addCookie(CookieUtils.generateCookie(TOKEN, token, MAX_AGE));
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.ERROR_WHILE_PARSING.getText());
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.FILE_CONTAINS_NOT_DOUBLE_ERROR.getText());
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NOT_EXISTING_HEADER.getText());
            }
        } else {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NO_COOKIE_ERROR.getText());
        }
        return "redirect:/";
    }
}

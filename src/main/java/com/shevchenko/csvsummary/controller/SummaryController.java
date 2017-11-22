package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Cache;
import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import com.shevchenko.csvsummary.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class SummaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryController.class);

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private Parser parser;
    private Cache cache;

    @Autowired
    public SummaryController(Parser parser, Cache cache) {
        this.parser = parser;
        this.cache = cache;
    }

    @PostMapping("/summarize")
    public String getSummary(@CookieValue(name = TOKEN, defaultValue = "") String token, @RequestParam String header,
                             RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (!token.isEmpty()) {
            try {
                File file = cache.get(token);
                LOGGER.debug("File was loaded from cache -> {}", token);
                redirectAttributes.addFlashAttribute(ModelAttributeNames.SUMMARY.getName(), parser.summarize(file, header));
                response.addCookie(CookieUtils.generateCookie(TOKEN, token, MAX_AGE));
                LOGGER.debug("Cookie was set for -> {}", token);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.ERROR_WHILE_PARSING.getText());
                LOGGER.debug("Parse problem -> {}", token);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.FILE_CONTAINS_NOT_DOUBLE_ERROR.getText());
                LOGGER.debug("Wrong data -> {}", token);
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NOT_EXISTING_HEADER.getText());
                LOGGER.debug("Wrong header -> {}", token);
            }
        } else {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NO_COOKIE_ERROR.getText());
            LOGGER.debug("No file in session");
        }
        return "redirect:/";
    }
}

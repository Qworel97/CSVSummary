package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Cache;
import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import com.shevchenko.csvsummary.util.CookieUtils;
import com.shevchenko.csvsummary.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private Parser parser;
    private Cache cache;

    @Value("${temporary.folder}")
    private String uploadFolder;

    @Autowired
    public UploadController(Parser parser, Cache cache) {
        this.parser = parser;
        this.cache = cache;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NO_FILE_SELECTED_ERROR.getText());
            LOGGER.debug("No file");
        }
        try {
            byte[] bytes = file.getBytes();
            String systemFileName = NameUtils.generateUniqueFileName(file.getOriginalFilename());
            Path path = Paths.get(uploadFolder + systemFileName);
            File tempFile = Files.write(path, bytes).toFile();
            if (parser.isValid(tempFile)) {
                cache.put(tempFile);
                LOGGER.debug("File was saved -> {}", systemFileName);
                redirectAttributes.addFlashAttribute(ModelAttributeNames.MESSAGE.getName(), Messages.SUCCESSFULLY_UPLOADED_FILE.getText());
                response.addCookie(CookieUtils.generateCookie(TOKEN, systemFileName, MAX_AGE));
                LOGGER.debug("Cookie was set for -> {}", systemFileName);
            } else {
                tempFile.delete();
                redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.WRONG_FILE_DATA.getText());
                LOGGER.debug("Wrong data -> {}", systemFileName);
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.ERROR_WHILE_UPLOADING.getText());
            LOGGER.debug("Error while uploading");
        }

        return "redirect:/";
    }

}


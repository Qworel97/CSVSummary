package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.util.CookieUtils;
import com.shevchenko.csvsummary.util.NameUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class UploadController {

    private static final String UPLOADED_FOLDER = "/temp//";

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private static final String ERROR_MODEL_ATTRIBUTE_NAME = "error";
    private static final String MESSAGE_MODEL_ATTRIBUTE_NAME = "message";
    private static final String HEADERS_MODEL_ATTRIBUTE_NAME = "headers";

    private static final String NO_FILE_SELECTED_ERROR = "No file selected";
    private static final String ERROR_WHILE_UPLOADING = "Error while uploading";
    private static final String FILE_WAS_NOT_FOUND = "You file was not found. Maybe it expired";

    private static final String SUCCESSFULLY_UPLOADED_FILE = "You have successfully uploaded file";

    private Parser parser;

    @Autowired
    public UploadController(Parser parser) {
        this.parser = parser;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MODEL_ATTRIBUTE_NAME, NO_FILE_SELECTED_ERROR);
        }
        try {
            byte[] bytes = file.getBytes();
            String systemFileName = NameUtils.generateUniqueFileName(file.getOriginalFilename());
            Path path = Paths.get(UPLOADED_FOLDER + systemFileName);
            File systemFile = new File(Files.write(path, bytes).toUri());
            redirectAttributes.addFlashAttribute(MESSAGE_MODEL_ATTRIBUTE_NAME, SUCCESSFULLY_UPLOADED_FILE);
            redirectAttributes.addFlashAttribute(HEADERS_MODEL_ATTRIBUTE_NAME, parser.parseHeaders(systemFile));
            response.addCookie(CookieUtils.generateCookie(TOKEN, systemFileName, MAX_AGE));
        }
        catch (FileNotFoundException e) {
            redirectAttributes.addFlashAttribute(ERROR_MODEL_ATTRIBUTE_NAME, FILE_WAS_NOT_FOUND);
        }
        catch (IOException e) {
            redirectAttributes.addFlashAttribute(ERROR_MODEL_ATTRIBUTE_NAME, ERROR_WHILE_UPLOADING);
        }

        return "redirect:/index";
    }

}


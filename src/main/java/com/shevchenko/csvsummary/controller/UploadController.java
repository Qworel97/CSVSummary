package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.util.CookieUtils;
import com.shevchenko.csvsummary.util.NameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private static final String NO_FILE_SELECTED_ERROR = "No file selected";
    private static final String ERROR_WHILE_UPLOADING = "Error while uploading";

    private static final String SUCCESSFULLY_UPLOADED_FILE = "You have successfully uploaded file";

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam MultipartFile file,
                                   Model model, HttpServletResponse response) {
        if (file.isEmpty()) {
            model.addAttribute(ERROR_MODEL_ATTRIBUTE_NAME, NO_FILE_SELECTED_ERROR);
        }
        try {

            byte[] bytes = file.getBytes();
            String systemFileName = NameUtils.generateUniqueFileName(file.getOriginalFilename());
            Path path = Paths.get(UPLOADED_FOLDER + systemFileName);
            Files.write(path, bytes);

            model.addAttribute(MESSAGE_MODEL_ATTRIBUTE_NAME, SUCCESSFULLY_UPLOADED_FILE);
            response.addCookie(CookieUtils.generateCookie(TOKEN, systemFileName, MAX_AGE));

        } catch (IOException e) {
            model.addAttribute(ERROR_MODEL_ATTRIBUTE_NAME, ERROR_WHILE_UPLOADING);
        }

        return "redirect:/index";
    }

}


package com.shevchenko.csvsummary.controller;

import com.shevchenko.csvsummary.component.Parser;
import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import com.shevchenko.csvsummary.util.CookieUtils;
import com.shevchenko.csvsummary.util.NameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dmytro_Shevchenko4
 */
@Controller
public class UploadController {

    private static final String TOKEN = "SFN_TOKEN";
    private static final int MAX_AGE = 1000;

    private Parser parser;

    @Value("${temporary.folder}")
    private String uploadFolder;

    @Autowired
    public UploadController(Parser parser) {
        this.parser = parser;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.NO_FILE_SELECTED_ERROR.getText());
        }
        try {
            byte[] bytes = file.getBytes();
            String systemFileName = NameUtils.generateUniqueFileName(file.getOriginalFilename());
            Path path = Paths.get(uploadFolder + systemFileName);
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute(ModelAttributeNames.MESSAGE.getName(), Messages.SUCCESSFULLY_UPLOADED_FILE.getText());
            response.addCookie(CookieUtils.generateCookie(TOKEN, systemFileName, MAX_AGE));
        } catch (FileNotFoundException e) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.FILE_WAS_NOT_FOUND.getText());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.ERROR_WHILE_UPLOADING.getText());
        }

        return "redirect:/";
    }

}


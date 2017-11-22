package com.shevchenko.csvsummary.handler;

import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartError(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.FILE_IS_TOO_BIG.getText());
        LOGGER.debug("File bigger than max");
        return "redirect:/";
    }

}
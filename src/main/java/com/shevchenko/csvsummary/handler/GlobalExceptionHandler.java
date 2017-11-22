package com.shevchenko.csvsummary.handler;

import com.shevchenko.csvsummary.entity.Messages;
import com.shevchenko.csvsummary.entity.ModelAttributeNames;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartError(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ModelAttributeNames.ERROR.getName(), Messages.FILE_IS_TOO_BIG.getText());
        return "redirect:/";
    }

}
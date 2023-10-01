package com.kino_cms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            return new ModelAndView("error/error404")
                    .addObject("errorMessage", "This Page Not Found")
                    .addObject("errorCode", status.toString());
        }
        return new ModelAndView("error/error404")
                .addObject("errorMessage", "Oops, something went wrong. <br/>Soryanchik :)")
                .addObject("errorCode", status.toString());

    }
}

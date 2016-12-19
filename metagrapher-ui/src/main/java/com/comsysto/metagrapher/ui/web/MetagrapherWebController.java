package com.comsysto.metagrapher.ui.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MetagrapherWebController {


    @RequestMapping("/")
    public ModelAndView indexHtml(HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView("metagrapher/index");
        // This variable is used to build the abstract resource urls, this can be done in a more sophisticated way ...
        modelAndView.addObject("applicationBasePath", request.getContextPath());
        return modelAndView;
    }



}

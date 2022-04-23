package com.qbros.uithymeleaf.controllers;

import com.qbros.uithymeleaf.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Slf4j
public abstract class AbsController {

    protected final String ERROR_KEY = "error";

    @Autowired
    protected State sessionState;

    protected void addErrorMsg(Model model, Throwable throwable) {
        log.warn("Error In Controller {}", throwable.getLocalizedMessage(), throwable);
        model.addAttribute(ERROR_KEY,
                throwable.getLocalizedMessage() != null ? throwable.getLocalizedMessage() : "err");
    }
}

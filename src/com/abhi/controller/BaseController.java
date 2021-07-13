package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.view.ViewFactory;

public abstract class BaseController {

    protected EmailManager emailManager;
    protected ViewFactory viewFactory;
    private final String fxmlName;

    public BaseController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName(){
        return fxmlName;
    }
}

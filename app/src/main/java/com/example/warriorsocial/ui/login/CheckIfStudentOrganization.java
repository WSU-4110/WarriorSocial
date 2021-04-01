package com.example.warriorsocial.ui.login;

import android.content.Context;

public class CheckIfStudentOrganization implements Handler {
    Handler nextHandler;

    @Override
    public String sendErrorMessage() {
        return null;
    }

    @Override
    public void setNextHandler(Handler h) {
        nextHandler = h;
    }

    @Override
    public void handle(Request r, Context mContext) {
        if (nextHandler != null) {
            nextHandler.handle(r, mContext);
        }
    }
}

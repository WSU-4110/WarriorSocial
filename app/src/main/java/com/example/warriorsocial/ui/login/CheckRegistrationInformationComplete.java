package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.text.TextUtils;

public class CheckRegistrationInformationComplete implements Handler {
    Handler nextHandler = null;

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
        if(TextUtils.isEmpty(r.useremail.getText().toString())){
            r.useremail.setError("Email is required!");
            return;
        }

        if(TextUtils.isEmpty(r.userpassword.getText().toString())){
            r.userpassword.setError("Password is required!");
            return;
        }

        //Check if password length has 5 or more characters
        if(r.userpassword.getText().toString().length() < 5){
            r.userpassword.setError("Password must have 5 or more characters!");
            return;
        }

        if (nextHandler != null) {
            nextHandler.handle(r, mContext);
        }
    }
}

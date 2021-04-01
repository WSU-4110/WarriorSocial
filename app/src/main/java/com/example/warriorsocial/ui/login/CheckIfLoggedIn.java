package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.content.Intent;

import com.example.warriorsocial.BottomActivity;

public class CheckIfLoggedIn implements Handler {
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
        //Check if user is already logged in
        if(r.fAuth.getCurrentUser() != null){
            mContext.startActivity(new Intent(mContext, BottomActivity.class));
        }
        if (nextHandler != null) {
            nextHandler.handle(r, mContext);
        }
    }
}

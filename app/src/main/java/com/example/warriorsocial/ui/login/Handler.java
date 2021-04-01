package com.example.warriorsocial.ui.login;

import android.content.Context;

public interface Handler {
    String sendErrorMessage();
    void setNextHandler(Handler h);
    void handle(Request r, Context mContext);
}

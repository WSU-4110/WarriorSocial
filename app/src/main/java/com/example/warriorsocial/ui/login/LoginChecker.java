package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import com.example.warriorsocial.R;

public class LoginChecker {
    public LoginChecker() {}

    public void checkIfLoginFormEmpty(LoginFormState loginFormState){
        if (loginFormState == null) {
            return;
        }

    }

    public void checkUserNameError(EditText username, LoginFormState loginFormState){
        if (loginFormState.getUsernameError() != null) {
            username.setError((loginFormState.getUsernameError()).toString());
        }
    }

    public void checkPasswordError(EditText password, LoginFormState loginFormState){
        if(loginFormState.getPasswordError() != null){
            password.setError((loginFormState.getPasswordError()).toString());
        }
    }

}

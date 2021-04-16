package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.warriorsocial.BottomActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationChecker {
    public RegistrationChecker() {}

    public boolean checkIfUserLoggedIn(FirebaseAuth fAuth, Context mContext) {
        if(fAuth.getCurrentUser() != null){
            mContext.startActivity(new Intent(mContext, BottomActivity.class));
            //finish();
            return false;
        }
        return true;
    }

    public boolean checkEmailEmpty(EditText email, Context mContext) {
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Email is required!");
            return false;
        }
        return true;
    }

    public boolean checkPasswordEmpty(EditText password, Context mContext) {
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Password is required!");
            return false;
        }
        return true;
    }


    public boolean checkPasswordLength(EditText password, Context mContext) {
        //Check if password length has 5 or more characters
        if(password.getText().toString().length() < 5){
            password.setError("Password must have 5 or more characters!");
            return false;
        }
        return true;
    }

}

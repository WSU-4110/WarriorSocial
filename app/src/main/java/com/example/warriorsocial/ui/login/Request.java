package com.example.warriorsocial.ui.login;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Request {
    protected EditText useremail;
    protected EditText userpassword;
    FirebaseAuth fAuth;

    public Request(EditText useremail, EditText userpassword, FirebaseAuth fAuth) {
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.fAuth = fAuth;
    }
}

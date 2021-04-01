package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.warriorsocial.BottomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegistrationHandler implements Handler{
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
    public void handle(Request r, final Context mContext) {
        String emailTxt = r.useremail.getText().toString();
        String passwordTxt = r.userpassword.getText().toString();
        r.fAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(mContext, "User created.", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, BottomActivity.class));
                }
                else{
                    Toast.makeText(mContext,"Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (nextHandler != null) {
            nextHandler.handle(r, mContext);
        }
    }
}

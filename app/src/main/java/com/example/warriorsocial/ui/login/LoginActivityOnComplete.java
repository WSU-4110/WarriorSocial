package com.example.warriorsocial.ui.login;

import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

import com.example.warriorsocial.BottomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityOnComplete extends LoginActivity {
    LoginActivity login = new LoginActivity();

    Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();



public boolean checkSuccess() {
    if (task.isSuccessful()) {
        Toast.makeText(LoginActivityOnComplete.this, "Log in successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), BottomActivity.class));
        System.out.println("~~~~~~~~~~~~~~~~ONCOMPLETE SUCCESS LINE 160~~~~~");
        return true;
    } else {
        Toast.makeText(LoginActivityOnComplete.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        System.out.println("~~~~~~~~~~~~~~~~ONCOMPLETE NOT SUCCESSFUL LINE 160~~~~~");
        return false;
    }
}


}

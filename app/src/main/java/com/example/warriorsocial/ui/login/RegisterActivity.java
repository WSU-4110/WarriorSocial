package com.example.warriorsocial.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.warriorsocial.R;

public class RegisterActivity extends AppCompatActivity {

    //Currently Editing (Alex 2/16/21 1:20 PM)
    private EditText userName;
    private EditText userPassword;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.et_userEmail);
        userPassword = findViewById(R.id.et_userPassword);
        createAccountButton = findViewById(R.id.button_create_account);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = userName.getText().toString();
                String passwordTxt = userPassword.getText().toString();
            }
        });

    }
}
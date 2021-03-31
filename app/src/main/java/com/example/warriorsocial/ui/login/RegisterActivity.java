package com.example.warriorsocial.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.warriorsocial.BottomActivity;
import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private EditText userName;
    private Button createAccountButton;
    FirebaseAuth fAuth;
    ProgressBar loadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.et_userEmail);
        userPassword = findViewById(R.id.et_userPassword);
        userName = findViewById(R.id.et_studentUserName);
        createAccountButton = findViewById(R.id.button_create_account);
        fAuth = FirebaseAuth.getInstance();
        loadingProgressBar = findViewById(R.id.loading);

        //Check if user is already logged in
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), BottomActivity.class));
            finish();

        }

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailTxt = userEmail.getText().toString();
                String passwordTxt = userPassword.getText().toString();
                final String usernameTxt = userName.getText().toString();

                //Check to see if input is empty
                if(TextUtils.isEmpty(emailTxt)){
                    userEmail.setError("Email is required!");
                    return;
                }

                if(TextUtils.isEmpty(passwordTxt)){
                    userPassword.setError("Password is required!");
                    return;
                }

                //Check if password length has 5 or more characters
                if(passwordTxt.length() < 5){
                    userPassword.setError("Password must have 5 or more characters!");
                    return;
                }

                if(TextUtils.isEmpty(usernameTxt)){
                    userName.setError("Username is required!");
                    return;
                }

                //loadingProgressBar.setVisibility(View.VISIBLE);

                //register user
                fAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(usernameTxt, emailTxt);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), BottomActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
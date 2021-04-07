package com.example.warriorsocial.ui.login;

import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginActivityTest {

    //FirebaseAuth fAuth;
    Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();

   /* Looper loop() {
        return null;
    }*/


   /*public Handler mHandler;

    public void run() {
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                // process incoming messages here
            }
        };

        Looper.loop();
    }*/



    LoginActivity login = new LoginActivity();



    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onCreate() {
    //login.onCompleteT(task);


    }
}
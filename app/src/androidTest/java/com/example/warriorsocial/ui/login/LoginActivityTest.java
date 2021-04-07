package com.example.warriorsocial.ui.login;

import android.os.Looper;
import android.text.Editable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class LoginActivityTest {


    //LoginActivityTest lll = new LoginActivityTest();
    private android.text.Editable s;





    public void run() {
        Looper.prepare();

        System.out.println("bblaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    }



   /* @Rule
    public LoginActivityTest mActivityRule =
            new LoginActivityTest(LoginActivity.class);*/

    //FirebaseAuth fAuth;
   // Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();

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



   // public LoginActivityTest(Class<LoginActivity> loginActivityClass) {
    //}


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onCreate() {
    //log.onCompleteT(task);


    }

    @Test
    public void checkTextChanged() {
        LoginActivityTest lll = new LoginActivityTest();
        //char s = 'h';

        lll.run();
        LoginCheckTextChanged log = new LoginCheckTextChanged();
        log.checkTextChanged(s);

        System.out.println("bblaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

     //   fail();

    }
}
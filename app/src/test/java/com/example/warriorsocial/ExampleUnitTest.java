package com.example.warriorsocial;

import com.example.warriorsocial.ui.login.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    LoginActivity lo = new LoginActivity();
    Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();

    /*@Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }*/

    @Test
    public void onCreate() {
        lo.onCompleteT(task);


    }
}
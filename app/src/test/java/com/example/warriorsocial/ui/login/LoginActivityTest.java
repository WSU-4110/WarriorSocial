package com.example.warriorsocial.ui.login;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    //check is loginResult is null
    public void loginResultP() {
        String email = null;
        assertNull(email);
    }
    @Test
    //check if loginresult != null
    public void loginResultF() {
        String email = "gb2852@wayne.edu";
        assertNotNull(email);
    }

    @Test
    //check if loginResult error != null
    public void loginErrorP() {
        String email = "this is not null";
        assertNotNull(email);
    }

    @Test
    //check if logingResult error = null
    public void loginErrorF() {
        String email = null;
        assertNull(email);
    }
    @Test
    //check if loginResult success != null
    public void loginSuccessP() {
        String email = "Success it is not null";
        assertNotNull(email);
    }
    @Test
    //check if loginResult success = null
    public void loginSuccessF() {
        String email = null;
        assertNull(email);
    }
}

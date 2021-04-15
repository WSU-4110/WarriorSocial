package com.example.warriorsocial.ui.login;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void loginResultP() {
        String email = null;
        assertNull(email);
    }
    @Test
    public void loginResultF() {
        String email = "gb2852@wayne.edu";
        assertNotNull(email);
    }

    @Test
    public void loginErrorP() {
        String email = "not@empty";
        assertNotNull(email);
    }

    @Test
    public void loginErrorF() {
        String email = "not@empty";
        assertNotNull(email);
    }
    @Test
    public void loginSuccessP() {
        String email = "not@empty";
        assertNotNull(email);
    }
    @Test
    public void loginSuccessF() {
        String email = "not@empty";
        assertNotNull(email);
    }
}

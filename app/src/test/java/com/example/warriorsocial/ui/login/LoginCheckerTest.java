package com.example.warriorsocial.ui.login;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginCheckerTest {

    @Test
    public void CheckIfLoginFormEmpty() {
        fail();
    }

    @Test
    public void CheckUserNameError() {
        String username = "username";
        assertNotNull(!username.isEmpty());
    }

    @Test
    public void CheckPasswordError() {
        String password = "password";
        assertNotNull(!password.isEmpty());
    }
}
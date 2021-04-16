package com.example.warriorsocial.ui.login;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationCheckerTest {

    @Test
    public void checkIfUserLoggedIn() {
        fail();
    }

    @Test
    public void checkEmailEmpty() {
        String email = "not@empty";
        assertTrue(!email.isEmpty());
    }

    @Test
    public void checkPasswordEmpty() {
        fail();
    }

    @Test
    public void checkPasswordLength() {
        fail();
    }
}
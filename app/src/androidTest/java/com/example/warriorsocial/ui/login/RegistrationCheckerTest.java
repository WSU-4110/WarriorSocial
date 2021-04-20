package com.example.warriorsocial.ui.login;

import android.content.Context;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationCheckerTest {

    private static RegistrationChecker registrationChecker;
    private static User testUser;
    private static FirebaseAuth fAuth;
    private static Context context;

    private static EditText testEmail;
    private static EditText testEmail2;
    private static EditText testEmail3;

    private static EditText testPassword;
    private static EditText testPassword2;
    private static EditText testPassword3;
    private static EditText testPassword4;

    @BeforeClass
    public static void before() {
        // Initiate a context; In this case we need to create a "sample" context for unit testing using InstrumentationRegistry
        // This is necessary as for one of the methods tested, context is required
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);


        // Initialize registrationChecker();
        registrationChecker = new RegistrationChecker();

        // Initialize sample EditTexts
        testEmail = new EditText(context);
        testEmail.setText("not@empty");
        testEmail2 = new EditText(context);
        testEmail2.setText("something@wayne");
        testEmail3 = new EditText(context);
        // no setText for testEmail3 ( should be empty )

        testPassword = new EditText(context);
        testPassword.setText("testing12321");
        testPassword2 = new EditText(context);
        testPassword2.setText("alex");
        testPassword3 = new EditText(context);
        // no setText for testPassword3 ( should be empty )
        testPassword4 = new EditText(context);
        testPassword4.setText("alex5");
        // Initiate testUser for unit tests
        testUser = new User("something", testEmail.getText().toString());

        // Initialize Firebase Auth (Simulate a login)
        fAuth = FirebaseAuth.getInstance();

        fAuth.signInWithEmailAndPassword(testEmail.getText().toString(), testPassword.getText().toString());
    }

    @Test
    public void checkIfUserLoggedIn() {
        // Should return a success (sample user was logged into the system with fAuth)
        // fAuth should log that action and remember the user that was logged in, given the context
        assertTrue(registrationChecker.checkIfUserLoggedIn(fAuth, context));
    }

    @Test
    public void checkEmailEmpty() {
        // Should return a success (testEmail text was initialized)
        assertTrue(registrationChecker.checkEmailEmpty(testEmail));

        // Should return a success (testEmail2 text was initialized)
        assertTrue(registrationChecker.checkEmailEmpty(testEmail2));

        // Should return a failure (testEmail3 text was NOT initialized)
        assertFalse(registrationChecker.checkEmailEmpty(testEmail3));
    }

    @Test
    public void checkPasswordEmpty() {
        // Should return a success (testPassword text was initialized)
        assertTrue(registrationChecker.checkPasswordEmpty(testPassword));

        // Should return a success (testPassword2 text was initialized)
        assertTrue(registrationChecker.checkPasswordEmpty(testPassword2));

        // Should return a failure (testPassword3 text was NOT initialized)
        assertFalse(registrationChecker.checkPasswordEmpty(testPassword3));

        // Should return a success (testPassword4 text was initialized)
        assertTrue(registrationChecker.checkPasswordEmpty(testPassword4));
    }

    @Test
    public void checkPasswordLength() {
        // Should return a success (testPassword text length is > 5)
        assertTrue(registrationChecker.checkPasswordLength(testPassword));

        // Should return a failure (testPassword2 text length is < 5)
        assertFalse(registrationChecker.checkPasswordLength(testPassword2));

        // Should return a failure (testPassword3 text was NOT initialized)
        assertFalse(registrationChecker.checkPasswordLength(testPassword3));

        // Should return a success (testPassword4 text length is > 5)
        assertTrue(registrationChecker.checkPasswordLength(testPassword4));
    }
}
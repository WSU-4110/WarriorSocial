package com.example.warriorsocial.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

// This test class tests the shared preferences functionality that saves user selected settings
// from SettingsFragment in internal storage to be accessed again later.
// In this case, we are only using booleans because each of these methods are linked to a switch
// that the user either turns on or off.

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest  {

    public static final String REPLIES_POST = "REPLIES_POST";
    public static final String REPLIES_COMMENTS = "REPLIES_COMMENTS";
    public static final String ALL_NOTIFICATIONS = "ALL_NOTIFICATIONS";
    public static final boolean BOOL_DEFAULT = false;

    private SharedPreferences sharedPreferences;

    @Before
    public void before() {
        // Get context of SettingsFragment to make sure we're testing SettingsFragment not test
        // application context.
        // This is necessary because we need to run an instrumented unit test for shared preferences.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        sharedPreferences = context.getSharedPreferences(REPLIES_POST, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(REPLIES_COMMENTS, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(ALL_NOTIFICATIONS, Context.MODE_PRIVATE);
    }

    @Test
    public void testWriteSPPost() {
        // Variable trueBoolP is the comparison value
        boolean trueBoolP = true;

        // Save value of trueBoolP in shared preferences
        sharedPreferences.edit().putBoolean(REPLIES_POST, trueBoolP).apply();
        // Get value stored in shared preferences and save in sharedPrefBoolP
        boolean sharedPrefBoolP = sharedPreferences.getBoolean(REPLIES_POST,BOOL_DEFAULT);

        // Check to see if received data is same as provided boolean
        assertEquals(trueBoolP, sharedPrefBoolP);
    }

    @Test
    public void testWriteSPComment() {
        // Variable trueBoolC is the comparison value
        boolean trueBoolC = false;

        // Save value of trueBoolP in shared preferences
        sharedPreferences.edit().putBoolean(REPLIES_COMMENTS, trueBoolC).apply();
        // Get value stored in shared preferences and save in sharedPrefBoolP
        boolean sharedPrefBoolC = sharedPreferences.getBoolean(REPLIES_COMMENTS,BOOL_DEFAULT);

        // Check to see if received data is same as provided boolean
        assertEquals(trueBoolC, sharedPrefBoolC);
    }

   @Test
    public void testWriteSPAll() {
        // Variable trueBoolA is the comparison value
        boolean trueBoolA = true;
        // To check for a false value
        boolean falseBoolA = false;

        // Save value of trueBoolA in shared preferences
        sharedPreferences.edit().putBoolean(ALL_NOTIFICATIONS, trueBoolA).apply();
        // Get value stored in shared preferences and save in sharedPrefBoolP
        boolean sharedPrefBoolA = sharedPreferences.getBoolean(ALL_NOTIFICATIONS,BOOL_DEFAULT);

        // Check to see if received data is same as provided boolean
        assertEquals(trueBoolA, sharedPrefBoolA);
    }

    // This method reads the values set by other other methods
    @Test
    public void testReadSharedPreferences() {
        // Note: sharedPrefBoolP = true
        boolean sharedPrefBoolP = sharedPreferences.getBoolean(REPLIES_POST,BOOL_DEFAULT);
        // Note: sharedPrefBoolA = true
        boolean sharedPrefBoolA = sharedPreferences.getBoolean(ALL_NOTIFICATIONS,BOOL_DEFAULT);
        // Note: sharedPrefBoolC = false
        boolean sharedPrefBoolC = sharedPreferences.getBoolean(REPLIES_COMMENTS,BOOL_DEFAULT);

        // Should return success (one true, one false)
        assertNotEquals(sharedPrefBoolA, sharedPrefBoolC);
        // Should return success (both true)
        assertEquals(sharedPrefBoolA, sharedPrefBoolP);
    }
}
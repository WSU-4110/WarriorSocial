package com.example.warriorsocial.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.warriorsocial.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettingsFragmentTest extends SettingsFragment  {

    public static final String REPLIES_POST = "REPLIES_POST";
    public static final String REPLIES_COMMENTS = "REPLIES_COMMENTS";
    public static final String ALL_NOTIFICATIONS = "ALL_NOTIFICATIONS";
    public static final boolean BOOL_DEFAULT = false;

    private SharedPreferences sharedPreferences;

    @Before
    public void before() {
        // Get context of SettingsFragment to make sure we're testing SettingsFragment not test class
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        sharedPreferences = context.getSharedPreferences(REPLIES_POST, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(REPLIES_COMMENTS, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(ALL_NOTIFICATIONS, Context.MODE_PRIVATE);
    }

   /* @Test
    public void put_and_get_preference() throws Exception {
        String string1 = "test";

       // sharedPreferences.edit().putString(KEY_PREF, string1).apply();
      //  String string2 = sharedPreferences.getString(KEY_PREF, "");

        // Verify that the received data is correct.
        assertEquals(string1, string2);
    }*/

    @After
    public void after() {
        //sharedPreferences.edit().putBoolean(REPLIES_POST, false).apply();
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
    public void testwriteSPComment() {
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
    public void testwriteSPAll() {
        // Variable trueBoolA is the comparison value
        boolean trueBoolA = true;
        // To check for a forced false value
        boolean falseBoolA = false;

        // Save value of trueBoolA in shared preferences
        sharedPreferences.edit().putBoolean(ALL_NOTIFICATIONS, trueBoolA).apply();
        // Get value stored in shared preferences and save in sharedPrefBoolP
        boolean sharedPrefBoolA = sharedPreferences.getBoolean(ALL_NOTIFICATIONS,BOOL_DEFAULT);

        // Check to see if received data is same as provided boolean
        assertEquals(trueBoolA, sharedPrefBoolA);
    }

    @Test
    public void testreadSharedPreferences() {
        // sharedPrefBoolP = true
        boolean sharedPrefBoolP = sharedPreferences.getBoolean(REPLIES_POST,BOOL_DEFAULT);
        // sharedPrefBoolA = true
        boolean sharedPrefBoolA = sharedPreferences.getBoolean(ALL_NOTIFICATIONS,BOOL_DEFAULT);
        // sharedPrefBoolC = false
        boolean sharedPrefBoolC = sharedPreferences.getBoolean(REPLIES_COMMENTS,BOOL_DEFAULT);

        // Should return success
        assertNotEquals(sharedPrefBoolA, sharedPrefBoolC);
        // Should return success
        assertEquals(sharedPrefBoolA, sharedPrefBoolP);
    }
}
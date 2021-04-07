package com.example.warriorsocial.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.EventDetailFragment;
import com.example.warriorsocial.ui.login.LoginActivity;
import com.example.warriorsocial.ui.settings.SettingsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    //private static final Object MODE_PRIVATE = 0;
    private SettingsViewModel settingsViewModel;
    FirebaseAuth fAuth;

    // Privacy Policy
    Button btn_privacy;

    //Change Password
    Button changepass;

    // Controls for shared preferences
    Switch swPost;
    Switch swComment;
    Switch swAllNotifications;
    EditText etUsername;
    Button btChangeUsername;


    // Shared preferences variables
    public static final String REPLIES_POST = "REPLIES_POST";
    public static final String REPLIES_COMMENTS = "REPLIES_COMMENTS";
    public static final String ALL_NOTIFICATIONS = "ALL_NOTIFICATIONS";
    public static final boolean BOOL_DEFAULT = false;
    public static final String USERNAME = "USERNAME";
    public static final String STRING_VALUE_DEFAULT = "WSU User";
    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Attach root to the layout
        View root = inflater.inflate(R.layout.settings_activity, container, false);

        // CHANGE THIS TO MATCH PRIVACY BUTTON WHEN ADDED
        btn_privacy = root.findViewById(R.id.btn_privacy);

        changepass = root.findViewById(R.id.resetPass);

        //Controls for shared preferences
        swPost = root.findViewById(R.id.switch1);
        swComment = root.findViewById(R.id.switch2);
        swAllNotifications = root.findViewById(R.id.switch3);
        etUsername = root.findViewById(R.id.textView2);
        btChangeUsername = root.findViewById(R.id.button3);

        // Read Shared Preferences values
        readSharedPreferences();

        // Set the onClickListener for the privacy button
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController for navigation between fragments
                NavController navController = Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment);

                // Bind to action to move from Settings page to Privacy Policy
                navController.navigate(R.id.action_navigation_settings_to_navigation_privacy);
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetmail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Change Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link.");
                passwordResetDialog.setView(resetmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error! Reset link was not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });

        // OnClick for Change Username Button
        btChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder changeUsernameDialog = new AlertDialog.Builder(v.getContext());
                changeUsernameDialog.setTitle("Change username to provided entry?");

                changeUsernameDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Write values to Shared Preferences
                        writeToSharedPreferences(swPost.isChecked(), swComment.isChecked(),
                                swAllNotifications.isChecked(), etUsername.getText().toString());

                    }
                });

                changeUsernameDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                    }
                });

                changeUsernameDialog.create().show();
            }
        });


        // OnClick for Replies to Post Switch
        swPost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                writeToSharedPreferences(swPost.isChecked(), swComment.isChecked(),
                        swAllNotifications.isChecked(), etUsername.getText().toString());
            }
        });
        // OnClick for Replies to Comments Switch
        swComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                writeToSharedPreferences(swPost.isChecked(), swComment.isChecked(),
                        swAllNotifications.isChecked(), etUsername.getText().toString());
            }
        });

        // OnClick for All Notifications
        swAllNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                writeToSharedPreferences(swPost.isChecked(), swComment.isChecked(),
                        swAllNotifications.isChecked(), etUsername.getText().toString());
            }
        });


        // Return root (layout)
        return root;
    }

    // writeToSharedPreferences writes data to the Shared Preferences
    public void writeToSharedPreferences(boolean replies_post, boolean replies_comments,
                                         boolean all_notifications, String username) {
        // Create editor object
        SharedPreferences sharedPrefWrite;
        SharedPreferences.Editor editor;
        sharedPrefWrite = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefWrite.edit();

        // Store values as name/value pairs
        editor.putString(USERNAME, username);
        editor.putBoolean(REPLIES_POST, replies_post);
        editor.putBoolean(REPLIES_COMMENTS, replies_comments);
        editor.putBoolean(ALL_NOTIFICATIONS, all_notifications);

        editor.commit();
    }


    // readSharedPreferences reads from shared preferences
    public void readSharedPreferences() {
        // Declare variables
        SharedPreferences sharedPrefRead;

        // Retrieve value from shared preferences
        sharedPrefRead = getActivity().getPreferences(Context.MODE_PRIVATE);

        // Set text fields to data from shared preferences
        etUsername.setText(sharedPrefRead.getString(USERNAME, STRING_VALUE_DEFAULT));
        swPost.setChecked(sharedPrefRead.getBoolean(REPLIES_POST, BOOL_DEFAULT));
        swComment.setChecked(sharedPrefRead.getBoolean(REPLIES_COMMENTS, BOOL_DEFAULT));
        swAllNotifications.setChecked(sharedPrefRead.getBoolean(ALL_NOTIFICATIONS, BOOL_DEFAULT));
    }

}


package com.example.warriorsocial.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        private SettingsViewModel settingsViewModel;
        FirebaseAuth fAuth;

        // Privacy Policy
        Button btn_privacy;

        //Change Password
        Button changepass;

        //Logout User
        Button logout;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            // Attach root to the layout
            View root = inflater.inflate(R.layout.settings_activity, container, false);

            // CHANGE THIS TO MATCH PRIVACY BUTTON WHEN ADDED
            btn_privacy = root.findViewById(R.id.btn_privacy);

            changepass = root.findViewById(R.id.resetPass);

            logout = root.findViewById(R.id.logout);

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

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });

            
            // Return root (layout)
            return root;
        }
 }



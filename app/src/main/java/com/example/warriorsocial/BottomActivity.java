package com.example.warriorsocial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.warriorsocial.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

// This is the activity that holds the icons on the bottom of the screen
public class BottomActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseUser user;
    Button logoutButton;
    Button changePass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        logoutButton = findViewById(R.id.button);
        changePass = findViewById(R.id.button4);
        //fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        // Connect to the icons
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Configuration for the action bar (top bar) with established hierarchies
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_organizations, R.id.navigation_discover, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                signOutUser();
            }

        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetpass = new EditText(v.getContext());
                final AlertDialog.Builder resetPassDialog = new AlertDialog.Builder(v.getContext());
                resetPassDialog.setTitle("Reset Password?");
                resetPassDialog.setMessage("Enter New Password (5 or more chars)");
                resetPassDialog.setView(resetpass);

                resetPassDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newpass = resetpass.getText().toString();
                        user.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BottomActivity.this, "Password was reset!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BottomActivity.this, "Password was reset!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    private void signOutUser() {
        Intent intent = new Intent(BottomActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
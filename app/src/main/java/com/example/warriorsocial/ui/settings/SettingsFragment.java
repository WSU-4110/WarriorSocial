package com.example.warriorsocial.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.warriorsocial.BottomActivity;
import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.login.LoginActivity;
import com.example.warriorsocial.ui.login.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

        private SettingsViewModel settingsViewModel;
        FirebaseAuth fAuth;
        StorageReference storageReference;


        // Privacy Policy
        Button btn_privacy;

        //Change Password
        Button changepass;

        //Logout User
        Button logout;


        // For sending notifications
        public static final String NOTIFICATION_S = "fromSettingsFragment";
        private boolean yesPass = false;


    // Controls for shared preferences
    Switch swAllNotifications;
    EditText etUsername;
    EditText userName;
    Button btChangeUsername;
    Button btnChangePicture;
    String userID;
    ImageView profileImage;

    // Shared preferences variables
    public static final String ALL_NOTIFICATIONS = "ALL_NOTIFICATIONS";
    public static final boolean BOOL_DEFAULT = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            // Attach root to the layout
            View root = inflater.inflate(R.layout.settings_activity, container, false);

            // CHANGE THIS TO MATCH PRIVACY BUTTON WHEN ADDED
            btn_privacy = root.findViewById(R.id.btn_privacy);

            changepass = root.findViewById(R.id.resetPass);

            logout = root.findViewById(R.id.logout);

            fAuth = FirebaseAuth.getInstance();

            profileImage = root.findViewById(R.id.imageView3);
            btnChangePicture = root.findViewById(R.id.button2);
            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });

        //Controls for shared preferences
        //swPost = root.findViewById(R.id.switch1);
        //swComment = root.findViewById(R.id.switch2);
        swAllNotifications = root.findViewById(R.id.switch3);
        etUsername = root.findViewById(R.id.textView2);
        userName = root.findViewById(R.id.et_studentUserName);
        btChangeUsername = root.findViewById(R.id.button3);
        final EditText newUserName = root.findViewById(R.id.textView2);
        //userID = fAuth.getCurrentUser().getUid();

        /*Query reference = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(userID);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        etUsername.setText(snapshot.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");

        etUsername.setText(username);

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



            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
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
                        etUsername.setText(newUserName.getText().toString());

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

        //OnClick for Change Profile Picture
        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open photo gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });


        // OnCheckedChange for All Notifications
        swAllNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                writeSPAll(swAllNotifications.isChecked());
            }
        });

        // Return root (layout)
            return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadToFirebase(imageUri);

            }
        }
    }

    private void uploadToFirebase(Uri imageUri) {
        final StorageReference fileReference = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // writeToSharedPreferences writes data to the Shared Preferences
    public void writeSPAll(boolean all_notifications) {
        // Create editor object
        SharedPreferences sharedPrefWrite;
        SharedPreferences.Editor editor;
        sharedPrefWrite = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefWrite.edit();

        // Store values as name/value pairs
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
        swAllNotifications.setChecked(sharedPrefRead.getBoolean(ALL_NOTIFICATIONS, BOOL_DEFAULT));
    }

    // For creating notifications
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_S,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("User Notification");

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}




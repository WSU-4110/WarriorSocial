package com.example.warriorsocial.ui.organizations;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class EditOrganizationProfile extends Fragment {
    private static final String TAG = "EditOrganizationProfileFragment";

    private DatabaseReference mDatabase;

    private Button editButton;
    private EditText editPresident;
    private EditText editVP;
    private EditText editPhone;
    private EditText editDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_organization_profile, container, false);

        editButton = root.findViewById(R.id.edit_organization_button);
        editPresident = root.findViewById(R.id.edit_organization_president);
        editVP = root.findViewById(R.id.edit_organization_vp);
        editPhone = root.findViewById(R.id.edit_organization_phone);
        editDescription = root.findViewById(R.id.edit_organization_description);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });


        // Initialize Organization
        mDatabase.child("StudentOrganizations/" + requireArguments().getString(OrganizationProfile.EXTRA_ORGANIZATION_KEY))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StudentOrganization currentOrg = dataSnapshot.getValue(StudentOrganization.class);

                        if (currentOrg == null) {
                            // User is null, error out
                            Log.e(TAG, "User is unexpectedly null");
                            Toast.makeText(getContext(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Set edit texts
                            editPresident.setText(currentOrg.getOrganizationPresident());
                            editVP.setText(currentOrg.getOrganizationVicePresident());
                            editPhone.setText(currentOrg.getOrganizationPhoneNumber());
                            editDescription.setText(currentOrg.getOrganizationDescription());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });


        // Initialize edit values

    }

    private void submitPost() {
        final String description = editDescription.getText().toString();
        final String president = editPresident.getText().toString();
        final String vp = editVP.getText().toString();
        final String phone = editPhone.getText().toString();

        // All fields are optional

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Posting...", Toast.LENGTH_SHORT).show();

        // Get current user's id
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("UserId Check: " + userId);
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getContext(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewPost(description, president, vp, phone);
                        }

                        setEditingEnabled(true);
                        // Navigate back to the org
                        NavController navController = NavHostFragment.findNavController(EditOrganizationProfile.this);

                        // Attach args
                        Bundle args = new Bundle();
                        String mOrganizationKey = requireArguments().getString(OrganizationProfile.EXTRA_ORGANIZATION_KEY);
                        if (mOrganizationKey == null) {
                            throw new IllegalArgumentException("Must pass EXTRA_ORGANIZATION_KEY");
                        }
                        args.putString(OrganizationProfile.EXTRA_ORGANIZATION_KEY, mOrganizationKey);

                        navController.navigate(R.id.action_editOrganizationProfile_to_organizationProfile, args);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        editDescription.setEnabled(enabled);
        editPresident.setEnabled(enabled);
        editVP.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        if (enabled) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.INVISIBLE);
        }
    }

    private void writeNewPost(final String description, final String president, final String vp, final String phone) {
        // Initialize Organization
        mDatabase.child("StudentOrganizations/" + requireArguments().getString(OrganizationProfile.EXTRA_ORGANIZATION_KEY))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StudentOrganization currentOrg = dataSnapshot.getValue(StudentOrganization.class);

                        if (currentOrg == null) {
                            // User is null, error out
                            Log.e(TAG, "User is unexpectedly null");
                            Toast.makeText(getContext(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String key = mDatabase.child("StudentOrganizations/"+requireArguments().getString(OrganizationProfile.EXTRA_ORGANIZATION_KEY)).getKey();
                            StudentOrganization updatedOrg = new StudentOrganization(currentOrg.getOrganizationName(), currentOrg.getOrganizationEmail()
                            , description, currentOrg.getOrganizationImageUrl(), phone, president, vp);
                            Map<String, Object> postValues = updatedOrg.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(key, postValues);

                            mDatabase.child("StudentOrganizations").updateChildren(childUpdates);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });

    }
}

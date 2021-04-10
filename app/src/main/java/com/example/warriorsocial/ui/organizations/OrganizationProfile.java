package com.example.warriorsocial.ui.organizations;
import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrganizationProfile extends Fragment {

    private static final String TAG = "OrganizationProfile";

    public static final String EXTRA_ORGANIZATION_KEY = "organization_key";

    //Organization reference + OrganizationListener + CalendarEvent key
    private DatabaseReference mOrganizationReference;
    private ValueEventListener mOrganizationListener;
    private String mOrganizationKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.activity_orgs, container, false);

        //Back button from fragment functionality
        //https://stackoverflow.com/questions/40395067/android-back-button-not-working-in-fragment/52331709
        setHasOptionsMenu(true);

        return root;
    }

    // onViewCreated (fires after onCreateView): 1st: Get Key from passed in args, 2nd: Get database reference using the key
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get StudentOrganization key from arguments
        mOrganizationKey = requireArguments().getString(EXTRA_ORGANIZATION_KEY);
        if (mOrganizationKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_ORGANIZATION_KEY");
        }

        // Initialize Database
        mOrganizationReference = FirebaseDatabase.getInstance().getReference()
                .child("StudentOrganizations/" + mOrganizationKey);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Inside onStart in OrganizationProfile");

        // Add value event listener to the StudentOrganization defining onDataChange and onCancelled for the listener
        ValueEventListener OrganizationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get StudentOrganization object and use the values to update the UI
                if(dataSnapshot.exists()) {
                    // Get TextViews and ImageViews from R
                    //ImageView organizationImage = getActivity().findViewById(R.id.tv_image);
                    TextView organizationName = getActivity().findViewById(R.id.tv_name);
                    TextView organizationEmail = getActivity().findViewById(R.id.tv_email);
                    TextView organizationPhone = getActivity().findViewById(R.id.tv_phone);
                    TextView organizationPresidentName = getActivity().findViewById(R.id.tv_president_name);
                    TextView organizationVicePresidentName = getActivity().findViewById(R.id.tv_vice_president_name);

                    // Get StudentOrganization object from dataSnapshot (handled by firebase using getters and setters)
                    StudentOrganization studentOrganization = dataSnapshot.getValue(StudentOrganization.class);
                    System.out.println("Organization Name: " + studentOrganization.getOrganizationName());

                    organizationName.setText(studentOrganization.getOrganizationName());
                    organizationEmail.setText(studentOrganization.getOrganizationEmail());
                    organizationPhone.setText(studentOrganization.getOrganizationDescription());
                    organizationPresidentName.setText(studentOrganization.getOrganizationDescription());
                    organizationVicePresidentName.setText(studentOrganization.getOrganizationEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting CalendarEvent failed, log a message
                Log.w(TAG, "loadCalendarEvent:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load CalendarEvent.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mOrganizationReference.addValueEventListener(OrganizationListener);

        // Keep copy of CalendarEvent listener so we can remove it when app stops
        mOrganizationListener = OrganizationListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove CalendarEvent value event listener
        if (mOrganizationListener != null) {
            mOrganizationReference.removeEventListener(mOrganizationListener);
        }
    }

}

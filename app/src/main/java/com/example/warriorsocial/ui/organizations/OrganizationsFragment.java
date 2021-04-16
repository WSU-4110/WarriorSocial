package com.example.warriorsocial.ui.organizations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.CalendarEventViewHolder;
import com.example.warriorsocial.ui.home.EventDetailFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrganizationsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<StudentOrganization, OrganizationsViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Inside onCreateView for OrganizationsFragment");
        //View root = inflater.inflate(R.layout.activity_orgs, container, false);//was my_row
        View root = inflater.inflate(R.layout.activity_main1, container, false);

        //Create database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Recycler Viewer from id
        //TODO: Change recyclerView id to something more representable (calendar uses id recycler_view, too similar)
        //TODO: Refactor activity_main1 to more representable layout name
        recyclerView = root.findViewById(R.id.recyclerView);

        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Inside onActivityCreated in OrganizationsFragment");

        // Setup Layout Manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with a default Query ( Current Day )
        // INITIAL QUERY IS SET TO ALL!

        Query eventsQuery = mDatabase.child("StudentOrganizations");
        // Setting up FirebaseRecyclerOptions be based off class:  "StudentOrganizations"
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<StudentOrganization>()
                .setQuery(eventsQuery, StudentOrganization.class)
                .build();
        //Setting up the FirebaseRecyclerAdapter to work with StudentOrganization as a model and OrganizationViewModel to construct them
        System.out.println("Setup mAdapter");
        mAdapter = new FirebaseRecyclerAdapter<StudentOrganization, OrganizationsViewHolder>(options) {

            @Override
            public OrganizationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                System.out.println("inside onCreateViewHolder in HomeFragment");
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new OrganizationsViewHolder(inflater.inflate(R.layout.student_organization_card, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(OrganizationsViewHolder viewHolder, int position, final StudentOrganization model) {
                System.out.println("inside onBindViewHolder in OrganizationsFragment");
                final DatabaseReference StudentOrganizationRef = getRef(position);
                // For Testing
                /*
                System.out.println("StudentOrganizationRef KEY: " + StudentOrganizationRef.getKey());
                System.out.println("StudentOrganizationRef PARENT: " + StudentOrganizationRef.getParent());
                System.out.println("StudentOrganizationRef REF: " + StudentOrganizationRef.getRef());
                System.out.println("StudentOrganizationRef ROOT: " + StudentOrganizationRef.getRoot());
                */

                // Set click listener for the entire card

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch StudentOrganizationProfile and pass a database reference key

                        NavController navController = Navigation.findNavController(requireActivity(),
                                R.id.nav_host_fragment);
                        Bundle args = new Bundle();

                        // Key = Organization Name (in this case)
                        String StudentOrganizationKey = StudentOrganizationRef.getKey();

                        // Load event key into args
                        args.putString(OrganizationProfile.EXTRA_ORGANIZATION_KEY, StudentOrganizationKey);
                        System.out.println("StudentOrganizationKey: " + StudentOrganizationKey);

                        // Navigate
                        navController.navigate(R.id.action_navigation_organizations_to_organizationProfile, args);
                    }
                });
                viewHolder.bindToPost(model);
            }
        };
        System.out.println("set recyclerView to mAdapter");
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
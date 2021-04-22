package com.example.warriorsocial.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.discover.DiscussionCategory;
import com.example.warriorsocial.ui.discover.DiscussionCategoryViewHolder;
import com.example.warriorsocial.ui.organizations.StudentOrganization;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


//need to figure out how to set up actmain to connect to item_news and activity_main
//need to set it up just like how the login page is setup

public class DiscoverFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<DiscussionCategory, DiscussionCategoryViewHolder> mAdapter;


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

        //layout is just square atm
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

        Query eventsQuery = mDatabase.child("DiscussionCategories");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DiscussionCategory>()
                .setQuery(eventsQuery, DiscussionCategory.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<DiscussionCategory, DiscussionCategoryViewHolder>(options) {

            @Override
            public DiscussionCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                System.out.println("inside onCreateViewHolder in HomeFragment");
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new DiscussionCategoryViewHolder(inflater.inflate(R.layout.discussion_category_card, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(DiscussionCategoryViewHolder viewHolder, int position, final DiscussionCategory model) {
                System.out.println("inside onBindViewHolder in OrganizationsFragment");
                final DatabaseReference DiscussionCategoryRef = getRef(position);

                // Set click listener for the entire card

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch StudentOrganizationProfile and pass a database reference key

                        NavController navController = Navigation.findNavController(requireActivity(),
                                R.id.nav_host_fragment);
                        Bundle args = new Bundle();

                        String DiscussionCategoryKey = DiscussionCategoryRef.getKey();
                        System.out.println("DiscussionCategoryKey: " + DiscussionCategoryKey);

                        // Load event key into args
                        args.putString("category", DiscussionCategoryKey);

                        // Navigate
                        navController.navigate(R.id.action_navigation_discover_to_discussion, args);
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
}
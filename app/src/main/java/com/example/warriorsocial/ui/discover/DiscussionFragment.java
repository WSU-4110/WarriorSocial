package com.example.warriorsocial.ui.discover;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.organizations.OrganizationPostViewHolder;
import com.example.warriorsocial.ui.organizations.StudentOrganization;
import com.example.warriorsocial.ui.organizations.StudentOrganizationPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class DiscussionFragment extends Fragment {

    private static final String TAG = "DiscussionPost";

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;

    //Organization reference + OrganizationListener + organization key
    private StorageReference mOrganizationStorageReference;
    private DatabaseReference mDatabase;
    private ValueEventListener mOrganizationListener;
    private String mCategoryKey;

    private Uri mImageUri;

    private ImageView mImageView;

    private FirebaseRecyclerAdapter<DiscussionPost, DiscussionPostViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;
    private FloatingActionButton discussionFAB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_discussion, container, false);

        recyclerView = root.findViewById(R.id.discussion_recycler_view);
        discussionFAB = root.findViewById(R.id.discussionFAB);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Back button from fragment functionality
        //https://stackoverflow.com/questions/40395067/android-back-button-not-working-in-fragment/52331709
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Inside onActivityCreated in OrganizationsFragment");

        // Connect FAB functionality
        discussionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment);

                Bundle args = new Bundle();
                args.putString("category", mCategoryKey);

                navController.navigate(R.id.action_navigation_discussion_to_newDiscussionPostFragment, args);
            }
        });

        // Setup Layout Manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        System.out.println(mCategoryKey);
        Query eventsQuery = mDatabase.child("DiscussionPosts").orderByChild("categoryName").equalTo(mCategoryKey);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DiscussionPost>()
                .setQuery(eventsQuery, DiscussionPost.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<DiscussionPost, DiscussionPostViewHolder>(options) {

            @Override
            public DiscussionPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new DiscussionPostViewHolder(inflater.inflate(R.layout.discussion_posts, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(DiscussionPostViewHolder viewHolder, int position, final DiscussionPost model) {
                final DatabaseReference DiscussionPostRef = getRef(position);

                // Set click listener for the entire card

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch StudentOrganizationProfile and pass a database reference key

                        NavController navController = Navigation.findNavController(requireActivity(),
                                R.id.nav_host_fragment);
                        Bundle args = new Bundle();

                        // Key = Organization Name (in this case)
                        String DiscussionPostRefKey = DiscussionPostRef.getKey();

                        // Load event key into args
                        args.putString("thread", DiscussionPostRefKey);

                        // Navigate
                        navController.navigate(R.id.action_navigation_discussion_to_threads, args);
                    }
                });
                viewHolder.bindToPost(model);
            }
        };
        System.out.println("set recyclerView to mAdapter");
        recyclerView.setAdapter(mAdapter);

    }

    //     onViewCreated (fires after onCreateView): 1st: Get Key from passed in args, 2nd: Get database reference using the key
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Layout Manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        // Get Category key from arguments
        mCategoryKey = requireArguments().getString("category");
        if (mCategoryKey == null) {
            throw new IllegalArgumentException("Must pass Category Key");
        }

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

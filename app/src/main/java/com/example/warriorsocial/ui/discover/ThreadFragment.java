package com.example.warriorsocial.ui.discover;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
public class ThreadFragment extends Fragment {

    private static final String TAG = "Thread";

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String EXTRA_ORGANIZATION_KEY = "comments";
    private static final int RESULT_OK = -1;

    //Organization reference + OrganizationListener + organization key
    private DatabaseReference mThreadReference;
    private DatabaseReference mDatabase;
    private ValueEventListener mThreadListener;
    private String mThreadKey;
    private FirebaseAuth fAuth;
    private Uri mImageUri;

    private ImageView mImageView;

    private FirebaseRecyclerAdapter<ThreadComment, ThreadCommentViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.discussion_thread, container, false);
        fAuth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.recyclerCommentView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button button = (Button)root.findViewById(R.id.reply_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something


                TextInputEditText mCommentText = root.findViewById(R.id.reply_text);
                String text = mCommentText.getText().toString();

                if(text == "") {
                    return;
                }
                TextView mThreadText = (TextView)root.findViewById(R.id.discussion_thread_title);
                String mThread = mThreadText.getText().toString();
                String username = fAuth.getCurrentUser().getEmail();
                int currentTimeMillis = Math.abs((int)System.currentTimeMillis());
                ThreadComment newComment = new ThreadComment(text, currentTimeMillis, username);
                Map<String, ThreadComment> commentMap = new HashMap<>();
                commentMap.put(String.valueOf(currentTimeMillis), newComment);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(
                        "DiscussionPosts/" + mThread + "/comments/" + currentTimeMillis);
                mDatabase.setValue(newComment);
                mCommentText.setText("");
            }
        });


        //Back button from fragment functionality
        //https://stackoverflow.com/questions/40395067/android-back-button-not-working-in-fragment/52331709
        setHasOptionsMenu(true);

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
        Query eventsQuery = mDatabase.child("DiscussionPosts/" + mThreadKey + "/comments");

        //Now I need to find out how to query by category == mOrganizationKey
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ThreadComment>()
                .setQuery(eventsQuery, ThreadComment.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<ThreadComment, ThreadCommentViewHolder>(options) {

            @Override
            public ThreadCommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                System.out.println("inside onCreateViewHolder in HomeFragment");
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ThreadCommentViewHolder(inflater.inflate(R.layout.discussion_comment, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(ThreadCommentViewHolder viewHolder, int position, final ThreadComment model) {

                // Set click listener for the entire card
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

        // Get StudentOrganization key from arguments
        mThreadKey = requireArguments().getString("thread");
        if (mThreadKey == null) {
            throw new IllegalArgumentException("Must pass Thread Key");
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }

        mThreadReference = FirebaseDatabase.getInstance().getReference()
                .child("DiscussionPosts/" + mThreadKey);

        ValueEventListener DiscussionPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get CalendarEvent object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    TextView discussionTitle = getActivity().findViewById(R.id.discussion_thread_title);
                    TextView discussionText = getActivity().findViewById(R.id.discussion_thread_text);

                    TextView eventTimestamp = getActivity().findViewById(R.id.event_timestamp);
                    TextView eventDescription = getActivity().findViewById(R.id.event_description);

                    DiscussionPost discussionPost = dataSnapshot.getValue(DiscussionPost.class);

                    discussionTitle.setText(discussionPost.getPostTitle());
                    discussionText.setText(discussionPost.getPostText());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting StudentOrganizationPost failure, log an error
                Log.w(TAG, "loadDiscussionThreadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load discussion thread.",
                        Toast.LENGTH_SHORT).show();
            }
        };


        mThreadReference.addValueEventListener(DiscussionPostListener);
        mThreadListener = DiscussionPostListener;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mThreadListener != null) {
            mThreadReference.removeEventListener(mThreadListener);
        }

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}

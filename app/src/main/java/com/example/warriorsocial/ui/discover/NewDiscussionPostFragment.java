package com.example.warriorsocial.ui.discover;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.NewCalendarEventFragment;
import com.example.warriorsocial.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewDiscussionPostFragment extends Fragment {
    private static final String TAG = "NewDiscussionPostFragment";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;

    private Button addDiscussionPostButton;
    private EditText discussionTitle;
    private EditText discussionText;

    private String mCategoryKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_discussion_post, container, false);

        addDiscussionPostButton = root.findViewById(R.id.discussion_post_button);
        discussionTitle = root.findViewById(R.id.discussion_title);
        discussionText = root.findViewById(R.id.discussion_text);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCategoryKey = requireArguments().getString("category");
        if (mCategoryKey == null) {
            throw new IllegalArgumentException("Must pass Category Key");
        }

        addDiscussionPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

    }

    private void submitPost() {
        final String title = discussionTitle.getText().toString();
        final String text = discussionText.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            discussionTitle.setError(REQUIRED);
            return;
        }

        // text is required
        if (TextUtils.isEmpty(text)) {
            discussionText.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Adding Discussion Post...", Toast.LENGTH_SHORT).show();

        // Get current user's id
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String postEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
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
                            // Write new post (first get category from args)
                            // Get Category key from arguments

                            writeNewPost(title, text, mCategoryKey);
                        }

                        setEditingEnabled(true);
                        NavController navController = NavHostFragment.findNavController(NewDiscussionPostFragment.this);
                        Bundle args = new Bundle();
                        args.putString("category", mCategoryKey);
                        navController.navigate(R.id.action_newDiscussionPostFragment_to_navigation_discussion2, args);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        discussionTitle.setEnabled(enabled);
        discussionText.setEnabled(enabled);
        if (enabled) {
            addDiscussionPostButton.setVisibility(View.VISIBLE);
        } else {
            addDiscussionPostButton.setVisibility(View.INVISIBLE);
        }
    }

    private void writeNewPost(String title, String text, String category) {
        final String key = mDatabase.child("DiscussionPosts/"+title).getKey();
        System.out.println("Writing with key: " + key);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DiscussionPost newPost = new DiscussionPost(category, text, title, userId);
        Map<String, Object> postValues = newPost.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);

        mDatabase.child("DiscussionPosts").updateChildren(childUpdates);

    }
}

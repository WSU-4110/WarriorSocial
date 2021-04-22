package com.example.warriorsocial.ui.home;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.login.User;
import com.example.warriorsocial.ui.organizations.NewPostFragment;
import com.example.warriorsocial.ui.organizations.StudentOrganizationPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;

public class NewCalendarEventFragment extends Fragment {
    private static final String TAG = "NewPostFragment";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private DatabaseReference mCalendarEventsReference;

    private Button addEventButton;
    private EditText eventTitleEditText;
    private EditText eventDescriptionEditText;
    private DatePicker eventDatePicker;
    private TimePicker eventTimePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_calendar_event, container, false);

        eventTitleEditText = root.findViewById(R.id.new_event_title);
        eventDescriptionEditText = root.findViewById(R.id.new_event_description);
        eventDatePicker = root.findViewById(R.id.date_picker);
        eventTimePicker = root.findViewById(R.id.time_picker);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addEventButton = getActivity().findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        // Initialize Organization Database
        mCalendarEventsReference = FirebaseDatabase.getInstance().getReference().child("CalendarEvents");
    }

    private void submitPost() {
        final String title = eventTitleEditText.getText().toString();
        final String description = eventDescriptionEditText.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            eventTitleEditText.setError(REQUIRED);
            return;
        }

        // Description is required
        if (TextUtils.isEmpty(description)) {
            eventDescriptionEditText.setError(REQUIRED);
            return;
        }

        // Date and Time have some defaults

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Adding Calendar Event...", Toast.LENGTH_SHORT).show();

        final Map<String, Integer> dateMap = new HashMap<>();
        int eventYear = eventDatePicker.getYear();
        int eventMonth = eventDatePicker.getMonth() + 1; // Month is by default off by 1
        int eventDay = eventDatePicker.getDayOfMonth();
        int eventHour = eventTimePicker.getHour();
        int eventMinutes = eventTimePicker.getMinute();
        /*
        String AM_PM;
        if(eventHour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }*/
        dateMap.put("year", eventYear);
        dateMap.put("month", eventMonth);
        dateMap.put("day", eventDay);
        dateMap.put("hour", eventHour);
        dateMap.put("minute", eventMinutes);

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
                            // Write new post (postImage handled later)
                            writeNewPost(description, postEmail, title, userId, dateMap);
                        }

                        setEditingEnabled(true);
                        NavHostFragment.findNavController(NewCalendarEventFragment.this)
                                .navigate(R.id.action_newCalendarEventFragment_to_navigation_home);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        eventTitleEditText.setEnabled(enabled);
        eventDescriptionEditText.setEnabled(enabled);
        if (enabled) {
            addEventButton.setVisibility(View.VISIBLE);
        } else {
            addEventButton.setVisibility(View.INVISIBLE);
        }
    }

    private void writeNewPost(final String description, String postEmail, final String title, String userId, final Map<String, Integer> dateMap) {
        // Create new event at /year/month/day/
        final String key = mCalendarEventsReference.child("/" + dateMap.get("year") +
                "/" + dateMap.get("month") +
                "/" + dateMap.get("day")).push().getKey();
        System.out.println("Writing with key: " + key);



        DatabaseReference username = mDatabase.child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/username" );
        username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eventTimestamp = dateMap.get("hour") + ":" + dateMap.get("minute");
                System.out.println("eventTimestamp: " + eventTimestamp);
                String currentUserName = dataSnapshot.getValue(String.class);
                System.out.println("CurrentUserName: " + currentUserName);
                CalendarEvent newEvent = new CalendarEvent(currentUserName, title, 0, description, eventTimestamp);
                Map<String, Object> postValues = newEvent.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);

                mCalendarEventsReference.child("/" + dateMap.get("year") +
                        "/" + dateMap.get("month") +
                        "/" + dateMap.get("day")).updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}


package com.example.warriorsocial.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.warriorsocial.R;
//import com.example.warriorsocial.databinding.FragmentCalendarEventDetailBinding;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.CalendarEventViewHolder;

import java.util.ArrayList;
import java.util.List;

public class EventDetailFragment extends Fragment {

    private static final String TAG = "CalendarEventDetailFragment";

    public static final String EXTRA_CALENDAREVENT_KEY = "CalendarEvent_key";

    //CalendarEvent referenc + CalendarEventlistener + CalendarEvent key
    private DatabaseReference mCalendarEventReference;
    private ValueEventListener mCalendarEventListener;
    private String mCalendarEventKey;
    // FragmentCalendarEventDetailBinding = binding to replace R for CalendarEventDetail
    //private FragmentCalendarEventDetailBinding binding;




    @Nullable
    @Override
    //onCreateView: setbinding to be for whatever layout is being
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        binding = FragmentCalendarEventDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
         */
        super.onCreateView(inflater, container, savedInstanceState);
        System.out.println("Inside onCreateView in EventDetailFragment");
        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("Inside onViewCreated in EventDetailFragment");

        // Get CalendarEvent key from arguments
        mCalendarEventKey = requireArguments().getString(EXTRA_CALENDAREVENT_KEY);
        if (mCalendarEventKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_CalendarEvent_KEY");
        }

        // Initialize Database
        mCalendarEventReference = FirebaseDatabase.getInstance().getReference()
                .child("CalendarEvents").child(mCalendarEventKey);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Inside onStart in EventDetailFragment");

        final TextView eventTitle = getActivity().findViewById(R.id.event_title);
        final TextView organizationName = getActivity().findViewById(R.id.event_organization_name);
        final TextView eventTimestamp = getActivity().findViewById(R.id.event_timestamp);
        final TextView eventDescription = getActivity().findViewById(R.id.event_description);

        // Add value event listener to the CalendarEvent
        ValueEventListener CalendarEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get CalendarEvent object and use the values to update the UI
                CalendarEvent CalendarEvent = dataSnapshot.getValue(CalendarEvent.class);
                eventTitle.setText(CalendarEvent.getEventTitle());
                organizationName.setText(CalendarEvent.getOrganizationName());
                eventTimestamp.setText(CalendarEvent.getEventTimestamp());
                eventDescription.setText(CalendarEvent.getEventDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting CalendarEvent failed, log a message
                Log.w(TAG, "loadCalendarEvent:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load CalendarEvent.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mCalendarEventReference.addValueEventListener(CalendarEventListener);

        // Keep copy of CalendarEvent listener so we can remove it when app stops
        mCalendarEventListener = CalendarEventListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove CalendarEvent value event listener
        if (mCalendarEventListener != null) {
            mCalendarEventReference.removeEventListener(mCalendarEventListener);
        }
    }
}



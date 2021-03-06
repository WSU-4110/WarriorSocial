package com.example.warriorsocial.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.organizations.StudentOrganization;
import com.example.warriorsocial.ui.organizations.StudentOrganizationPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Home Fragment displays the calendar and allows Students to select event lists from the dates
public class HomeFragment extends Fragment {

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private FirebaseRecyclerAdapter<CalendarEvent, CalendarEventViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    // RecyclerView connected to the calendar (list of events)
    private RecyclerView recyclerView;
    // Calendar on home fragment
    private CalendarView calendarView;
    private FloatingActionButton eventFAB;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Attaches to fragment_home layout
        super.onCreateView(inflater, container, savedInstanceState);
        System.out.println("Inside onCreateView");
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Create database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Calendar
        calendarView = root.findViewById(R.id.home_calendar);

        // Recycler Viewer
        recyclerView = root.findViewById(R.id.recycler_view);

        // Floating Action (Post new calendar Event)
        eventFAB = root.findViewById(R.id.eventFAB);
        return root;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            System.out.println("Inside onActivityCreated");
            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(mManager);

            // InitDb
            Button initDb = getActivity().findViewById(R.id.init_db_button);
            initDb.setVisibility(View.GONE);
            initDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("reading file");
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(
                                "orginfo.txt")));
                        int count = 0;
                        String line = reader.readLine();
                        while (count < 10) {
                            if (line != null) {
                                System.out.println(line);
                                String name = line.substring(2);
                                line = reader.readLine();
                                String email = line.substring(2);
                                email = email.replaceAll("\\.", "_");
                                line = reader.readLine();
                                String description;
                                if (line.length() > 2 && line.length() <= 27) {
                                    description = line.substring(2);
                                } else if (line.length() > 27){
                                    description = line.substring(2,27);
                                } else {
                                    description = "";
                                }
                                line = reader.readLine();
                                StudentOrganization newOrg = new StudentOrganization(name, email, description, "", "", "", "");
                                writeNewOrg(newOrg);
                            }
                            count++;
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Set onClick for FAB
            eventFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch EventDetailFragment and pass a database reference key

                    NavController navController = Navigation.findNavController(requireActivity(),
                            R.id.nav_host_fragment);
                    navController.navigate(R.id.action_navigation_home_to_newCalendarEventFragment);
                }
            });


            // Set up FirebaseRecyclerAdapter with a default Query ( Current Day )
            //TODO: Set initial query to current day (Current implementation requires API 26 [our minimum is 24])

            int currentYear = 0;
            int currentMonth = 0;
            int currentDay = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
                currentYear = currentDate.getYear();
                currentMonth = currentDate.getMonthValue();
                currentDay = currentDate.getDayOfMonth();
            }

            Query eventsQuery = getQuery(mDatabase, currentYear, currentMonth, currentDay);

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CalendarEvent>()
                    .setQuery(eventsQuery, CalendarEvent.class)
                    .build();
            System.out.println("Setup mAdapter");
            mAdapter = new FirebaseRecyclerAdapter<CalendarEvent, CalendarEventViewHolder>(options) {

                @Override
                public CalendarEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                    System.out.println("inside onCreateViewHolder in HomeFragment");
                    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                    return new CalendarEventViewHolder(inflater.inflate(R.layout.calendar_event, viewGroup, false));
                }

                @Override
                protected void onBindViewHolder(CalendarEventViewHolder viewHolder, int position, final CalendarEvent model) {
                    System.out.println("inside onBindViewHolder in HomeFragment");
                    final DatabaseReference CalendarEventRef = getRef(position);
                    System.out.println("CalendarEventRef KEY: " + CalendarEventRef.getKey());
                    System.out.println("CalendarEventRef PARENT: " + CalendarEventRef.getParent());
                    System.out.println("CalendarEventRef REF: " + CalendarEventRef.getRef());
                    System.out.println("CalendarEventRef ROOT: " + CalendarEventRef.getRoot());


                    // Set click listener for the whole event view
                    //final String CalendarEventKey = CalendarEventRef.getKey();


                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Launch EventDetailFragment and pass a database reference key

                            NavController navController = Navigation.findNavController(requireActivity(),
                                    R.id.nav_host_fragment);
                            Bundle args = new Bundle();

                            // Find the Event "key"/"path" in the database using a regex
                            Pattern dateRegex = Pattern.compile("\\d{4}\\W(0?[1-9]|1[012])\\W(0?[1-9]|[12][0-9]|[3[01]]).*");
                            Matcher dateMatcher = dateRegex.matcher(CalendarEventRef.getRef().toString());
                            String CalendarEventKey = ""; //default key
                            if (dateMatcher.find()) {
                                System.out.println("Date matcher found: " + dateMatcher.group());
                                CalendarEventKey = dateMatcher.group();
                            }

                            // Load event key
                            args.putString(EventDetailFragment.EXTRA_CALENDAREVENT_KEY, CalendarEventKey);
                            System.out.println("CalendarEventKey: " + CalendarEventKey);

                            // Navigate
                            navController.navigate(R.id.action_navigation_home_to_event_detail, args);
                        }
                    });
                    viewHolder.bindToPost(model);
                }
            };
            System.out.println("set recyclerView to mAdapter");
            recyclerView.setAdapter(mAdapter);


            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    // Sends date info to RecyclerAdapter (mAdapter)
                    System.out.println("Changed Date| year: " + year + " month: " + month + " day: " + dayOfMonth);
                    // For an unknown reason, month is received 1 month too low. The plus 1 is to counteract this for now
                    // TODO: Figure out reason behind the decremented month
                    Query newQuery = getQuery(mDatabase, year, month+1, dayOfMonth);
                    FirebaseRecyclerOptions newOptions = new FirebaseRecyclerOptions.Builder<CalendarEvent>()
                            .setQuery(newQuery, CalendarEvent.class)
                            .build();
                    mAdapter.updateOptions(newOptions);
                }
            });
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

        public Query getQuery(DatabaseReference databaseReference, int year, int month, int dayOfMonth) {
            // All posts
            System.out.println("getquery");
            return databaseReference.child("CalendarEvents/" + year + "/" + month + "/" + dayOfMonth);
        }

        public void writeNewOrg(StudentOrganization newOrg) {
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
            String key = mDatabase.child("StudentOrganizations").push().getKey();
            Map<String, Object> postValues = newOrg.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, postValues);

            mDatabase.child("StudentOrganizations").updateChildren(childUpdates);
        }
    }
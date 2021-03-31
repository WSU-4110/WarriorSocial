package com.example.warriorsocial.ui.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warriorsocial.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Home Fragment displays the calendar and allows Students to select event lists from the dates
public class HomeFragment extends Fragment {

    // Instantiating the DBSingleton class
    private DBSingleton dbSingleton = DBSingleton.getInstance();
    private FirebaseRecyclerAdapter<CalendarEvent, CalendarEventViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    // RecyclerView connected to the calendar (list of events)
    private RecyclerView recyclerView;
    // Calendar on home fragment
    private CalendarView calendarView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Attaches to fragment_home layout
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Calendar
        calendarView = root.findViewById(R.id.home_calendar);

        // Recycler Viewer
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        return root;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(mManager);

            // Using DBSingleton to set up FirebaseRecyclerAdapter with a default Query
            Query eventsQuery = dbSingleton.getQuery(dbSingleton, 0, 0, 0);

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CalendarEvent>()
                    .setQuery(eventsQuery, CalendarEvent.class)
                    .build();
            // Set up mAdapter
            mAdapter = new FirebaseRecyclerAdapter<CalendarEvent, CalendarEventViewHolder>(options) {

                @Override
                public CalendarEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                    return new CalendarEventViewHolder(inflater.inflate(R.layout.calendar_event, viewGroup, false));
                }

                @Override
                protected void onBindViewHolder(CalendarEventViewHolder viewHolder, int position, final CalendarEvent model) {
                    final DatabaseReference CalendarEventRef = getRef(position);

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
                                CalendarEventKey = dateMatcher.group();
                            }

                            // Load event key
                            args.putString(EventDetailFragment.EXTRA_CALENDAREVENT_KEY, CalendarEventKey);

                            // Navigate
                            navController.navigate(R.id.action_navigation_home_to_event_detail, args);
                        }
                    });
                    viewHolder.bindToPost(model);
                }
            };
            // Set recyclerView to mAdapter
            recyclerView.setAdapter(mAdapter);


            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    // Sends date info to RecyclerAdapter (mAdapter)

                    // Using getQuery method in DBSingleton class
                    Query newQuery = dbSingleton.getQuery(dbSingleton.getInstance(), year, month+1, dayOfMonth);

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

    }
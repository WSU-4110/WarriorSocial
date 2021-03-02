package com.example.warriorsocial.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

// Home Fragment displays the calendar and allows Students to select event lists from the dates
public class HomeFragment extends Fragment {

    // RecyclerView connected to the calendar (list of events)
    private RecyclerView recyclerView;
    // Calendar on home fragment
    private CalendarView calendarView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Attaches to fragment_home layout
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Calendar
        calendarView = root.findViewById(R.id.home_calendar);

        // Recycler Viewer
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        // Make the calendar respond when date clicked
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Sends date info to RecyclerAdapter
                recyclerView.setAdapter(new RecyclerAdapter(year, month, dayOfMonth));
            }
        });
        return root;
    }

    }
package com.example.warriorsocial.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

import java.util.Random;

// This is linked to the RecyclerView on the Home Fragment (list below calendar where events appear)
public class RecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {

        // Represents the year, month, and dayOfMonth
        private int year;
        private int month;
        private int day;

        // For demo purposes in Sprint 1
        String eventArr[] = {"Software Engineering Club Meeting", "Alpha Gamma Delta Meeting", "American Red Cross Club",
        "Armenian Folk Dance Club", "Bagels for Homeless Club", "Black Student Union", "Country Music Club",
        "Detroit vs. Addiction", "Formula One Club", "LGBT+ People in Medicine"};

        // Constructor
        public RecyclerAdapter(int year, int month, int dayOfMonth) {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;
        }

        @Override
        // Position in the RecyclerView
        public int getItemViewType(final int position) {
            return R.layout.home_item_view;
        }

        // Inflate the Layout (instantiates the layout into the view)
        @NonNull
        @Override
        public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // viewType defines which view we're using
            // parent is where the new view will be added
            // attachToRoot is false because RecyclerViewer will handle when to attach
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            // Return the view
            return new HomeRecyclerViewHolder(view);
        }

        // Set text in RecyclerView. Iterates as many times as items in getItemCount()
        @Override
        public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
                holder.getView().setText(eventArr[position]);
        }

        // Number of entries, currently linked to array of student clubs for Sprint 1
       @Override
        public int getItemCount() {
            return eventArr.length;
        }
}

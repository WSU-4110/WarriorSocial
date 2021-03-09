package com.example.warriorsocial.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    // Below for Recycler Viewer
    private TextView view;

    // Text box in each field of Recycler Viewer
    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        // recyclerText is defined in home_item_view.xml layout
        view = itemView.findViewById(R.id.recyclerText);
        System.out.println("Inside HomeRecyclerViewHolder!");
    }

    public TextView getView() {
        System.out.println("Inside getView!");
        return view;
    }
}

package com.example.warriorsocial.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.CalendarEventViewHolder;
import com.example.warriorsocial.ui.home.EventDetailFragment;
//need to figure out how to set up actmain to connect to item_news and activity_main
//need to set it up just like how the login page is setup

public class DiscoverFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.act_main, container, false);
        return root;
    }
}
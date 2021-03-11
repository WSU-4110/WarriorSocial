package com.example.warriorsocial.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.warriorsocial.R;
//need to figure out how to set up actmain to connect to item_news and activity_main
//need to set it up just like how the login page is setup

public class DiscoverFragment extends Fragment {

    private DiscoverViewModel discoverViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.item_news, container, false);//changed from act_main
        return root;

    }
}
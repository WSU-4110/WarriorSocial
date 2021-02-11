package com.example.warriorsocial.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.settings.SettingsViewModel;

public class SettingsFragment extends Fragment {

        private SettingsViewModel settingsViewModel;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            //just change the fragment_dashboard
            //with the fragment you want to inflate
            //like if the class is HomeFragment it should have R.layout.home_fragment
            //if it is DashboardFragment it should have R.layout.fragment_dashboard
            return inflater.inflate(R.layout.settings_activity, null);


          /*  settingsViewModel =
                    new ViewModelProvider(this).get(SettingsViewModel.class);
            View root = inflater.inflate(R.layout.fragment_settings, container, false);
            final TextView textView = root.findViewById(R.id.text_settings);
            settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
            return root;*/






        }
    }


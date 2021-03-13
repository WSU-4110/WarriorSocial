package com.example.warriorsocial.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.EventDetailFragment;
import com.example.warriorsocial.ui.settings.SettingsViewModel;

public class SettingsFragment extends Fragment {

        private SettingsViewModel settingsViewModel;

        // Privacy Policy
        Button btn_privacy;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            // Attach root to the layout
            View root = inflater.inflate(R.layout.settings_activity, container, false);

            // CHANGE THIS TO MATCH PRIVACY BUTTON WHEN ADDED
            btn_privacy = root.findViewById(R.id.btn_privacy);

            // Set the onClickListener for the privacy button
            btn_privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // NavController for navigation between fragments
                    NavController navController = Navigation.findNavController(requireActivity(),
                            R.id.nav_host_fragment);

                    // Bind to action to move from Settings page to Privacy Policy
                    navController.navigate(R.id.action_navigation_settings_to_navigation_privacy);
                }
            });
            
            // Return root (layout)
            return root;
        }
    }


package com.example.warriorsocial.ui.organizations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.warriorsocial.R;

public class OrganizationsFragment extends Fragment {

    private OrganizationsViewModel organizationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Inside onCreateView for Organizations");
        organizationsViewModel =
                new ViewModelProvider(this).get(OrganizationsViewModel.class);
        View root = inflater.inflate(R.layout.my_row, container, false);
        final TextView textView = root.findViewById(R.id.myText1);
        final ImageView imageView = root.findViewById(R.id.myImageView);
        /*organizationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                //imageView.setImageResource(t);
            }
        });*/
        return root;
    }
}
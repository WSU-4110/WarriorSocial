package com.example.warriorsocial.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.login.LoginViewModel;
import com.example.warriorsocial.ui.login.LoginViewModelFactory;

public class HomeViewModel extends ViewModel {

    // Pregenerated code
    private MutableLiveData<String> mText;

    // Pregenerated code
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    // Pregenerated code
    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.warriorsocial.ui.privacy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warriorsocial.PrivacyPolicy;

public class PrivacyViewModel extends ViewModel {



        private MutableLiveData<String> mText;

        public PrivacyViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is settings fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }


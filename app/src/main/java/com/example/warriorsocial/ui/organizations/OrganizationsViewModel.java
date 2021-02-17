package com.example.warriorsocial.ui.organizations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrganizationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrganizationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is organizations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
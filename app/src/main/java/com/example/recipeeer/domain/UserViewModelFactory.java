package com.example.recipeeer.domain;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class UserViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public UserViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UserViewModel(mApplication, mParam);
    }
}

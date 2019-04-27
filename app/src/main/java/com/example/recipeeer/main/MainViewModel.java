package com.example.recipeeer.main;

import android.app.Application;

import com.example.recipeeer.db.UserRepository;
import com.example.recipeeer.domain.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<User> currentUser;

    public MainViewModel(@NonNull Application application, String email) {
        super(application);
        userRepository = new UserRepository(application);
        currentUser = userRepository.getUserByEmail(email);
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public LiveData<User> getUser() {
        return currentUser;
    }
}

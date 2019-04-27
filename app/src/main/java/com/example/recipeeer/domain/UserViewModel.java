package com.example.recipeeer.domain;

import android.app.Application;

import com.example.recipeeer.db.UserRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<User> currentUser;


    public UserViewModel(@NonNull Application application, String email) {
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

    public void updateUsername(String email, String username) {
        userRepository.updateUsername(email,username);
    }

    public void updateUserGender(String email, int gender) {
        userRepository.updateUserGender(email,gender);
    }
}

package com.example.recipeeer.profile;

import android.app.Application;

import com.example.recipeeer.db.UserRepository;
import com.example.recipeeer.domain.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProfileViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<User> getUser(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void updateUsername(String email, String username) {
        userRepository.updateUsername(email,username);
    }

    public void updateUserGender(String email, int gender) {
        userRepository.updateUserGender(email,gender);
    }
}

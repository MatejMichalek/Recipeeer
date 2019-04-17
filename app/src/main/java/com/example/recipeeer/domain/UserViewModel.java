package com.example.recipeeer.domain;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;
    private LiveData<User> currentUser;


    public UserViewModel(@NonNull Application application, String email) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUsers();
        currentUser = userRepository.getUserByEmail(email);
    }


    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public User getCurrentUserByEmail(String email) {
        return userRepository.getCurrentUserByEmail(email);
    }

    public LiveData<User> getUser() {
        return currentUser;
    }

    public boolean checkIfUserExists(String email) {
        if (userRepository.checkIfUserExists(email) == 1) {
            return true;
        }
        return false;
    }

    public void updateUsername(String email, String username) {
        userRepository.updateUsername(email,username);
    }

    public void updateUserGender(String email, int gender) {
        userRepository.updateUserGender(email,gender);
    }
}

package com.example.recipeeer.domain;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository (Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert (User user) {
        new insertAsyncTask(userDao).execute(user);
    }

    public User getCurrentUserByEmail(String email) {
//        return userDao.getUserByEmail(email);
        try {
            return new getCurrentUserByEmailAsyncTask(userDao).execute(email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



    private static class insertAsyncTask extends AsyncTask<User,Void,Void> {

        private UserDao asyncTaskDao;

        public insertAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }


        @Override
        protected Void doInBackground(User... users) {
            asyncTaskDao.insert(users[0]);
            return null;
        }
    }

    private static class getCurrentUserByEmailAsyncTask extends AsyncTask<String,Void,User> {
        private UserDao asyncTaskDao;

        public getCurrentUserByEmailAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
           return asyncTaskDao.getUserByEmail(strings[0]);
        }
    }
}

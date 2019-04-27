package com.example.recipeeer.db;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import com.example.recipeeer.domain.User;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDao userDao;

    public UserRepository (Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    public void insert (User user) {
        new insertAsyncTask(userDao).execute(user);
    }

    public LiveData<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public void updateUsername(String email, String username) {
        new updateUsernameAsyncTask(userDao).execute(new UpdateUsername(email,username));
    }

    public void updateUserGender(String email, int gender) {
        new updateUserGenderAsyncTask(userDao).execute(new UpdateUserGender(email,gender));
    }

    private static class insertAsyncTask extends AsyncTask<User,Void,Void> {

        private UserDao asyncTaskDao;

        public insertAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }


        @Override
        protected Void doInBackground(User... users) {
            try {
                asyncTaskDao.insert(users[0]);
            } catch (SQLiteConstraintException e) {
                // user is already in db
                return null;
            }
            return null;
        }
    }

    private static class updateUsernameAsyncTask extends AsyncTask<UpdateUsername,Void,Void> {

        private UserDao asyncTaskDao;

        public updateUsernameAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }

        @Override
        protected Void doInBackground(UpdateUsername... updateUsers) {
            asyncTaskDao.updateUsername(updateUsers[0].email,updateUsers[0].updatedValue);
            return null;
        }
    }

    private class UpdateUsername {
        String email;
        String updatedValue;

        public UpdateUsername(String email, String updatedValue) {
            this.email = email;
            this.updatedValue = updatedValue;
        }
    }

    private static class updateUserGenderAsyncTask extends AsyncTask<UpdateUserGender,Void,Void> {

        UserDao asyncTaskDao;

        public updateUserGenderAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }

        @Override
        protected Void doInBackground(UpdateUserGender... updateUserGenders) {
            asyncTaskDao.updateUserGender(updateUserGenders[0].email,updateUserGenders[0].gender);
            return null;
        }
    }

    private class UpdateUserGender {
        String email;
        int gender;

        public UpdateUserGender(String email, int gender) {
            this.email = email;
            this.gender = gender;
        }
    }
}
package com.example.recipeeer.domain;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
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
//        return userDao.getCurrentUserByEmail(email);
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

    public int checkIfUserExists(String email) {
        try {
            return new userExistsAsyncTask(userDao).execute(email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
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

    private static class getCurrentUserByEmailAsyncTask extends AsyncTask<String,Void,User> {
        private UserDao asyncTaskDao;

        public getCurrentUserByEmailAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
           return asyncTaskDao.getCurrentUserByEmail(strings[0]);
        }
    }

    private static class userExistsAsyncTask  extends AsyncTask<String,Void,Integer> {
        private UserDao asyncTaskDao;

        public userExistsAsyncTask(UserDao userDao) {
            asyncTaskDao = userDao;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return asyncTaskDao.userExists(strings[0]);
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

    private class UpdateUserGender {
        String email;
        int gender;

        public UpdateUserGender(String email, int gender) {
            this.email = email;
            this.gender = gender;
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
}

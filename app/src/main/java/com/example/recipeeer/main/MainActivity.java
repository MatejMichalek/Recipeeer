package com.example.recipeeer.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.createRecipe.CreateRecipeActivity;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.favorites.FavoriteRecipesFragment;
import com.example.recipeeer.login.LogInActivity;
import com.example.recipeeer.myRecipes.MyRecipesFragment;
import com.example.recipeeer.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements ActivityWithDrawer, WelcomeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MyRecipesFragment.OnFragmentInteractionListener, FavoriteRecipesFragment.OnFragmentInteractionListener {

    private User currentUser;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // try to get firebase user instance
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            // user is not logged in yet, therefore LogIn activity is started
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            // user is logged in

            // create view model for main activity
            MainViewModel mainViewModel = ViewModelProviders.of(this, new MainViewModelFactory(getApplication(), mFirebaseUser.getEmail())).get(MainViewModel.class);
            mNavigationView = findViewById(R.id.nav_view);

            // if saved bundle is not null, this activity was already created before, and that is why ViewModel already holds data for current user
            if (savedInstanceState != null)
                currentUser = mainViewModel.getUser().getValue();

            // start observing changes in user's data in order to update info in drawer
            mainViewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        currentUser = user;
                        ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.headerUserName)).setText(user.getName());
                    }
                }
            });

            // try to insert new user to local storage, if user already exists in db, nothing happens
            mainViewModel.insert(new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(),2));

            Toolbar mToolbar = findViewById(R.id.mToolbar);
            setSupportActionBar(mToolbar);

            // if this activity is created for the first time, it specifies start/welcome fragment for this activity
            if (savedInstanceState == null)
                Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_welcomeFragment);


            // creating drawer with toggle
            mDrawerLayout = findViewById(R.id.drawerLayout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            // navigation view actions
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()) {
                        // navigating by navigation graph and global actions for below fragments
                        case R.id.mHome:
                            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_welcomeFragment);
                            break;

                        case R.id.profile:
                            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_profileFragment);
                            break;

                        case R.id.myRecipes:
                            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_myRecipesFragment);
                            break;

                        case R.id.favorites:
                            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_favoriteRecipesFragment);
                            break;

                        case R.id.logOut:
                            // logging out from the system
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // starting activity for creating recipe
                    Intent intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                    intent.putExtra("currentUserID",currentUser.getId());
                    intent.putExtra("currentUserEmail", currentUser.getEmail());
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateNavState(int selectedItemID) {
        // update navigation view (makes selected item regarding current fragment)
        mNavigationView.setCheckedItem(selectedItemID);
    }

    public User getCurrentUser() {
        return currentUser;
    }

}

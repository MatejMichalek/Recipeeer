package com.example.recipeeer.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.recipeeer.R;
import com.example.recipeeer.createRecipe.CreateRecipeActivity;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.domain.UserViewModel;
import com.example.recipeeer.domain.UserViewModelFactory;
import com.example.recipeeer.login.LogInActivity;
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

public class MainActivity extends AppCompatActivity implements ActivityWithDrawer, WelcomeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MyRecipesFragment.OnFragmentInteractionListener,FavoriteRecipesFragment.OnFragmentInteractionListener {

    private User currentUser;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private UserViewModel mUserViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            mUserViewModel = ViewModelProviders.of(this,new UserViewModelFactory(getApplication(), mFirebaseUser.getEmail())).get(UserViewModel.class);
            mNavigationView = findViewById(R.id.nav_view);

            if (savedInstanceState != null)
                currentUser = mUserViewModel.getUser().getValue();

            mUserViewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        currentUser = user;
                        ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.headerUserName)).setText(user.getName());
                    }
                }
            });
            mUserViewModel.insert(new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(),2));

            Toolbar mToolbar = findViewById(R.id.mToolbar);
            setSupportActionBar(mToolbar);

            if (savedInstanceState == null)
                Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_welcomeFragment);


            mDrawerLayout = findViewById(R.id.drawerLayout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()) {
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
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                            startActivity(intent);
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

                    Intent intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                    intent.putExtra("currentUserID",mUserViewModel.getUser().getValue().getId());
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
        mNavigationView.setCheckedItem(selectedItemID);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

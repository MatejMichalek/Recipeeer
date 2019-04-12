package com.example.recipeeer.main;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.recipeeer.R;
import com.example.recipeeer.createRecipe.CreateRecipeActivity;
import com.example.recipeeer.login.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements ActivityWithDrawer, WelcomeFragment.OnFragmentInteractionListener, MyRecipesFragment.OnFragmentInteractionListener,FavoriteRecipesFragment.OnFragmentInteractionListener {

    private FirebaseUser mFirebaseUser;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toolbar mToolbar = findViewById(R.id.mToolbar);
            setSupportActionBar(mToolbar);


//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_welcomeFragment);

            mDrawerLayout = findViewById(R.id.drawerLayout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            mNavigationView = findViewById(R.id.nav_view);
            ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.headerUserName)).setText(mFirebaseUser.getDisplayName());

            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // set item as selected to persist highlight

                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    switch (menuItem.getItemId()) {
                        case R.id.mHome:
                            Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.action_global_welcomeFragment);
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

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                }
            });

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                    startActivity(intent);
//                    Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.createRecipeActivity);
                }
            });
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                TextView textView = findViewById(R.id.headerUserName);
//                textView.setText(mFirebaseUser.getDisplayName());
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateNavState(int selectedItemID) {
        mNavigationView.setCheckedItem(selectedItemID);
    }

//    @Override
//    public void backPressed() {
//        Navigation.findNavController(findViewById(R.id.content_frame)).navigateUp();
//    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Navigation.findNavController(findViewById(R.id.content_frame)).navigateUp();
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements ActivityWithDrawer, WelcomeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MyRecipesFragment.OnFragmentInteractionListener,FavoriteRecipesFragment.OnFragmentInteractionListener {

    private FirebaseUser mFirebaseUser;
    private User currentUser;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private UserViewModel mUserViewModel;


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


            mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            currentUser = mUserViewModel.getCurrentUserByEmail(mFirebaseUser.getEmail());

            mNavigationView = findViewById(R.id.nav_view);
//            ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.headerUserName)).setText(mFirebaseUser.getDisplayName());
            if (currentUser == null) {
                mUserViewModel.insert(new User(mFirebaseUser.getEmail(),mFirebaseUser.getDisplayName(),2));
                currentUser = mUserViewModel.getCurrentUserByEmail(mFirebaseUser.getEmail());
            }
            ((TextView)mNavigationView.getHeaderView(0).findViewById(R.id.headerUserName)).setText(currentUser.getName());


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

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                }
            });

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent replyIntent = new Intent();
//                    replyIntent.putExtra("email", "NewEmail");
//                    replyIntent.putExtra("name", "NewName");
//                    replyIntent.putExtra("age", 15);
//                    replyIntent.putExtra("gender", 2);
//                    setResult(RESULT_OK, replyIntent);

                    Intent intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                    intent.putExtra("currentUserID",currentUser.getId());
                    startActivity(intent);
////                    Navigation.findNavController(findViewById(R.id.content_frame)).navigate(R.id.createRecipeActivity);
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

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

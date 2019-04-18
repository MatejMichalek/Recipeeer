package com.example.recipeeer.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.RecipeViewModel;
import com.example.recipeeer.domain.SearchedRecipesListAdapter;
import com.example.recipeeer.domain.UserListAdapter;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;

public class SearchActivity extends AppCompatActivity implements SearchedRecipesListAdapter.OnSearchListItemClickListener {

    private RecipeViewModel mRecipeViewModel;
    private SearchedRecipesListAdapter adapter;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchedRecipesListAdapter(this,this);
        recyclerView.setAdapter(adapter);

        adapter.setSearchedRecipes(mRecipeViewModel.getSearchedRecipes("burger"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(String recipeID) {
        Toast.makeText(this,recipeID,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipeFromApiID",recipeID);
        intent.putExtra("currentUserID",userID);
        startActivity(intent);
    }
}

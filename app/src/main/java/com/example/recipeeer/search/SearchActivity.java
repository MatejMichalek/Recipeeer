package com.example.recipeeer.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipeeer.R;
import com.example.recipeeer.domain.PagingHelper;
import com.example.recipeeer.domain.RecipeListFromAPI;
import com.example.recipeeer.domain.RecipeViewModel;
import com.example.recipeeer.domain.SearchedRecipesListAdapter;
import com.example.recipeeer.domain.UserListAdapter;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;

public class SearchActivity extends AppCompatActivity implements SearchedRecipesListAdapter.OnSearchListItemClickListener {

    private RecipeViewModel mRecipeViewModel;
    private SearchedRecipesListAdapter adapter;
    private int userID;
    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userID = getIntent().getExtras().getInt("currentUserID");
        searchTerm = getIntent().getExtras().getString("searchTerm");


        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search \""+searchTerm+"\"");

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchedRecipesListAdapter(this,this);
        recyclerView.setAdapter(adapter);

        mRecipeViewModel.search(searchTerm,0);

//        adapter.setSearchedRecipes(mRecipeViewModel.getSearchedRecipes(searchTerm));
        mRecipeViewModel.getSearchedRecipes(searchTerm).observe(this, new Observer<RecipeListFromAPI>() {
            @Override
            public void onChanged(RecipeListFromAPI recipeListFromAPI) {
                adapter.setSearchedRecipes(recipeListFromAPI);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.previousPage:
                        mRecipeViewModel.search(searchTerm,-1);
                        break;
                    case R.id.nextPage:
                        mRecipeViewModel.search(searchTerm,1);
                        break;
                    default:
                        break;
                }
            }
        };
        final Button previousPage = findViewById(R.id.previousPage);
        final Button nextPage = findViewById(R.id.nextPage);

        previousPage.setOnClickListener(listener);
        nextPage.setOnClickListener(listener);

        mRecipeViewModel.getPagingHelper().observe(this, new Observer<PagingHelper>() {
            @Override
            public void onChanged(PagingHelper pagingHelper) {
                if (pagingHelper != null) {
                    previousPage.setEnabled(pagingHelper.canGoToPreviousPage());
                    nextPage.setEnabled(pagingHelper.canGoToNextPage());
                }
            }
        });

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

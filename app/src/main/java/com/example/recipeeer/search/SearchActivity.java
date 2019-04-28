package com.example.recipeeer.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.RecipeListFromAPI;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;

public class SearchActivity extends AppCompatActivity implements SearchedRecipesListAdapter.OnSearchListItemClickListener {

    private SearchViewModel mSearchViewModel;
    private SearchedRecipesListAdapter adapter;
    private int userID;
    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // try to get data from intent
        // userID used to check user's favorite recipes
        userID = getIntent().getExtras().getInt("currentUserID");
        searchTerm = getIntent().getExtras().getString("searchTerm");


        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search \""+searchTerm+"\"");

        // creates view model for this activity
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        // create recycler view and set adapter to it
        RecyclerView recyclerView = findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchedRecipesListAdapter(this,this);
        recyclerView.setAdapter(adapter);

        // start initial search where parameter page == 0
        mSearchViewModel.search(searchTerm,0);

        // start observing changes in the search list of recipes
        mSearchViewModel.getSearchedRecipes().observe(this, new Observer<RecipeListFromAPI>() {
            @Override
            public void onChanged(RecipeListFromAPI recipeListFromAPI) {
                // update adapter for recycler view
                adapter.setSearchedRecipes(recipeListFromAPI);
            }
        });

        // listener for paging buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.previousPage:
                        // request previous page of results from API
                        mSearchViewModel.search(searchTerm,-1);
                        break;
                    case R.id.nextPage:
                        // request next page of results from API
                        mSearchViewModel.search(searchTerm,1);
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

        // start observing changes in PagingHelper object that holds total no of results and also current offset
        mSearchViewModel.getPagingHelper().observe(this, new Observer<PagingHelper>() {
            @Override
            public void onChanged(PagingHelper pagingHelper) {
                if (pagingHelper != null) {
                    // update UI regarding the data in PagingHelper
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
        // start recipe detail activity on click on an item from recycler view
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipeFromApiID",recipeID);
        intent.putExtra("currentUserID",userID);
        startActivity(intent);
    }
}

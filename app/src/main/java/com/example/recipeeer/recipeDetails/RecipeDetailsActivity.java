package com.example.recipeeer.recipeDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.IngredientViewModel;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeFromAPI;
import com.example.recipeeer.domain.RecipeListFromAPI;
import com.example.recipeeer.domain.RecipeViewModel;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeViewModel recipeViewModel;
    private IngredientViewModel ingredientsViewModel;
    private TextView mRecipeTitle;
    private TextView mAuthor;
    private TextView mPreparationTime;
    private LinearLayout mIngredientsFrame;
    private TextView mInstructions;
    private ImageView mImage;
    private Integer currentUserID;
    private Object recipeID;
    private boolean isMyRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        isMyRecipe = checkIsMyRecipe();

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        mRecipeTitle = findViewById(R.id.recipeTitle);
        mAuthor = findViewById(R.id.author);
        if (isMyRecipe) {
            findViewById(R.id.authorFrame).setVisibility(View.GONE);
        }
        mPreparationTime = findViewById(R.id.preparationTime);
        mInstructions = findViewById(R.id.instructions);
        mIngredientsFrame = findViewById(R.id.ingredientsFrame);

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (isMyRecipe) {
            ingredientsViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);

            recipeViewModel.getRecipeById((int) recipeID).observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    if (recipe != null) {
                        mRecipeTitle.setText(recipe.getName());
                        mPreparationTime.setText(String.valueOf(recipe.getPreparationTime())+" min");
                        mInstructions.setText(recipe.getInstruction());
                    }
                }
            });

            ingredientsViewModel.getIngredientsForRecipe((int) recipeID).observe(this, new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(List<Ingredient> ingredients) {
                    displayIngredients(ingredients);
                }
            });
        }
        else {
            RecipeFromAPI recipe = recipeViewModel.getRecipeFromApi((String) recipeID);
            mRecipeTitle.setText(recipe.title);
            mAuthor.setText(String.valueOf(recipe.publisher));
            mPreparationTime.setText(String.valueOf(recipe.preparationTime)+" min");
            mInstructions.setText(recipe.instructions);
            displayIngredients(recipe.ingredients);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipe details");
    }

    private void displayIngredients(List<Ingredient> ingredients) {
        View view;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Toast.makeText(RecipeDetailsActivity.this,String.valueOf(ingredients.size()+" ingredients"),Toast.LENGTH_LONG).show();
        for (Ingredient i: ingredients) {
            view = inflater.inflate(R.layout.layout_recipe_details_ingredient_item,mIngredientsFrame,false);
            ((TextView) view.findViewWithTag("IngredientText")).setText(i.getValue());
            mIngredientsFrame.addView(view);
        }
    }

    private boolean checkIsMyRecipe() {
//        try {
        try {
            currentUserID = getIntent().getExtras().getInt("currentUserID");
        }
        catch (NullPointerException e) {
            finish();
            return false;
        }
        recipeID = getIntent().getExtras().get("recipeFromApiID");
        if (recipeID != null) {
            return false;
        }
        recipeID = getIntent().getExtras().get("recipeID");
        if (recipeID != null) {
            return true;
        }
        finish();
        return true;

//            int authorID = getIntent().getExtras().getInt("authorID");
//            return currentUserID==authorID;
//        }
//        catch (NullPointerException e) {
//            finish();
//            return false;
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                int result = recipeViewModel.delete(getIntent().getExtras().getInt("recipeID"));
                if (result == 1) {
                    finish();
                    return true;
                }
            case R.id.action_favorite:
                //TODO
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMyRecipe) {
            getMenuInflater().inflate(R.menu.my_recipe_details_bar_menu,menu);
        }
        else {
            getMenuInflater().inflate(R.menu.api_recipe_details_bar_menu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
}

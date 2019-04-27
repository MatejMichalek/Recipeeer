package com.example.recipeeer.recipeDetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.recipeeer.R;
import com.example.recipeeer.domain.Favorites;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeFromAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RecipeDetailsActivity extends AppCompatActivity {

    private DetailsViewModel mDetailsViewModel;
    private TextView mRecipeTitle;
    private TextView mAuthor;
    private TextView mPreparationTime;
    private LinearLayout mIngredientsFrame;
    private TextView mInstructions;
    private ImageView mImage;
    private Integer currentUserID;
    private Object recipeID;
    private boolean isMyRecipe;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        isMyRecipe = checkIsMyRecipe();

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        mRecipeTitle = findViewById(R.id.recipeTitle);
        mAuthor = findViewById(R.id.author);

        mPreparationTime = findViewById(R.id.preparationTime);
        mInstructions = findViewById(R.id.instructions);
        mIngredientsFrame = findViewById(R.id.ingredientsFrame);
        mImage = findViewById(R.id.recipeDetailsImage);

        mDetailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        if (isMyRecipe) {

            storageReference = FirebaseStorage.getInstance().getReference().child("users/"+String.valueOf(currentUserID)+"/recipes/"+String.valueOf((int) recipeID));

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(RecipeDetailsActivity.this)
                            .load(uri)
                            .apply(new RequestOptions().transform(new RoundedCorners(30), new FitCenter()))
                            .placeholder(R.drawable.img_not_found)
                            .into(mImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mImage.setVisibility(View.GONE);
                }
            });

            mDetailsViewModel.getRecipeById((int) recipeID).observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    if (recipe != null) {
                        mRecipeTitle.setText(recipe.getName());
                        mPreparationTime.setText(String.valueOf(recipe.getPreparationTime())+" min");
                        mInstructions.setText(recipe.getInstruction());
                    }
                }
            });

            mDetailsViewModel.getIngredientsForRecipe((int) recipeID).observe(this, new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(List<Ingredient> ingredients) {
                    displayIngredients(ingredients);
                }
            });

            if(mAuthor.getText().toString().trim().length()<1)
                ((LinearLayout) mAuthor.getParent()).setVisibility(View.GONE);
        }
        else {
            RecipeFromAPI recipe = mDetailsViewModel.getRecipeFromApi((String) recipeID);
            mRecipeTitle.setText(recipe.title);
            mAuthor.setText(String.valueOf(recipe.publisher));
            mPreparationTime.setText(String.valueOf(recipe.preparationTime)+" min");
            mInstructions.setText(recipe.instructions);

            Glide.with(this)
                    .load(recipe.imageURL)
                    .apply(new RequestOptions().transform(new RoundedCorners(30), new FitCenter()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .placeholder(R.drawable.img_not_found)
                    .into(mImage);

            displayIngredients(recipe.ingredients);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipe details");
    }

    private void displayFavorite(boolean isPresent, Menu menu) {
        if (isPresent) {
            menu.findItem(R.id.action_favorite).setVisible(false);
            menu.findItem(R.id.action_favourite_false).setVisible(true);
        }
        else {
            menu.findItem(R.id.action_favorite).setVisible(true);
            menu.findItem(R.id.action_favourite_false).setVisible(false);
        }
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                int result = mDetailsViewModel.delete((int)recipeID);
                if (result == 1) {
                    storageReference.delete();
                    finish();
                    return true;
                }
            case R.id.action_favorite:
                Toast.makeText(this,"Add favorite",Toast.LENGTH_LONG).show();
                int time  = Integer.parseInt(mPreparationTime.getText().toString().substring(0,mPreparationTime.getText().length()-4));
                mDetailsViewModel.insert(new Favorites(currentUserID,(String) recipeID,mRecipeTitle.getText().toString(),time));
                return false;

            case R.id.action_favourite_false:
                Toast.makeText(this,"Remove favorite",Toast.LENGTH_LONG).show();
                mDetailsViewModel.delete(new Favorites(currentUserID,(String) recipeID));
                return false;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (isMyRecipe) {
            getMenuInflater().inflate(R.menu.my_recipe_details_bar_menu,menu);
        }
        else {
            getMenuInflater().inflate(R.menu.api_recipe_details_bar_menu,menu);
            mDetailsViewModel.getFavoritesForUser(currentUserID).observe(this, new Observer<List<Favorites>>() {
                @Override
                public void onChanged(List<Favorites> favorites) {
                    boolean isPresent = false;
                    for (Favorites f: favorites) {
                        if (f.getRecipeId().equals(recipeID)) {
                            isPresent = true;
                            break;
                        }
                    }
                    displayFavorite(isPresent, menu);
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
}

package com.example.recipeeer.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.Favorites;
import com.example.recipeeer.main.ActivityWithDrawer;
import com.example.recipeeer.main.MainActivity;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FavoriteRecipesFragment extends Fragment implements FavoritesListAdapter.OnListItemClickListener {

    private FloatingActionButton fab;
    private FavoriteRecipesViewModel mFavoriteRecipesViewModel;
    private FavoritesListAdapter mAdapter;

    public FavoriteRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // creates view model for this fragment
        mFavoriteRecipesViewModel = ViewModelProviders.of(this).get(FavoriteRecipesViewModel.class);
        int currentUserId = ((MainActivity) getActivity()).getCurrentUser().getId();

        // start observing changes in user's favorite recipes
        mFavoriteRecipesViewModel.getFavoritesForUser(currentUserId).observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(List<Favorites> recipes) {
                mAdapter.setFavorites(recipes);
            }
        });

        fab = getActivity().findViewById(R.id.fab);

        // inflates recycler view and set adapter for it
        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FavoritesListAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // updates UI - FAB, Drawer and ActionBar
        fab.hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite recipes");
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.favorites);
    }

    @Override
    public void onListItemClick(String recipeID) {
        // starts activity for recipe details
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("currentUserID",((MainActivity) getActivity()).getCurrentUser().getId());
        intent.putExtra("recipeFromApiID",recipeID);
        startActivity(intent);
    }
}

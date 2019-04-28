package com.example.recipeeer.myRecipes;

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
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.main.ActivityWithDrawer;
import com.example.recipeeer.main.MainActivity;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyRecipesFragment extends Fragment implements MyRecipesListAdapter.OnListItemClickListener{

    private FloatingActionButton fab;
    private MyRecipesListAdapter mAdapter;

    public MyRecipesFragment() {
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
        MyRecipesViewModel myRecipesViewModel = ViewModelProviders.of(this).get(MyRecipesViewModel.class);

        // start observing changes in the list of my recipes
        myRecipesViewModel.getAllMyRecipes(((MainActivity) getActivity()).getCurrentUser().getEmail()).observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mAdapter.setMyRecipes(recipes);
            }
        });

        fab = getActivity().findViewById(R.id.fab);

        // inflates recycler view and sets adapter to it
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyRecipesListAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // updates UI - FAB, Drawer and ActionBar
        fab.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My recipes");
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.myRecipes); //just add this line
    }

    @Override
    public void onListItemClick(int recipeID) {
        // starts recipe details activity with recipe id from recycler view
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("currentUserID",((MainActivity) getActivity()).getCurrentUser().getId());
        intent.putExtra("currentUserEmail", ((MainActivity) getActivity()).getCurrentUser().getEmail());
        intent.putExtra("recipeID",recipeID);
        startActivity(intent);
    }
}

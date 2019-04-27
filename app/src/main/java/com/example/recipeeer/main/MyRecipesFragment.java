package com.example.recipeeer.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.MyRecipesListAdapter;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeViewModel;
import com.example.recipeeer.recipeDetails.RecipeDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecipesFragment extends Fragment implements MyRecipesListAdapter.OnListItemClickListener{

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton fab;
    private RecipeViewModel mRecipeViewModel;
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

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeViewModel.getAllMyRecipes(FirebaseAuth.getInstance().getCurrentUser().getEmail()).observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mAdapter.setMyRecipes(recipes);
            }
        });


        // Inflate the layout for this fragment
        fab = getActivity().findViewById(R.id.fab);

        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_myRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MyRecipesListAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My recipes");
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.myRecipes); //just add this line
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(int recipeID) {
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("currentUserID",((MainActivity) getActivity()).getCurrentUser().getId());
        intent.putExtra("recipeID",recipeID);
        Toast.makeText(getActivity(),"Recipe: "+String.valueOf(recipeID)+" CurrentUser: "+String.valueOf(((MainActivity) getActivity()).getCurrentUser().getId()),Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

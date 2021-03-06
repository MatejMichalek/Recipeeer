package com.example.recipeeer.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.QuickSearchItem;
import com.example.recipeeer.search.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFragment extends Fragment implements OnSearchIconClickListener {

    private FloatingActionButton fab;
    private EditText searchEdit;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fab = getActivity().findViewById(R.id.fab);

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        // creates recycler view with grid layout
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_quickSearch);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(manager);

        // set and populate adapter for recycler view
        QuickSearchAdapter adapter = new QuickSearchAdapter(loadQuickSearchItems(), this);
        recyclerView.setAdapter(adapter);

        final Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEdit.getText().toString().trim();
                startSearch(searchTerm);

            }
        });

        searchEdit = view.findViewById(R.id.searchEdit);
        // listener to check if input fields contains smth or is empty --> enable/disable search button
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchEdit.getText().toString().trim().length()>0) {
                    searchButton.setEnabled(true);
                }
                else {
                    searchButton.setEnabled(false);
                }
            }
        });

        return view;
    }

    // starting search activity with searchTerm from editView
    private void startSearch(String searchTerm) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("currentUserID",((MainActivity) getActivity()).getCurrentUser().getId());
        intent.putExtra("searchTerm",searchTerm.toLowerCase());
        startActivity(intent);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // updates UI - FAB, Drawer and ActionBar
        fab.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.mHome); //just add this line
    }

    @Override
    public void onSearchIconClick(String name) {
        startSearch(name);
    }

    // create list of quick search items
    private List<QuickSearchItem> loadQuickSearchItems() {
        List<QuickSearchItem> items = new ArrayList<>();
        items.add(new QuickSearchItem("Chicken",R.drawable.icons8_chicken_50));
        items.add(new QuickSearchItem("Cookies",R.drawable.icons8_cookies_50));
        items.add(new QuickSearchItem("Beef",R.drawable.icons8_cow_50));
        items.add(new QuickSearchItem("Doughnut",R.drawable.icons8_doughnut_filled_50));
        items.add(new QuickSearchItem("Fish",R.drawable.icons8_fish_food_50));
        items.add(new QuickSearchItem("Grill",R.drawable.icons8_grill_filled_50));
        items.add(new QuickSearchItem("Burger",R.drawable.icons8_hamburger_filled_50));
        items.add(new QuickSearchItem("Soup",R.drawable.icons8_hot_springs_filled_50));
        items.add(new QuickSearchItem("Chili",R.drawable.icons8_chili_pepper_50));
        items.add(new QuickSearchItem("Mushroom",R.drawable.icons8_mushroom_50));
        items.add(new QuickSearchItem("Pancake",R.drawable.icons8_pancake_50));
        items.add(new QuickSearchItem("Pork",R.drawable.icons8_pig_50));
        items.add(new QuickSearchItem("Pizza",R.drawable.icons8_pizza_filled_50));
        items.add(new QuickSearchItem("Potato",R.drawable.icons8_potato_50));
        items.add(new QuickSearchItem("Pasta",R.drawable.icons8_spaghetti_50));
        items.add(new QuickSearchItem("Vegan",R.drawable.icons8_vegan_food_50));
        return items;
    }
}

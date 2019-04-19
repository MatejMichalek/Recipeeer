package com.example.recipeeer.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.recipeeer.domain.QuickSearchItem;
import com.example.recipeeer.domain.RecipeeerDatabase;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.domain.UserListAdapter;
import com.example.recipeeer.domain.UserViewModel;
import com.example.recipeeer.search.OnSearchIconClickListener;
import com.example.recipeeer.search.QuickSearchAdapter;
import com.example.recipeeer.search.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipeeer.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * createService an instance of this fragment.
 */
public class WelcomeFragment extends Fragment implements OnSearchIconClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private FloatingActionButton fab;
    private UserViewModel mUserViewModel;
    private UserListAdapter adapter;
    private QuickSearchAdapter mAdapter;
    private EditText searchEdit;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to createService a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
//                adapter.setUsers(users);
            }
        });

        // Inflate the layout for this fragment
        fab = getActivity().findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User("NewEmail","NewName",15,2);
//                mUserViewModel.insert(user);
//
//            }
//        });



        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_users);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        adapter = new UserListAdapter(getActivity());
//        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_quickSearch);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(manager);

        mAdapter = new QuickSearchAdapter(loadQuickSearchItems(),this);
        recyclerView.setAdapter(mAdapter);

        final Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEdit.getText().toString().trim();
                startSearch(searchTerm);

            }
        });

        searchEdit = view.findViewById(R.id.searchEdit);
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

    private void startSearch(String searchTerm) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("currentUserID",((MainActivity) getActivity()).getCurrentUser().getId());
        intent.putExtra("searchTerm",searchTerm.toLowerCase());
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        fab.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.mHome); //just add this line
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
    public void onSearchIconClick(String name) {
        startSearch(name);
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

//    @Override
//    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//        super.onInflate(context, attrs, savedInstanceState);
//        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.home); //just add this line
//    }


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

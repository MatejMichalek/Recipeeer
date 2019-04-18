package com.example.recipeeer.main;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.domain.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * createService an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
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
    private Spinner mSpinner;
    private AlertDialog mDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to createService a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        fab = getActivity().findViewById(R.id.fab);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    ((TextView) getActivity().findViewById(R.id.userEmailText)).setText(user.getEmail());
                    ((EditText) getActivity().findViewById(R.id.usernameEdit)).setText(user.getName());

                    mSpinner.setSelection(user.getGender());
                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton editButton = view.findViewById(R.id.editUsernameButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        createEditDialog();

        mSpinner = view.findViewById(R.id.userGenderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, R.layout.layout_spinner_item);
        adapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUserViewModel.updateUserGender(((TextView) getActivity().findViewById(R.id.userEmailText)).getText().toString(),position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void createEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Edit username");
//        builder.setMessage("This is the example code snippet to disable button if edittext attached to mDialog is empty.");
        DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:
                        updateUserInDB();
                        dialog.cancel();
                        Toast.makeText(getActivity(),"Save clicked",Toast.LENGTH_LONG).show();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        dialog.cancel();
                        Toast.makeText(getActivity(),"Cancel clicked",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }

            private void updateUserInDB() {
                String email = ((TextView) getActivity().findViewById(R.id.userEmailText)).getText().toString();
                String username = ((EditText) mDialog.findViewById(R.id.dialogEditName)).getText().toString().trim();
                mUserViewModel.updateUsername(email,username);
            }
        };
        builder.setPositiveButton("Save", dialogOnClickListener);
        builder.setNegativeButton("Cancel", dialogOnClickListener);

        View view = getLayoutInflater().inflate(R.layout.edit_username_dialog_layout,null);
        builder.setView(view);
        mDialog = builder.create();

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ((EditText)mDialog.findViewById(R.id.dialogEditName)).setText("");
            }
        });
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((EditText)mDialog.findViewById(R.id.dialogEditName)).setHint(((EditText) getActivity().findViewById(R.id.usernameEdit)).getText());
            }
        });

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
        fab.hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My profile");
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.profile);


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

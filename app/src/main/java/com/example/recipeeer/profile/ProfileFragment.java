package com.example.recipeeer.profile;

import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.main.ActivityWithDrawer;
import com.example.recipeeer.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FloatingActionButton fab;
    private ProfileViewModel mProfileViewModel;
    private Spinner mSpinner;
    private AlertDialog mDialog;

    public ProfileFragment() {
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

        // create view model for this fragment
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        // start observing changes in current user data
        mProfileViewModel.getUser(((MainActivity) getActivity()).getCurrentUser().getEmail()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // update UI
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
                // opens dialog
                mDialog.show();
            }
        });

        // create dialog for editing user name
        createEditDialog();

        // create spinner with data from source and set adapter for it
        mSpinner = view.findViewById(R.id.userGenderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, R.layout.layout_spinner_item);
        adapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        return view;
    }

    private void createEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit username");
        DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:
                        // update local data stored in DB
                        updateUserInDB();
                        dialog.cancel();
                        Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                    default:
                        break;
                }
            }

            private void updateUserInDB() {
                String email = ((TextView) getActivity().findViewById(R.id.userEmailText)).getText().toString();
                String username = ((EditText) mDialog.findViewById(R.id.dialogEditName)).getText().toString().trim();
                mProfileViewModel.updateUsername(email,username);
            }
        };
        // configure buttons in dialog
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

    @Override
    public void onResume() {
        super.onResume();
        // updates UI - FAB, Drawer and ActionBar
        fab.hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My profile");
        ((ActivityWithDrawer) getActivity()).updateNavState(R.id.profile);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // update local storage with new data from spinner
        mProfileViewModel.updateUserGender(((TextView) getActivity().findViewById(R.id.userEmailText)).getText().toString(),position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

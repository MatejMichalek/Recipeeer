package com.example.recipeeer.createRecipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.recipeeer.R;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.IngredientViewModel;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;


public class CreateRecipeActivity extends AppCompatActivity {

    private static final int GALLERY = 111;
    private static final int CAMERA = 222;

    private ImageView image;
    private RecipeViewModel mRecipeViewModel;
    private IngredientViewModel mIngredientViewModel;
    private EditText editName;
    private EditText editPreparationTime;
    private EditText editIngredients;
    private EditText editInstructions;
    private int currentUserID;
    private AppCompatButton addIngredientButton;
    private LinearLayout addedIngredientsFrame;
    private Button saveRecipeBtn;
    private boolean isImageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        currentUserID = getIntent().getExtras().getInt("currentUserID");

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("New recipe");
        }

        Button uploadImgBtn = findViewById(R.id.uploadImage_btn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        mIngredientViewModel = ViewModelProviders.of(CreateRecipeActivity.this).get(IngredientViewModel.class);

        editName = findViewById(R.id.editName);
        image = findViewById(R.id.recipeImage);
        editPreparationTime = findViewById(R.id.editTime);
        editInstructions = findViewById(R.id.editInstructions);
        editIngredients = findViewById(R.id.editIngredient);
        addedIngredientsFrame = findViewById(R.id.ingredients);
        addIngredientButton = findViewById(R.id.addIngredient);
        saveRecipeBtn = findViewById(R.id.saveRecipe);



        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_ingredient_item,addedIngredientsFrame,false);
                Ingredient ingredient = new Ingredient(editIngredients.getText().toString().trim());
                int ingredientNo = mIngredientViewModel.addIngredient(ingredient);
                ((TextView) view.findViewWithTag("IngredientText")).setText(editIngredients.getText().toString());
                ((LinearLayout) view).getChildAt(2).setTag(ingredientNo-1);

                editIngredients.setText("");
                addedIngredientsFrame.addView(view);
                checkInputField();
            }
        });

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);


        TextWatcher mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                // to disable add ingredient button when edit field is empty
                if (getCurrentFocus().getId() == editIngredients.getId())
                    addIngredientButton.setEnabled(editIngredients.getText().toString().trim().length()>0);
                // checking input field to enable/disable save button
                else
                    checkInputField();
            }
        };

        editName.addTextChangedListener(mWatcher);
        editPreparationTime.addTextChangedListener(mWatcher);
        editIngredients.addTextChangedListener(mWatcher);
        editInstructions.addTextChangedListener(mWatcher);

        saveRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe(editName.getText().toString().trim(),Integer.parseInt(editPreparationTime.getText().toString().trim()),editInstructions.getText().toString().trim(),currentUserID);
                int id = mRecipeViewModel.insert(recipe);
                mIngredientViewModel.insertIngredientsForRecipe(id);
                if (isImageSelected) {
                    storeIntoFirebase(id);
                }
                Toast.makeText(CreateRecipeActivity.this,String.valueOf(id),Toast.LENGTH_LONG).show();
                finish();
            }
        });

        isImageSelected = false;
    }

    private void storeIntoFirebase(int id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("users/"+String.valueOf(currentUserID)+"/recipes/"+String.valueOf(id));
        //create a file to write bitmap data
        final File file = new File(getBaseContext().getCacheDir(), String.valueOf(id));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("FSTORAGE","Cannot crete file");
        }

        //Convert bitmap to byte array
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("FSTORAGE","Cannot find file for output stream");
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            Log.i("FSTORAGE","Cannot write to output stream");
        }


        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("FSTORAGE","Cannot find file for input stream");
        }

        UploadTask uploadTask = storageReference.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("FSTORAGE","Unsuccessfully uploaded, exception: "+exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.i("FSTORAGE","Successfully uploaded, path: "+taskSnapshot.getMetadata().getPath());
                Log.i("FSTORAGE","File is deleted = "+file.delete());
            }
        });
    }

    private void showPictureDialog() {
        MaterialAlertDialogBuilder pictureDialog = new MaterialAlertDialogBuilder(this);
        pictureDialog.setTitle("Select action");

        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }

                    private void choosePhotoFromGallery() {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY);
                    }

                    private void takePhotoFromCamera() {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                    }
                });
        AlertDialog dialog = pictureDialog.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                isImageSelected = true;
                return false;
            }
        };

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                Glide.with(this).load(contentURI).listener(requestListener).placeholder(R.drawable.img_not_found).fitCenter().into(image);
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                Glide.with(this).load(thumbnail).listener(requestListener).placeholder(R.drawable.img_not_found).fitCenter().into(image);
            }
            else
                Toast.makeText(this,"Could not upload image",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkInputField() {
        boolean isReady =
                editName.getText().toString().trim().length()>0
                && editPreparationTime.getText().toString().trim().length()>0
                && addedIngredientsFrame.getChildCount()>0
                && editInstructions.getText().toString().trim().length()>0;
        if (isReady) {
            saveRecipeBtn.setEnabled(true);
        }
        else {
            saveRecipeBtn.setEnabled(false);
        }
    }

    public void removeIngredient(View view) {
        int ingredientIndex = Integer.parseInt(view.getTag().toString());
        int noOfLeftIngredients =  mIngredientViewModel.removeIngredient(ingredientIndex);
        for (int i = ingredientIndex+1;i<=noOfLeftIngredients;i++) {
            addedIngredientsFrame.getChildAt(i).findViewWithTag(i).setTag(i-1);
        }
        addedIngredientsFrame.removeView((View) view.getParent());
        ((LinearLayout) view.getParent()).setVisibility(View.GONE);
        checkInputField();
    }
}

package com.example.recipeeer.createRecipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeViewModel;
import com.example.recipeeer.domain.User;
import com.example.recipeeer.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class CreateRecipeActivity extends AppCompatActivity {

    private static final int GALLERY = 111;
    private static final int CAMERA = 222;
    private static final String IMAGE_DIRECTORY = "recipes_images";

    private ImageView image;
    private RecipeViewModel mRecipeViewModel;
    private EditText editName;
    private EditText editPreparationTime;
    private EditText editIngredients;
    private EditText editInstructions;
    private int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        currentUserID = getIntent().getExtras().getInt("currentUserID");

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button uploadImgBtn = findViewById(R.id.uploadImage_btn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog(v);
            }
        });

        editName = findViewById(R.id.editName);
        editPreparationTime = findViewById(R.id.editTime);
        editIngredients = findViewById(R.id.editIngredient);
        editInstructions = findViewById(R.id.editInstructions);
        image = findViewById(R.id.recipeImage);



        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        final Button saveRecipeBtn = findViewById(R.id.saveRecipe);
        TextWatcher mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkInputField()) {
                    saveRecipeBtn.setEnabled(true);
                }
                else {
                    saveRecipeBtn.setEnabled(false);
                }
            }

            private boolean checkInputField() {
                return editName.getText().length()>0 && editPreparationTime.getText().length()>0 && editIngredients.getText().length()>0 && editInstructions.getText().length()>0;
            }
        };
        editName.addTextChangedListener(mWatcher);
        editPreparationTime.addTextChangedListener(mWatcher);
        editIngredients.addTextChangedListener(mWatcher);
        editInstructions.addTextChangedListener(mWatcher);

        saveRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe(editName.getText().toString(),Integer.parseInt(editPreparationTime.getText().toString()),editInstructions.getText().toString(),currentUserID);
                int id = mRecipeViewModel.insert(recipe);
                Toast.makeText(CreateRecipeActivity.this,String.valueOf(id),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPictureDialog(View v) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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
        pictureDialog.show(); 
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ((ActivityWithDrawer)getParent()).backPressed();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    String path = saveImage(bitmap);
                    Toast.makeText(CreateRecipeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    image.setImageURI(contentURI);
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateRecipeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
//            saveImage(thumbnail);
            Toast.makeText(CreateRecipeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImage(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}

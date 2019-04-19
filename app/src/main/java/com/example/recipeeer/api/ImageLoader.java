package com.example.recipeeer.api;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.http.Url;

public class ImageLoader {
    public static Drawable LoadImageFromWeb(String str, AppCompatActivity activity) {

//            InputStream is = (InputStream) new Url(url);
//            Drawable d = Drawable.createFromStream(is,url);
//            return d;

        Drawable createFromStream;
        IOException e;
        try {

            InputStream open = activity.getAssets().open(str);
            createFromStream = Drawable.createFromStream(open, str);
            try {
                open.close();
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                return createFromStream;
            }
        } catch (IOException e3) {
            IOException iOException = e3;
            createFromStream = null;
            e = iOException;
            e.printStackTrace();
            return createFromStream;
        }
        return createFromStream;

    }
}

package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    private TextView nom;
    private String authority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.nom = (TextView) findViewById(R.id.textnom);
        authority = getResources().getString(R.string.authority);
        SharedPreferences pref=getSharedPreferences("save", Context.MODE_PRIVATE);
        String nomSaved = pref.getString("nomSaved","");

        nom.setText("Hey "+nomSaved+" !");

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("save", Context.MODE_PRIVATE);
        boolean hasSaved = pref.getBoolean("hasSaved", false);

        if(hasSaved)
        {
            System.out.println("ok");
        }

    }

    public void b_aliment(View view) {
        Intent iii = new Intent(getApplicationContext(),Ajout_aliment.class);
        startActivity(iii);
    }

    public void importCvs(View view) {

        AssetManager assetManager = getAssets();

        ContentResolver contentResolver = getContentResolver();




        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("calorie.csv")));
            String str = "";
            int i = 0;

            while ((str = br.readLine()) != null) {
                if (i != 0) {
                    String[] as = str.split(";");
                    String key = new String(as[0].getBytes(), "UTF-8");
                    Double tmp = Double.valueOf(as[1].replace("-", "0").replace("traces","0"));
                    if (!tmp.isNaN()) {

                        Uri.Builder builder = new Uri.Builder();

                        builder.scheme("content")
                                .authority(authority)
                                .appendPath("aliment");

                        Uri uri = builder.build();

                        int value = tmp.intValue();

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("aliment",key);
                        contentValues.put("calories",value);

                        uri = contentResolver.insert(uri, contentValues);
                        Log.d("DEBUG: ", "Apres le uri");

                    }
                }

                System.out.println(i++);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

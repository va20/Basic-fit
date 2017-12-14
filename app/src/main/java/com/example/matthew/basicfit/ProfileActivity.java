package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileActivity extends AppCompatActivity {

    private static String authority;
    private Button import_cvs;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_aliment:
                    intent_add();
                    return true;
                case R.id.settings:
                    return true;
                case R.id.home:
                    intent_home();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.import_cvs = (Button) findViewById(R.id.b_import);
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

    public void intent_add(){
        Intent intent = new Intent();
        intent.setClass(this,Ajout_aliment.class);
        this.startActivity(intent);
        ProfileActivity.this.finish();
    }

    public void intent_home(){
        Intent intent= new Intent();
        intent.setClass(this,HomeActivity.class);
        this.startActivity(intent);
        ProfileActivity.this.finish();
    }
}

package com.example.matthew.basicfit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileActivity extends AppCompatActivity {

    private static String authority;
    private Button import_cvs;
    private Button ajouter;
    private TextView calories;
    private TextView objectif;
    private EditText bdd_aliment;
    private EditText b_calories;

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
        authority = getResources().getString(R.string.authority);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.import_cvs = (Button) findViewById(R.id.b_import);
        this.ajouter = (Button) findViewById(R.id.b_ok);
        this.bdd_aliment = (EditText) findViewById(R.id.bdd_aliment);
        this.b_calories = (EditText) findViewById(R.id.b_calorie);
        this.objectif = (TextView) findViewById(R.id.nb_cal);
        SharedPreferences pref=getSharedPreferences("save", Context.MODE_PRIVATE);
        int nb_calories = pref.getInt("caloriesSaved",0);
        this.objectif.setText(Integer.toString(nb_calories));


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

    private boolean checkEditText() {
        return !bdd_aliment.getText().toString().equals("") && !b_calories.getText().toString().equals("");
    }

    public void add(View view) {

        ContentResolver resolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();

        ContentValues values = new ContentValues();

        builder.scheme("content").authority(authority).appendPath("aliment");

        Uri uri = builder.build();


        if (checkEditText()) {
            String nom_aliment = bdd_aliment.getText().toString();
            int nb_calories = Integer.parseInt(b_calories.getText().toString());

            values.put("aliment", nom_aliment);
            values.put("calories", nb_calories);

            uri = resolver.insert(uri, values);
            Toast.makeText(getApplicationContext(), "l'aliment a bien été rajouté", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("TOAST: ", "DANS LE ELSE");
            Toast.makeText(getApplicationContext(), "Les champs ne sont pas correctement remplis", Toast.LENGTH_SHORT).show();
        }

    }

    public void modifier(View view){
        ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.switch_edit_text);
        EditText change_nb_calories = (EditText) findViewById(R.id.change_nb_calories);
        TextView nb_cal = (TextView) findViewById(R.id.nb_cal);
        if(viewSwitcher.getCurrentView()==change_nb_calories){
            if(!change_nb_calories.getText().toString().equals("")){
                int cal_tmp=Integer.parseInt(change_nb_calories.getText().toString());
                nb_cal.setText(change_nb_calories.getText().toString());
                SharedPreferences prefs = getSharedPreferences("save", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("caloriesSaved",cal_tmp);
                editor.apply();
                viewSwitcher.showPrevious();
            }
            else{
                viewSwitcher.showPrevious();
            }
        }
        else if(viewSwitcher.getCurrentView()==nb_cal){
            viewSwitcher.showNext();
        }
    }

    public void intent_add(){
        Intent intent = new Intent();
        intent.setClass(this,Ajout_aliment_repas.class);
        this.startActivity(intent);

    }

    public void intent_home(){
        Intent intent= new Intent();
        intent.setClass(this,HomeActivity.class);
        this.startActivity(intent);
    }
}

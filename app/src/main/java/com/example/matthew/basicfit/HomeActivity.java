package com.example.matthew.basicfit;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity {
    private TextView nom;
    private TextView objectif;
    private Spinner jours_spinner;
    private GraphView graphView;
    private String authority;

    private Intent intent;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_aliment:
                    intent_add();
                    return true;
                case R.id.settings:
                    intent_settings();
                    return true;
                case R.id.home:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        authority = getResources().getString(R.string.authority);

        this.nom = (TextView) findViewById(R.id.textnom);
        this.objectif = (TextView) findViewById(R.id.textObjectif);
        jours_spinner = (Spinner) findViewById(R.id.jours_spinner);

        graphView = (GraphView) findViewById(R.id.graphview);

        ArrayList<String> list = getDate();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);

        jours_spinner.setAdapter(arrayAdapter);

        jours_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TreeMap<String, String> treeMap = getKcalMeal(adapterView.getSelectedItem().toString());
                graphView.setMap(treeMap);

                for (String s : treeMap.keySet()) {
                    Log.e("TREEMAP ", s + ": " + treeMap.get(s));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences pref = getSharedPreferences("save", Context.MODE_PRIVATE);
        String nomSaved = pref.getString("nomSaved", "");
        int nb_calories = pref.getInt("caloriesSaved", 0);
        String calories = Integer.toString(nb_calories);
        nom.setText("Hey " + nomSaved + " !");
        this.objectif.setText("/" + calories + "Kcal");

    }

    public TreeMap<String,String> getKcalMeal(String date) {
        TreeMap<String,String> treeMap = new TreeMap<String,String>();

        ContentResolver contentResolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();

        Uri uri = builder.scheme("content").authority(authority).appendPath("matin").build();

        String[] projection = {AlimentContentProvider.STRING_DATE,AlimentContentProvider.STRING_CALORIES};

        Cursor cursor = contentResolver.query(uri, projection, "date LIKE ?",new String[]{date+"%"}, null);

        if (cursor != null) {
            String query = "";
            int i = 0;
            while (cursor.moveToNext()) {
                String key = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_DATE));
                String value = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
                treeMap.put(key,value);
            }
        }

        Uri.Builder builder1 = new Uri.Builder();

        Uri uri1 = builder1.scheme("content").authority(authority).appendPath("midi").build();

        cursor = contentResolver.query(uri1, projection, "date LIKE ?",new String[]{date+"%"}, null);

        if (cursor != null) {
            String query = "";
            int i = 0;
            while (cursor.moveToNext()) {
                String key = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_DATE));
                String value = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
                treeMap.put(key,value);
            }
        }

        Uri.Builder builder2 = new Uri.Builder();

        Uri uri2 = builder2.scheme("content").authority(authority).appendPath("soir").build();

        cursor = contentResolver.query(uri2, projection, "date LIKE ?",new String[]{date+"%"}, null);

        if (cursor != null) {
            String query = "";
            int i = 0;
            while (cursor.moveToNext()) {
                String key = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_DATE));
                String value = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
                treeMap.put(key,value);
            }
        }

        return treeMap;
    }

    public ArrayList<String> getDate(){
        ArrayList<String> liste_date = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath("matin").build();

        String[] projection = {AlimentContentProvider.STRING_DATE};

        Cursor cursor = contentResolver.query(uri, projection, null,null, null);
        if (cursor != null) {
            String query = "";
            int i = 0;
            while (cursor.moveToNext()) {
                if (i == 7) break;
                liste_date.add(cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_DATE)).substring(0,10));
                i++;
            }
        }
        return liste_date;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("save", Context.MODE_PRIVATE);
        boolean hasSaved = pref.getBoolean("hasSaved", false);

        if (hasSaved) {
            System.out.println("ok");
        }

    }

    public void intent_add() {
        intent = new Intent();
        intent.setClass(this, Ajout_aliment_repas.class);
        this.startActivity(this.intent);
    }

    public void intent_settings() {
        intent = new Intent();
        intent.setClass(this, ProfileActivity.class);
        this.startActivity(this.intent);
    }

}

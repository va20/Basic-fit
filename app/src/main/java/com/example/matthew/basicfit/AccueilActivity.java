package com.example.matthew.basicfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccueilActivity extends AppCompatActivity {

    private Button save;
    private EditText nom;
    private EditText nb_cal;
    public static final String PREFS_NAME = "save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        this.save = (Button) findViewById(R.id.save);
        this.nom = (EditText) findViewById(R.id.nom);
        this.nb_cal = (EditText) findViewById(R.id.nb_cal);

        /*SharedPreferences sharedPrefs = getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("save", Context.MODE_PRIVATE);
        boolean hasSaved = pref.getBoolean("hasSaved", false);

        if(hasSaved)
        {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            AccueilActivity.this.finish();
        }

    }

    public void save(View view){
        view = this.save;
        if(nom.getText().length()!=0 && nb_cal.getText().length()!=0){
            SharedPreferences prefs = getSharedPreferences("save", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("hasSaved", true);
            editor.putString("nomSaved",nom.getText().toString());
            editor.putInt("caloriesSaved",Integer.parseInt(nb_cal.getText().toString()));
            editor.apply();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            AccueilActivity.this.finish();
        }

    }
}

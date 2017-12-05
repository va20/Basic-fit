package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Ajout_aliment extends AppCompatActivity {

    private String authority;
    private EditText et_aliment, et_calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_aliment);

        authority = getResources().getString(R.string.authority);

        et_aliment = (EditText) findViewById(R.id.b_aliment);
        et_calories = (EditText) findViewById(R.id.b_calorie);

    }

    public void ok(View view) {

        ContentResolver resolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();

        ContentValues values = new ContentValues();

        builder.scheme("content").authority(authority).appendPath("aliment");

        Uri uri = builder.build();

        if (!et_aliment.getText().toString().equals("") && !et_calories.getText().toString().equals("")) {
            String nom_aliment = et_aliment.getText().toString();
            int nb_calories = Integer.parseInt(et_calories.getText().toString());

            values.put("aliment", nom_aliment);
            values.put("calories", nb_calories);

            uri = resolver.insert(uri, values);
            Toast.makeText(getApplicationContext(), "l'aliment a bien été rajouté", Toast.LENGTH_SHORT);
        }
        else {
            Toast.makeText(getApplicationContext(), "Les champs ne sont pas correctement remplis", Toast.LENGTH_SHORT);
        }

    }
}

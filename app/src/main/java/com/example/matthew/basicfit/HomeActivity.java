package com.example.matthew.basicfit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView nom;
    private TextView objectif;
    private Spinner jours_spinner;

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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.nom = (TextView) findViewById(R.id.textnom);
        this.objectif = (TextView) findViewById(R.id.textObjectif);
        jours_spinner = (Spinner) findViewById(R.id.jours_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_jours, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        jours_spinner.setAdapter(adapter);

        SharedPreferences pref=getSharedPreferences("save", Context.MODE_PRIVATE);
        String nomSaved = pref.getString("nomSaved","");
        int nb_calories = pref.getInt("caloriesSaved",0);
        String  calories = Integer.toString(nb_calories);
        nom.setText("Hey "+nomSaved+" !");
        this.objectif.setText("Objectif du jour "+calories +"Kcal");

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

    public void intent_add(){
        intent = new Intent();
        intent.setClass(this,Ajout_aliment_repas.class);
        this.startActivity(this.intent);
    }

    public void intent_settings(){
        intent = new Intent();
        intent.setClass(this,ProfileActivity.class);
        this.startActivity(this.intent);
    }

}

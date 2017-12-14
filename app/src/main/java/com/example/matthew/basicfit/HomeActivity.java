package com.example.matthew.basicfit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView nom;
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

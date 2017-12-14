package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Ajout_aliment extends AppCompatActivity {
    private Button ok;
    private static String authority;
    private EditText et_aliment, et_calories;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_aliment:
                    return true;
                case R.id.settings:
                    intent_settings();
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
        setContentView(R.layout.activity_ajout_aliment);

        authority = getResources().getString(R.string.authority);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        et_calories = (EditText) findViewById(R.id.b_calorie);
        this.ok=(Button)findViewById(R.id.b_ok);
    }

    private boolean checkEditText() {
        return !et_aliment.getText().toString().equals("") &&
                !et_calories.getText().toString().equals("");
    }

    /*
    * La fonction "ok" sert à ajouter un aliment dans la base de donnée.
     */


    public void intent_settings(){
        Intent intent = new Intent();
        intent.setClass(this,ProfileActivity.class);
        this.startActivity(intent);
        Ajout_aliment.this.finish();
    }

    public void intent_home(){
        Intent intent = new Intent();
        intent.setClass(this,HomeActivity.class);
        this.startActivity(intent);
        Ajout_aliment.this.finish();
    }
}

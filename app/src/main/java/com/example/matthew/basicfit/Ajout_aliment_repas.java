package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/*
*
* TODO: - Faire la fonction calcule de calories (utiliser les calories du retour de la requête).
* TODO: - Retirer les farines de sarrasin par les vraies valeur.
*
 */

public class Ajout_aliment_repas extends AppCompatActivity {

    private String authority;
    private EditText et_aliment, et_gramme;
    private ListView listView;
    private Button b_chercher, b_ajouter;
    private RadioButton rb_matin, rb_midi, rb_soir;
    private ListAdapter listAdapter;


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
        setContentView(R.layout.activity_ajout_aliment_repas);

        authority = getResources().getString(R.string.authority);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.list);

        et_aliment = (EditText) findViewById(R.id.et_aliment);
        et_gramme = (EditText) findViewById(R.id.et_gramme);


        rb_matin = (RadioButton) findViewById(R.id.radio_matin);
        rb_midi = (RadioButton) findViewById(R.id.radio_midi);
        rb_soir = (RadioButton) findViewById(R.id.radio_soir);

        b_chercher = (Button) findViewById(R.id.b_chercher);
        b_ajouter = (Button) findViewById(R.id.b_ok);
    }

    /*
    * Calcul le nombre de calories pour 100g dans l'activité acitivty_ajout_aliment_repas.xml
    * @param gramme nombre de gramme
    * @return nombre de calories pour 100g
     */

    public int calcul_calories(int gramme) {
        return 0;
    }


    /*
    * Check si les EditText sont vides
    * @return boolean
     */

    private boolean checkEditText() {
        return !et_aliment.getText().toString().equals("") && !et_gramme.getText().toString().equals("");
    }


    /*
    * Check si le string en parametre est un chiffre
    * @param str string representant un chiffre
    * @return boolean si le string represente un int
    *
     */
    public static boolean isNumeric(String str) {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    /*
    * Retourne le nombre de calorie d'un aliment passé en parametre
    * @param aliment Nom de l'aliment
    * @return le nombre de calorie de l'aliment
    *
     */

    public int getCalorie(String aliment) {

        ContentResolver  contentResolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("content").authority(authority).appendPath("aliment");

        Uri uri = builder.build();

        Cursor cursor = contentResolver.query(uri, new String[]{AlimentContentProvider.STRING_CALORIES}, "aliment = ?", new String[]{aliment},null);

        String query = "";

        if (cursor != null) {
            while (cursor.moveToNext()) {
                query = cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
            }

            if (isNumeric(query)) {
                return Integer.parseInt(query);
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }


    /*
    * Action des buttons de l'activité Ajout_aliment_repas.
    *
    * le button "AJOUTER REPAS" ajoute à la base de donnée le repas avec quand il a été prit.
    * Il faut cocher le repas et toucher l'aliment à selectionner.
    *
    * le button "CHERCHER ALIMENT" liste tous les aliments commencant par le mot rentré.
    *
    * @return void
     */

    public void ok(View view) {

        ContentResolver contentResolver = getContentResolver();

        Uri.Builder builder;

        Uri uri;

        ContentValues contentValues = new ContentValues();

        switch(view.getId()){
            case R.id.b_ok:
                String mot_aliment = "";

                if(checkEditText()) {
                    mot_aliment = et_aliment.getEditableText().toString();

                    String repas;

                    if (rb_matin.isChecked()) {
                        repas = "matin";
                    } else if (rb_midi.isChecked()) {
                        repas = "midi";
                    }
                    else {
                        repas = "soir";
                    }

                    builder = new Uri.Builder();

                /*
                * Il faudra prendre un aliment de la listView pour le rentrer dans la base de donnée.
                 */

                    builder.scheme("content").authority(authority).appendPath(repas).build();

                    uri = builder.build();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();

                    int grammes = 0;

                    String string_gramme = et_gramme.getText().toString();

                    if (isNumeric(string_gramme)) {
                        grammes  = calcul_calories(Integer.parseInt(string_gramme));

                        contentValues.put("date",dateFormat.format(date));
                        contentValues.put("aliment", "Farine de sarrasin");
                        contentValues.put("calories", grammes);

                        uri = contentResolver.insert(uri,contentValues);

                        builder.scheme("content").authority(authority).appendPath("moi").build();

                        contentValues = new ContentValues();

                        int new_grammes = grammes + getCalorie("Farine de sarrasin"); // A RETIRER

                        contentValues.put("calories", new_grammes);

                        contentResolver.update(uri, contentValues, "calories = ?", new String[]{"calories"});
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "vous n'avez pas rentré une valeur correct", Toast.LENGTH_SHORT);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vous devez rentrer un aliment", Toast.LENGTH_SHORT);
                }


                break;

            case R.id.b_chercher:

                mot_aliment = "";



                if(!et_aliment.getText().toString().equals("")) {
                    mot_aliment = et_aliment.getEditableText().toString();

                    String[] projection = {AlimentContentProvider.STRING_ALIMENT_ID,AlimentContentProvider.STRING_ALIMENT,AlimentContentProvider.STRING_CALORIES};

                    builder = new Uri.Builder();

                    builder.scheme("content").authority(authority).appendPath("aliment").build();

                    uri = builder.build();

                    Cursor cursor = contentResolver.query(uri, projection, "aliment LIKE ?", new String[]{mot_aliment+"%"}, null);

                    if (cursor == null )
                        Toast.makeText(getApplicationContext(), "Cursor NULL \n"+mot_aliment, Toast.LENGTH_SHORT).show();
                    else {

                        listAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, new String[]{AlimentContentProvider.STRING_ALIMENT, AlimentContentProvider.STRING_CALORIES}, new int[]{android.R.id.text1, android.R.id.text2},2);
                        listView.setAdapter(listAdapter);
                        String query = "";
                        while (cursor.moveToNext()) {
                            query += cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_ALIMENT))+"\n";
                        }
                        Log.d("CURSOR: ", cursor.toString());
                        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vous devez rentrer un aliment", Toast.LENGTH_SHORT).show();
                }
                break;

            default:

        }
    }

    public void intent_settings(){
        Intent intent = new Intent();
        intent.setClass(this,ProfileActivity.class);
        this.startActivity(intent);
    }

    public void intent_home(){
        Intent intent = new Intent();
        intent.setClass(this,HomeActivity.class);
        this.startActivity(intent);
    }
}
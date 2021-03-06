package com.example.matthew.basicfit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
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
    private TimePicker timePicker;
    private Button b_chercher, b_ajouter;
    protected RadioButton rb_matin, rb_midi, rb_soir;
    private int hour;
    private int minute;
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
                case R.id.supprimer_aliment:
                    intent_supp();
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
        navigation.setSelectedItemId(R.id.add_aliment);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.list);

        et_aliment = (EditText) findViewById(R.id.et_aliment);
        et_gramme = (EditText) findViewById(R.id.et_gramme);

        b_chercher = (Button) findViewById(R.id.b_chercher);
        b_ajouter = (Button) findViewById(R.id.b_ok);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String repas="";
                String calorie="";
                Cursor cur = (Cursor)listAdapter.getItem(position);
                repas=cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_ALIMENT));
                calorie=cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
                et_aliment.setText(repas);
                if(!et_gramme.getText().toString().equals("")) {
                    AlertDialog.Builder repas_dialog = new AlertDialog.Builder(Ajout_aliment_repas.this);
                    View view1 = getLayoutInflater().inflate(R.layout.dialog, null);
                    Ajout_aliment_repas.this.rb_matin = (RadioButton) view1.findViewById(R.id.radio_matin);
                    Ajout_aliment_repas.this.rb_midi = (RadioButton) view1.findViewById(R.id.radio_midi);
                    Ajout_aliment_repas.this.rb_soir = (RadioButton) view1.findViewById(R.id.radio_soir);
                    Button ok = (Button) view1.findViewById(R.id.b_ajout);
                    Button annuler = (Button) view1.findViewById(R.id.b_annuler);

                    repas_dialog.setView(view1);

                    Ajout_aliment_repas.this.timePicker = (TimePicker) view1.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);

                    Ajout_aliment_repas.this.hour = timePicker.getHour();
                    Ajout_aliment_repas.this.minute = timePicker.getMinute();

                    final AlertDialog r_dialog = repas_dialog.create();
                    r_dialog.show();

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Ajout_aliment_repas.this.rb_matin.isChecked() ||
                                    Ajout_aliment_repas.this.rb_midi.isChecked() ||
                                    Ajout_aliment_repas.this.rb_soir.isChecked()) {
                                Ajout_aliment_repas.this.hour = timePicker.getHour();
                                Ajout_aliment_repas.this.minute = timePicker.getMinute();

                                r_dialog.cancel();
                            }
                        }
                    });

                    annuler.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            r_dialog.cancel();
                        }
                    });
                }
            }
        });
    }

    /*
    * Calcul le nombre de calories pour 100g dans l'activité acitivty_ajout_aliment_repas.xml
    * @param gramme nombre de gramme
    * @return nombre de calories pour 100g
     */

    public int calcul_calories(int gramme) {
        return gramme*20;
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

        String mot_aliment = "";
        switch(view.getId()){
            case R.id.b_ok:
                if(rb_soir!=null || rb_midi !=null || rb_matin!=null){


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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date = new Date();

                        int grammes = 0;

                        String string_gramme = et_gramme.getText().toString();

                        if (isNumeric(string_gramme)) {
                            grammes  = calcul_calories(Integer.parseInt(string_gramme));

                            contentValues.put("date",dateFormat.format(date));
                            contentValues.put("aliment", "Farine de sarrasin");
                            contentValues.put("calories", grammes);

                            uri = contentResolver.insert(uri,contentValues);

                            Uri uri_calories;

                            Uri.Builder builder1 = new Uri.Builder();

                            uri_calories = builder1.scheme("content").authority(authority).appendPath("moi").appendPath("calories").build();

                            contentValues = new ContentValues();

                            int new_grammes = grammes + getCalorie("Farine de sarrasin"); // A RETIRER

                            contentValues.put("calories", new_grammes);

                            contentResolver.update(uri_calories, contentValues, "calories = ?", new String[]{"calories"});
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "vous n'avez pas rentré une valeur correct", Toast.LENGTH_SHORT);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Vous devez rentrer un aliment", Toast.LENGTH_SHORT);
                    }

                    this.et_aliment.setText("");
                    //this.et_gramme.setText("");
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
                        //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
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

    public void intent_supp(){
        Intent intent = new Intent();
        intent.setClass(this,SuppressionActivity.class);
        this.startActivity(intent);
    }
}
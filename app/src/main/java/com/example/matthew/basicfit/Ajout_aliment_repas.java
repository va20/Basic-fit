package com.example.matthew.basicfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Ajout_aliment_repas extends AppCompatActivity {

    private static String authority;
    private EditText ed_add;
    private Button b_chercher, b_ajouter;
    private ListView list_aliment;
    private RadioButton rb_matin, rb_midi, rb_soir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_aliment_repas);

        authority = getResources().getString(R.string.authority);

        ed_add = (EditText) findViewById(R.id.b_aliment);

        rb_matin = (RadioButton) findViewById(R.id.radio_matin);
        rb_midi = (RadioButton) findViewById(R.id.radio_midi);
        rb_soir = (RadioButton) findViewById(R.id.radio_soir);

        b_chercher = (Button) findViewById(R.id.b_chercher);
        b_ajouter = (Button) findViewById(R.id.b_ok);

        this.list_aliment= (ListView) findViewById(R.id.listview_aliment);


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

        ContentValues contentValues = new ContentValues();

        switch(view.getId()){
            case R.id.b_ok:
                String aliment = ed_add.getEditableText().toString();

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

                Uri uri = builder.build();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                contentValues.put("date",dateFormat.format(date));
                contentValues.put("aliment", "Farine de sarrasin");

                uri = contentResolver.insert(uri,contentValues);

                break;

            case R.id.b_chercher:

                String mot_aliment = ed_add.getEditableText().toString();

                String[] projection = {AlimentContentProvider.STRING_ALIMENT,AlimentContentProvider.STRING_CALORIES};

                builder = new Uri.Builder();

                builder.scheme("content").authority(authority).appendPath("aliment").build();

                uri = builder.build();

                Cursor cursor = contentResolver.query(uri, projection, "aliment LIKE ?", new String[]{mot_aliment+"%"}, null);
                SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,new String[]{"aliment","calories"},new int[]{android.R.id.text1},0);

                list_aliment.setAdapter(adapter);

                if (cursor == null )
                    Toast.makeText(getApplicationContext(), "Cursor NULL \n"+mot_aliment, Toast.LENGTH_SHORT).show();
                else {
                    String query = "";
                    while (cursor.moveToNext()) {
                        query += cursor.getString(cursor.getColumnIndex(AlimentContentProvider.STRING_ALIMENT))+"\n";
                    }
                    Log.d("CURSOR: ", cursor.toString());
                    Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                }
                break;

            default:

        }
    }
}

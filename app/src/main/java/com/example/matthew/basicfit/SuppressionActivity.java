package com.example.matthew.basicfit;

import android.content.ContentResolver;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SuppressionActivity extends AppCompatActivity {

    private TextView objectif;
    private EditText bdd_aliment;
    private ListAdapter listAdapter;
    private Button b_chercher, b_supprimer;
    private String authority;
    private ListView listView;


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
                    intent_home();
                    return true;
                case R.id.supprimer_aliment:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppression);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.supprimer_aliment);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        authority = getResources().getString(R.string.authority);

        listView = (ListView) findViewById(R.id.list);

        bdd_aliment = (EditText) findViewById(R.id.et_aliment);

        b_chercher = (Button) findViewById(R.id.b_chercher);
        b_supprimer = (Button) findViewById(R.id.b_supprimer);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                String repas="";
                String calorie="";
                Cursor cur = (Cursor)listAdapter.getItem(position);
                System.out.println(cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_ALIMENT)));
                System.out.println(cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_CALORIES)));
                repas=cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_ALIMENT));
                calorie=cur.getString(cur.getColumnIndex(AlimentContentProvider.STRING_CALORIES));
                bdd_aliment.setText(repas);

            }
        });
    }


    private boolean checkEditText() {
        return !bdd_aliment.getText().toString().equals("");
    }


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


    public void supprimer(final View view){
        final ContentResolver contentResolver = getContentResolver();

        final Uri uri;

        Uri.Builder builder1 = new Uri.Builder();

        uri = builder1.scheme("content").authority(authority).appendPath("aliment").build();

        if(!bdd_aliment.getText().toString().equals("")){

            AlertDialog.Builder repas_dialog = new AlertDialog.Builder(SuppressionActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.dialog_supp,null);

            Button oui = (Button) view1.findViewById(R.id.oui);
            Button non = (Button) view1.findViewById(R.id.non);

            repas_dialog.setView(view1);

            final AlertDialog r_dialog = repas_dialog.create();

            r_dialog.show();

            oui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contentResolver.delete(uri,"aliment = ?",new String[]{bdd_aliment.getText().toString()});
                    ok(view);
                    SuppressionActivity.this.bdd_aliment.setText("");
                    r_dialog.cancel();
                }
            });

            non.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    r_dialog.cancel();
                }
            });


        }


    }

    public void ok(View view) {

        ContentResolver contentResolver = getContentResolver();

        Uri.Builder builder;

        Uri uri;

        ContentValues contentValues = new ContentValues();

        String mot_aliment = "";



        if(!bdd_aliment.getText().toString().equals("")) {
            mot_aliment = bdd_aliment.getEditableText().toString();

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

    public void intent_add(){
        Intent intent = new Intent();
        intent.setClass(this,Ajout_aliment_repas.class);
        this.startActivity(intent);
    }
}

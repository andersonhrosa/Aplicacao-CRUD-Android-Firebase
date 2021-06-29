package com.example.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private ImageButton btfirst;
    private ImageButton btant;
    private ListView mListView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<Jogador> listJogador = new ArrayList<Jogador>();
    private ArrayAdapter<Jogador> arrayAdapterJogador;

    private static final String TAG = "MainActivity2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btfirst = (ImageButton) findViewById(R.id.imageButton1);
        btant = (ImageButton) findViewById(R.id.imageButton2);

        mListView = (ListView) findViewById(R.id.listView);

        inicializarFirebase();
        populateListView();

        btant.setOnClickListener (new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.transition.slide2_in_dir, R.transition.slide2_out_esq);

            }
        });

        btfirst.setOnClickListener (new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.transition.slide2_in_dir, R.transition.slide2_out_esq);
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        databaseReference.child("Jogador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listJogador.clear();
                for (DataSnapshot objSnapshot:snapshot.getChildren()) {
                    Jogador jogador = objSnapshot.getValue(Jogador.class);

                    listJogador.add(jogador);
                }
                arrayAdapterJogador = new ArrayAdapter<Jogador>(MainActivity2.this,
                        android.R.layout.simple_list_item_1, listJogador);
                mListView.setAdapter(arrayAdapterJogador);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity2.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
package com.example.formulario;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private EditText edtvlr1;
    private EditText edtvlr2;
    private EditText edtvlr3;
    private EditText edtvlr4;

    private ImageButton btprox;
    private ImageButton btlast;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Jogador jogador;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtvlr1 = (EditText) findViewById(R.id.edtvlr1);
        edtvlr2 = (EditText) findViewById(R.id.edtvlr2);
        edtvlr3 = (EditText) findViewById(R.id.edtvlr3);
        edtvlr4 = (EditText) findViewById(R.id.edtvlr4);

        btprox = (ImageButton) findViewById(R.id.imageButton3);
        btlast = (ImageButton) findViewById(R.id.imageButton4);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializarFirebase();


        btprox.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
            }
        });

        btlast.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create) {
            String newEntry1 = edtvlr1.getText().toString();
            String newEntry2 = edtvlr2.getText().toString();
            String newEntry3 = edtvlr3.getText().toString();
            String newEntry4 = edtvlr4.getText().toString();

            if (edtvlr1.length() != 0 && edtvlr2.length() != 0 && edtvlr3.length() != 0 && edtvlr4.length() != 0) {
                AddData(newEntry1, newEntry2, newEntry3, newEntry4);
                limparCampos();

            } else {
                Toast.makeText(getApplicationContext(),"Voce deve preencher todos os campos!",Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (id == R.id.read) {
            String cod = edtvlr1.getText().toString();
            if (edtvlr1.length() != 0 ) {
            Jogador j = capturaCodigo(cod);

                if(j != null) {
                    edtvlr2.setText(j.getNome());
                    edtvlr3.setText(j.getEndereco());
                    edtvlr4.setText(j.getFone());
                } else {
                    Toast.makeText(getApplicationContext(),"Consulta Inválida, Tente novamente!",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Voce deve preencher o campo de código para realizar a consulta!",Toast.LENGTH_SHORT).show();
            }

            return true;

        } else if (id == R.id.update) {
            String cod = edtvlr1.getText().toString();
            String name = edtvlr2.getText().toString();
            String address = edtvlr3.getText().toString();
            String phone = edtvlr4.getText().toString();

            if (edtvlr1.length() != 0 && edtvlr2.length() != 0 && edtvlr3.length() != 0 && edtvlr4.length() != 0) {
                AddData(cod, name, address, phone);
                limparCampos();
            } else {
                Toast.makeText(getApplicationContext(),"Voce deve preencher todos os campos!",Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (id == R.id.delete) {
            String cod = edtvlr1.getText().toString();
            databaseReference.child("Jogador").child(cod).removeValue();
            limparCampos();
            return true;

        } else if (id == R.id.readall) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void AddData(String cod, String nome, String ender, String fone) {
        Jogador jogador = new Jogador();
        jogador.setCodigo(cod);
        jogador.setNome(nome);
        jogador.setEndereco(ender);
        jogador.setFone(fone);

        databaseReference.child("Jogador").child(jogador.getCodigo()).setValue(jogador);
    }


    private Jogador capturaCodigo(String cod) {
            databaseReference.child("Jogador").orderByChild("codigo").equalTo(cod).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    jogador = objSnapshot.getValue(Jogador.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return jogador;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void limparCampos() {
        edtvlr1.setText("");
        edtvlr2.setText("");
        edtvlr3.setText("");
        edtvlr4.setText("");
    }
}
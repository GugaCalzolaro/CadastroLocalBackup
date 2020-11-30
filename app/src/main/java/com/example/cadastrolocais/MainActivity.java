package com.example.cadastrolocais;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cadastrolocais.modelo.Local;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText editTextNome, editTextDescricao;
    ListView listV_locais;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Local> listLocais = new ArrayList<Local>();
    private ArrayAdapter<Local> arrayAdapterLocal;

    Local localSelecionado;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextDescricao = (EditText)findViewById(R.id.editTextDescricao);
        listV_locais = (ListView)findViewById(R.id.listV_locais);

        inicializarFirebase();
        eventoDatabase();

        listV_locais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                localSelecionado = (Local)parent.getItemAtPosition(position);
                editTextNome.setText(localSelecionado.getNome());
                editTextDescricao.setText(localSelecionado.getDescricao());
            }
        });

    }

    private void eventoDatabase() {
        databaseReference.child("Local").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLocais.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Local l = objSnapshot.getValue(Local.class);
                    listLocais.add(l);

                }
                arrayAdapterLocal = new ArrayAdapter<Local>(MainActivity.this,
                        android.R.layout.simple_list_item_1,listLocais);
                listV_locais.setAdapter(arrayAdapterLocal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){

            //Função para Mandar para o banco
            Local l = new Local();
            l.setId(UUID.randomUUID().toString());
            // l.setData();
            l.setNome(editTextNome.getText().toString());
            l.setDescricao(editTextDescricao.getText().toString());
            //l.setLatitude();
            //l.setLongitude();
            databaseReference.child("Local").child(l.getId()).setValue(l);
            limparCampos();
        }else if(id ==R.id.menu_editar){
            //Edita Dados do Nome e Descr apenas
            Local l = new Local();
            l.setId(localSelecionado.getId());
            l.setNome(editTextNome.getText().toString().trim());
            l.setDescricao(editTextDescricao.getText().toString().trim());
            databaseReference.child("Local").child(l.getId()).setValue(l);
            limparCampos();
        }else if (id == R.id.menu_deletar){
            //Deleta Dados
            Local l = new Local();
            l.setId(localSelecionado.getId());
            databaseReference.child("Local").child(l.getId()).removeValue();
        }
        return true;
    }
    private void limparCampos(){
        editTextNome.setText("");
        editTextDescricao.setText("");
    }
}
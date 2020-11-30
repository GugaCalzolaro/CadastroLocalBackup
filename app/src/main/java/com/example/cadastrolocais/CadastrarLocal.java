/*package com.example.cadastrolocais;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cadastrolocais.modelo.Local;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarLocal extends AppCompatActivity {
    EditText editTextNome, editTextDescricao;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextDescricao = (EditText)findViewById(R.id.editTextDescricao);

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
        }
        return true;
    }
    private void limparCampos(){
        editTextNome.setText("");
        editTextDescricao.setText("");
    }
} */

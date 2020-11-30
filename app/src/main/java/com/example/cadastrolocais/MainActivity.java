package com.example.cadastrolocais;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

///////////////////////////// GPS INICIO //////////////////////////////////////////////////////

import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



///////////////////////////// GPS FINAL  //////////////////////////////////////////////////////




public class MainActivity extends AppCompatActivity{

    ///////////////////////////// GPS INICIO //////////////////////////////////////////////////////
    private GpsTracker gpsTracker;
    private TextView Latitude,Longitude;

    ///////////////////////////// GPS FINAL  //////////////////////////////////////////////////////

    ///////////////////////////// IMAGEM INICIO //////////////////////////////////////////////////////

    ///////////////////////////// IMAGEM FINAL  //////////////////////////////////////////////////////


    EditText mTitleEt, mDescriptionEt;
    TextView data;
    Button mSaveBtn, mListBtn;

    ProgressDialog pd;

    FirebaseFirestore db;

    String pId, pTitle, pDescription, pLatitude, pLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////// DATA E HORA INICIO ////////
        Date currentTime = Calendar.getInstance().getTime();
        String data_hora = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        //////////// DATA E HORA FINAL ////////



        ///////////////////////////// GPS INICIO //////////////////////////////////////////////////////
        Latitude = (TextView)findViewById(R.id.rLatitudeTv);
        Longitude = (TextView)findViewById(R.id.rLongitudeTv);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        ///////////////////////////// GPS FINAL  //////////////////////////////////////////////////////


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        /*mListBtn = findViewById(R.id.listBtn);*/





        final Bundle bundle = getIntent().getExtras();
        if (bundle != null ){
            // ATUALIZAR DADOS

            // Atualiza o título do cabeçalho
            actionBar.setTitle("Atualizar Local");

            //Atualiza o text do botão de Salvar
            mSaveBtn.setText("Atualizar");

            //get dados
            pId = bundle.getString("pId");
            pTitle = bundle.getString("pTitle");
            pDescription = bundle.getString("pDescription");


            //set dados
            mTitleEt.setText(pTitle);
            mDescriptionEt.setText(pDescription);

        }
        else {
            // ADICIONAR OS DADOS

            // Atualiza o título do cabeçalho
            actionBar.setTitle("Adicionar Local");

            //Atualiza o text do botão de Salvar
            mSaveBtn.setText("Salvar");
        }

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        //Botao para subir os dados
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle!= null){
                    //updating
                    String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    //function call to update data
                    updateData(id, title, description);
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
                else {
                    //adding new
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    gpsTracker = new GpsTracker(MainActivity.this);
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    String sLatitude = String.valueOf(latitude);
                    String sLongitude = String.valueOf(longitude);

                    String horario = data_hora;

                    uploadData(title, description, sLatitude, sLongitude, horario);
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            }
        });

        //Botão para startar a ListActivity
/*        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        });*/
    }


    /////////////////////// GPS INICIO /////////////////////
    public void getLocation(View view){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Latitude.setText(String.valueOf(latitude));
            Longitude.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    /////////////////////// GPS FINAL /////////////////////

    //ATUALIZANDO DADOS NO FIRESTORE
    private void updateData(String id, String title, String description) {
        pd.setTitle("Atualizando locais");
        pd.show();
        db.collection("Documents").document(id)
                .update("title", title, "description", description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated sucessfully
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Seu local foi atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when there is any error
                        pd.dismiss();
                        //get and show error message
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //ADICIONANDO DADOS NO FIRESTORE
    private void uploadData(String title, String description, String sLatitude, String sLongitude, String horario) {
        pd.setTitle("Adicionando dados no Firestore...");
        pd.show();
        //Gera ID aleatório
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);
        doc.put("latitude", sLatitude);
        doc.put("longitude", sLongitude);
        doc.put("horario", horario);


        db.collection("Documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Salvando Locais", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }
}
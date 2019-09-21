package com.example.loginieca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelosdatos.Model;

public class RealTime extends AppCompatActivity implements  View.OnClickListener  {
    Button Bguardar,Bactualizar,Beliminar;
    Spinner spGroup, spLecture;
    RecyclerView recyclerView;
    TextView txtActivity;


    //definir las variables de conexion a la base no-sql

    FirebaseDatabase firebaseDatabase;
    DatabaseReference modelClass;




    String [] grupos={"TI-701","AG-701","GE-701","IN-701","ME-701"};
    String [] materias={"Calculo I","Calculo II","Ecuaciones dif","Colorimetria","Comunicacion asertiva"};

    public ArrayList<Model> list = new ArrayList<>();
    public MyRecyclerViewHolder myRecyclerViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);


        firebaseDatabase = FirebaseDatabase.getInstance();
        modelClass = firebaseDatabase.getReference("Model");





        spGroup = findViewById(R.id.spGrupo);
        spLecture= findViewById(R.id.spMateria);
        txtActivity=findViewById(R.id.txtActividad);
        Bguardar= findViewById(R.id.btnGuardar);
        Beliminar= findViewById(R.id.btnEliminar);
        Bactualizar= findViewById(R.id.btnActualizar);
        recyclerView= findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.getAdapter();

        //llenar spinners

        spGroup.setAdapter(new ArrayAdapter<String >(
                getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,
                grupos));

        spLecture.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,
                materias));


        Bguardar.setOnClickListener(this);
        Beliminar.setOnClickListener(this);
        Bactualizar.setOnClickListener(this);


        getDataFromFirebase();


    }

    @Override
    public void onClick(View v) {
        switch ( (v.getId())){


            case R.id.btnGuardar:

                addNode();

                break;

            case R.id.btnEliminar:

                deleteNode ("-LpB0x435LApBF7us2d0");
                break;

            case R.id.btnActualizar:
                updateNode("-LpB0zD9psvXhUgwCp5B");


                break;

        }
    }


    private void addNode() {
//recolectar los datos del formulario
        //grupo

        String datosGrupo= spGroup.getSelectedItem().toString();
        String datosMateria= spLecture.getSelectedItem().toString();
        String datosActividad= txtActivity.getText().toString().trim();

        if(datosActividad.isEmpty()) {


            txtActivity.setError("Linear campo");
            txtActivity.setFocusable(true);
        }else
        {
            //agregamos el dato a firebase
            //consultamos la base donde se agregaran los elementos

            String idDatabase= modelClass.push().getKey();


            //instancia de nuestro modelo de datos, para poder guardar informacion
            Model myActivity = new Model(idDatabase,datosGrupo,datosMateria,datosActividad);

            //guardamos en la base datos de firebase

            modelClass.child("Lectures").child(idDatabase).setValue(myActivity);

            Toast.makeText(getApplicationContext(),
                    "Agregado correctamente",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void  getDataFromFirebase(){

        modelClass.child("Lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    //procesar la informacion que recolectamos de firebase

                    list.clear();

                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        String id = ds.child("id").getValue().toString();
                        String grupo = ds.child("group").getValue().toString();
                        String actividad = ds.child("activity").getValue().toString();
                        String lecture = ds.child("lecture").getValue().toString();

                        list.add(new Model(id,grupo,lecture,actividad));
                    }

                    //llenar recycler view
                    myRecyclerViewHolder= new MyRecyclerViewHolder(list, new MyRecyclerViewHolder.RecyclerViewOnItemClickListener(){
                        @Override
                        public void onClick(View v, int position) {
                            Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_LONG).show();
                            v.getId();



                        }
                    });
                    recyclerView.setAdapter(myRecyclerViewHolder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void deleteNode(String id){

        modelClass.child("Lectures").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(),
                        "Se ha eliminado el elemento correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "EL elemento no se ha podido eliminar",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateNode (String id){


        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("activity","actualizado");
        dataMap.put("group","actualizado");
        dataMap.put("lecture","actualizado");

        modelClass.child("Lectures").child(id).updateChildren(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getApplicationContext(),
                        "Elemento actualizado correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "No se pudo actualizar correctamente, intente de nuevo",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    }


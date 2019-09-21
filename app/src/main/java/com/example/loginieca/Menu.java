package com.example.loginieca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    public static final String user="names";

    Button multimedia,realTime,storage;
    TextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        txtUser =(TextView) findViewById(R.id.textser);
        String user= getIntent().getStringExtra("names");

        txtUser.setText("¡Bienvenid@ "+ user +"!");

        realTime = findViewById(R.id.btnRealTime);
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Menu.this, RealTime.class);
                startActivity(intent);
            }
        });


        multimedia= (Button)findViewById(R.id.btnMultimedia);
        multimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent anterior = new Intent(Menu.this, Multimedia.class);
                startActivity(anterior);

            }
        });
        storage= (Button)findViewById(R.id.btnStorage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent anterior = new Intent(Menu.this, Storage.class);
                startActivity(anterior);

            }
        });

    }

    }


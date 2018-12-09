package com.octaviano.abcc_http_mysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void menuAltas(View v){
        startActivity(new Intent(this, Altas.class));
    }

    public void consiultas(View v){
        startActivity(new Intent(this, ConsultasActivity.class));
    }

    public void bajas(View v){
        startActivity(new Intent(this, BajasActivity.class));
    }

    public void cambios(View v){
        startActivity(new Intent(this, CambiosActivity.class));
    }
}

package com.octaviano.abcc_http_mysql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import controlador.AnalizadorJSON;

public class Altas extends AppCompatActivity {

    EditText txtNc, txtn,txtpa,txtsa,txte,txts,txtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);

        txtNc = findViewById(R.id.txtAltasNC);
        txtn = findViewById(R.id.txtAltasN);
        txtsa = findViewById(R.id.txtAltasSA);
        txtpa = findViewById(R.id.txtAltasPA);
        txtc = findViewById(R.id.txtAltasC);
        txte = findViewById(R.id.txtAltasE);
        txts = findViewById(R.id.txtAltasS);
    }

    public void agregarAlumno(View v){
        String nc = txtNc.getText().toString();
        String n = txtn.getText().toString();
        String pa = txtpa.getText().toString();
        String sa = txtsa.getText().toString();
        String s = txts.getText().toString();
        String c = txtc.getText().toString();
        String e = txte.getText().toString();

        //revisar si existe conexion con wifi
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni =  cn.getActiveNetworkInfo();

        //condicio para ver si hay coneccion
        if (ni != null && ni.isConnected()){
            //conectar y enviar datos para guardar en MySQL
            new AgregarAlumno().execute(nc,n,pa,sa,s,c,e);
        }
    }

    //Clase interna

    class AgregarAlumno extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... args) {

            Map<String,String> datos = new HashMap<String,String>();
            datos.put("nc",args[0]);
            datos.put("n",args[1]);
            datos.put("pa",args[2]);
            datos.put("sa",args[3]);
            datos.put("s",args[4]);
            datos.put("c",args[5]);
            datos.put("e",args[6]);

            AnalizadorJSON aJESON = new AnalizadorJSON();

            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/altas_alumnos.php";
            String metodo = "POST";

            JSONObject resultado = aJESON.peticionesHTTP(url,metodo,datos);

            int r = 0;
            try {
                r = resultado.getInt("exito");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (r ==1){
                Log.i("Msj resultado", "Registro Agregado");
            }else {
                Log.i("Msj resultado", "Error Registro");
            }

            return null;
        }
    }
}

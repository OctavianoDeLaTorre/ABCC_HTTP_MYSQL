package com.octaviano.abcc_http_mysql;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class ConsultasActivity extends AppCompatActivity {
    ListView lsAlumnos;
    RadioButton rbNc;
    RadioButton rbN;
    RadioButton rbPa;
    EditText txtConsulta;
    ArrayList<String> lista = new ArrayList<>();
    AnalizadorJSON json = new AnalizadorJSON();
    ArrayAdapter<String> adaptador;
    String campo = "num_control";
    String valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        rbNc = findViewById(R.id.rbNc);
        rbN = findViewById(R.id.rbN);
        rbPa = findViewById(R.id.rbPa);
        txtConsulta = findViewById(R.id.txtConsultas);
        lsAlumnos = findViewById(R.id.lsConsulta);

        new MOstrarAlumnos().execute();
        adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lista);
        lsAlumnos.setAdapter(adaptador);

        rbPa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "primer_ap";
                }
            }
        });
        rbN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "nombre";
                }
            }
        });
        rbNc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "num_control";
                }
            }
        });

        txtConsulta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String valor = txtConsulta.getText().toString();
                    if ( valor !="" ){
                        lista = new BuscarAlumno().execute(campo,valor).get();
                        if (lista != null){
                            adaptador = new ArrayAdapter<>(ConsultasActivity.this,android.R.layout.simple_list_item_1,lista);
                            lsAlumnos.setAdapter(adaptador);

                        }else{
                            Toast.makeText(ConsultasActivity.this,"No hay registros",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        new MOstrarAlumnos().execute();
                        adaptador = new ArrayAdapter<>(ConsultasActivity.this,android.R.layout.simple_list_item_1,lista);
                        lsAlumnos.setAdapter(adaptador);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class MOstrarAlumnos extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {


            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/prueba_conexion.php";
            JSONObject jsonObject= json.peticionesHTTP(url);

            try {
                JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                String cadena = "";
                for (int i = 0; i < jesonArray.length(); i++){
                    cadena = jesonArray.getJSONObject(i).getString("nc") + " | " +
                            jesonArray.getJSONObject(i).getString("n") + " | " +
                            jesonArray.getJSONObject(i).getString("pa") + " | " +
                            jesonArray.getJSONObject(i).getString("sa") + " | " +
                            jesonArray.getJSONObject(i).getString("c") + " | " +
                            jesonArray.getJSONObject(i).getString("s") + " | " +
                            jesonArray.getJSONObject(i).getString("e");

                    lista.add(cadena);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class BuscarAlumno extends AsyncTask<String, String, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> lista = null;


            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/consultas_patrones.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);


            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                    String cadena = "";
                    for (int i = 0; i < jesonArray.length(); i++){
                        cadena = jesonArray.getJSONObject(i).getString("nc") + " | " +
                                jesonArray.getJSONObject(i).getString("n") + " | " +
                                jesonArray.getJSONObject(i).getString("pa") + " | " +
                                jesonArray.getJSONObject(i).getString("sa") + " | " +
                                jesonArray.getJSONObject(i).getString("c") + " | " +
                                jesonArray.getJSONObject(i).getString("s") + " | " +
                                jesonArray.getJSONObject(i).getString("e");

                        lista.add(cadena);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }
    }
}

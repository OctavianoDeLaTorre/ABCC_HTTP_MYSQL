package com.octaviano.abcc_http_mysql;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class BajasActivity extends AppCompatActivity {

    ListView lvBajas;
    Button btnBuscar;
    Button btnEliminar;
    EditText txtBNumControl;

    String nc;
    ArrayAdapter<String> adaptador;
    ArrayList<String> lista;
    AnalizadorJSON json = new AnalizadorJSON();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        lvBajas = findViewById(R.id.lvBajas);
        txtBNumControl = findViewById(R.id.txtBNumControl);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setEnabled(false);


        try {
            lista = new BuscarAlumno().execute("1","1").get();
            if (lista != null){
                adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lista);
                lvBajas.setAdapter(adaptador);
            }else{
                Toast.makeText(this,"No hay registros",Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numControl = txtBNumControl.getText().toString();

                try {
                    lista = new BuscarAlumno().execute("num_control",numControl).get();
                    if (lista != null){
                        adaptador = new ArrayAdapter<>(BajasActivity.this,android.R.layout.simple_list_item_1,lista);
                        lvBajas.setAdapter(adaptador);
                        btnEliminar.setEnabled(true);
                    }else{
                        btnEliminar.setEnabled(false);
                        Toast.makeText(BajasActivity.this,"No hay registros",Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean resultado = new EliminarAlumno().execute(nc).get();
                    Toast.makeText(BajasActivity.this,"ELIMINACION CORRECTA...",Toast.LENGTH_SHORT).show();
                    lista = new BuscarAlumno().execute("1","1").get();
                    if (lista != null){
                        adaptador = new ArrayAdapter<>(BajasActivity.this,android.R.layout.simple_list_item_1,lista);
                        lvBajas.setAdapter(adaptador);
                    }else{
                        Toast.makeText(BajasActivity.this,"No hay registros",Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    class BuscarAlumno extends AsyncTask<String, String, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> lista = null;


            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/consultas.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);


            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                    String cadena = "";
                    for (int i = 0; i < jesonArray.length(); i++){
                        nc = jesonArray.getJSONObject(i).getString("nc");
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

    class EliminarAlumno extends AsyncTask<String, String, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/bajas_alumnos.php";
            JSONObject jsonObject = json.eliminarHTTP(url,strings[0]);

            try {
                if (jsonObject.getInt("exito") == 1) {
                    return true;
                }else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}

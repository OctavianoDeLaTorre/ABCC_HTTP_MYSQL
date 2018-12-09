package com.octaviano.abcc_http_mysql;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class CambiosActivity extends AppCompatActivity {

    EditText txtCNc, txtCn,txtCpa,txtCsa,txtCe,txtCs,txtCc;
    Button btnCBuscar, btnCGuardar;
    AnalizadorJSON json = new AnalizadorJSON();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        txtCNc = findViewById(R.id.txtCNC);
        txtCn = findViewById(R.id.txtCN);
        txtCsa = findViewById(R.id.txtCSa);
        txtCpa = findViewById(R.id.txtCPa);
        txtCc = findViewById(R.id.txtCC);
        txtCe = findViewById(R.id.txtCE);
        txtCs = findViewById(R.id.txtCS);
        btnCBuscar = findViewById(R.id.btnCBuscar);
        btnCGuardar = findViewById(R.id.btnCGuardar);

        btnCBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numControl = txtCNc.getText().toString();

                try {
                   ArrayList<String> lista = new BuscarAlumno().execute("num_control",numControl).get();
                    if (lista != null){
                        txtCn.setText(lista.get(1));
                        txtCpa.setText(lista.get(2));
                        txtCsa.setText(lista.get(3));
                        txtCc.setText(lista.get(4));
                        txtCs.setText(lista.get(5));
                        txtCe.setText(lista.get(6));
                    }else{

                        Toast.makeText(CambiosActivity.this,"No hay registros",Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nc = txtCNc.getText().toString();
                String n = txtCn.getText().toString();
                String pa = txtCpa.getText().toString();
                String sa = txtCsa.getText().toString();
                String s = txtCs.getText().toString();
                String c = txtCc.getText().toString();
                String e = txtCe.getText().toString();

                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni =  cn.getActiveNetworkInfo();

                //condicio para ver si hay coneccion
                if (ni != null && ni.isConnected()){
                    //conectar y enviar datos para guardar en MySQL
                    try {
                        boolean resultado = new ActualizarAlumno().execute(nc,n,pa,sa,s,c,e).get();
                        if (resultado){
                            Toast.makeText(CambiosActivity.this,"Registro actualizado...",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CambiosActivity.this,"Registro NO actualizado...",Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    class BuscarAlumno extends AsyncTask<String, String, ArrayList<String>> {

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
                        lista.add(jesonArray.getJSONObject(i).getString("nc"));
                        lista.add(jesonArray.getJSONObject(i).getString("n"));
                        lista.add(jesonArray.getJSONObject(i).getString("pa"));
                        lista.add(jesonArray.getJSONObject(i).getString("sa"));
                        lista.add(jesonArray.getJSONObject(i).getString("c"));
                        lista.add(jesonArray.getJSONObject(i).getString("s"));
                        lista.add(jesonArray.getJSONObject(i).getString("e"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }
    }

    class ActualizarAlumno extends AsyncTask <String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... args) {

            Map<String,String> datos = new HashMap<String,String>();
            datos.put("nc",args[0]);
            datos.put("n",args[1]);
            datos.put("pa",args[2]);
            datos.put("sa",args[3]);
            datos.put("s",args[4]);
            datos.put("c",args[5]);
            datos.put("e",args[6]);

            AnalizadorJSON aJESON = new AnalizadorJSON();

            String url = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/web_service_http_android/cambios_alumnos.php";
            String metodo = "POST";

            JSONObject resultado = aJESON.peticionesHTTP(url,metodo,datos);

            int r = 0;
            try {
                r = resultado.getInt("exito");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (r ==1){
                return true;
            }
            return false;
        }
    }
}

package com.fiubapp.fiubapp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil_secundaria extends Fragment {

    private View view = null;

    final Calendar c = Calendar.getInstance();
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.perfil_educacion, container, false);

        cargarDatosEducacionSecundaria();
        final ImageView imgEditar = (ImageView)this.view.findViewById(R.id.imgEditar);

        final EditText fechaInicio = (EditText)this.view.findViewById(R.id.etFechaInicio);
        final EditText fechaFin = (EditText)this.view.findViewById(R.id.etFechaFin);
        final EditText titulo = (EditText)this.view.findViewById(R.id.etTitulo);
        final EditText escuela = (EditText)this.view.findViewById(R.id.etEscuela);

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titulo.isEnabled()) {


                    if (validaIngresoDatosEducacionSecundaria()) {
                        imgEditar.setImageResource(R.drawable.ic_editar);
                        guardarDatosEducacionSecundaria();
                        fechaInicio.setEnabled(false);
                        fechaFin.setEnabled(false);
                        titulo.setEnabled(false);
                        escuela.setEnabled(false);

                    }
                } else{
                    imgEditar.setImageResource(R.drawable.ic_save);
                    fechaInicio.setEnabled(!fechaInicio.isEnabled());
                    fechaFin.setEnabled(!fechaFin.isEnabled());
                    titulo.setEnabled(!titulo.isEnabled());
                    escuela.setEnabled(!escuela.isEnabled());

                }

            }
        });

        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                fechaInicio.setText(String.format("%02d", dayOfMonth) + "/"
                                        + String.format("%02d", (monthOfYear + 1)) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                fechaFin.setText(String.format("%02d", dayOfMonth) + "/"
                                        + String.format("%02d", (monthOfYear + 1)) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        return view;
    }

    public boolean validaIngresoDatosEducacionSecundaria(){
        final EditText etFechaInicio = (EditText)this.view.findViewById(R.id.etFechaInicio);
        final EditText etFechaFin = (EditText)this.view.findViewById(R.id.etFechaFin);

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

        Date actual = new Date();
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = formatoDelTexto.parse(etFechaInicio.getText().toString());
            fechaFin = formatoDelTexto.parse(etFechaFin.getText().toString());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if(!fechaInicio.before(fechaFin) || !fechaFin.before(actual)){
            Popup.showText(this.getActivity(), "Verifique que la fecha desde sea menor a la fecha hasta y no sean futuras", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void cargarDatosEducacionSecundaria() {

        SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), 0);
        String username = null;
        if (settings.getBoolean("isExchange",false)){
            username = "I"+settings.getString("username",null);
        }else{
            username = settings.getString("username",null);
        }
        final String token = settings.getString("token", null);

        final EditText etFechaInicio = (EditText)this.view.findViewById(R.id.etFechaInicio);
        final EditText etFechaFin = (EditText)this.view.findViewById(R.id.etFechaFin);
        final EditText etTitulo = (EditText)this.view.findViewById(R.id.etTitulo);
        final EditText etEscuela = (EditText)this.view.findViewById(R.id.etEscuela);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = this.getString(R.string.urlAPI) + "/students/" + username + "/highSchool";

        JSONObject jsonParams = new JSONObject();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String titulo = response.getString("degree");
                            String escuela = response.getString("schoolName");
                            String fechaInicio = response.getString("dateFrom");
                            String fechaFin = response.getString("dateTo");

                            etFechaInicio.setText(fechaInicio);
                            etFechaFin.setText(fechaFin);
                            etTitulo.setText(titulo);
                            etEscuela.setText(escuela);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //parseo la respuesta del server para obtener JSON
                        String body = null;
                        try {
                            body = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));

                            JSONObject JSONBody = new JSONObject(body);
                            String codigoError = JSONBody.getString("code");

                            if(!codigoError.equals("4002")){
                                iniciarSesionNuevamente(JSONBody.getString("message"));
                            }
                        } catch (UnsupportedEncodingException e) {
                            iniciarSesionNuevamente(null);
                        } catch (JSONException e) {
                            iniciarSesionNuevamente(null);
                        }
                    }
                }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };

        queue.add(jsObjRequest);
    }

    public void iniciarSesionNuevamente(String mensaje){

        String msg = "Debe iniciar sesión nuevamente";

        if(mensaje != null)
            msg = mensaje;

        Popup.showText(getActivity(), msg, Toast.LENGTH_LONG).show();

        Intent i = new Intent(getActivity().getBaseContext(), Login.class);
        startActivity(i);
    }

    public void guardarDatosEducacionSecundaria(){
        guardarOACtualizarDatosEducacionSecundaria(Request.Method.PUT);
    }

    public void guardarOACtualizarDatosEducacionSecundaria(int guardarActualizar){

        SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), 0);
        String username = null;
        if (settings.getBoolean("isExchange",false)){
            username = "I"+settings.getString("username",null);
        }else{
            username = settings.getString("username",null);
        }
        final String token = settings.getString("token", null);

        final EditText fechaInicio = (EditText)this.view.findViewById(R.id.etFechaInicio);
        final EditText fechaFin = (EditText)this.view.findViewById(R.id.etFechaFin);
        final EditText titulo = (EditText)this.view.findViewById(R.id.etTitulo);
        final EditText escuela = (EditText)this.view.findViewById(R.id.etEscuela);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = this.getString(R.string.urlAPI) + "/students/" + username + "/highSchool";

        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("degree", titulo.getText());
            jsonParams.put("schoolName", escuela.getText());
            jsonParams.put("dateFrom", fechaInicio.getText());
            jsonParams.put("dateTo", fechaFin.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(guardarActualizar, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Popup.showText(getActivity(), "Datos de educación secundaria guardados correctamente", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //parseo la respuesta del server para obtener JSON
                        String body = null;
                        try {
                            body = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));

                            JSONObject JSONBody = new JSONObject(body);
                            String codigoError = JSONBody.getString("code");

                            if(codigoError.equals("4002")) {
                                guardarOACtualizarDatosEducacionSecundaria(Request.Method.POST);
                            }
                            else {
                                iniciarSesionNuevamente(JSONBody.getString("message"));
                            }
                        } catch (JSONException e) {
                            iniciarSesionNuevamente(null);
                        } catch (UnsupportedEncodingException e) {
                            iniciarSesionNuevamente(null);
                        }
                    }
                }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };

        queue.add(jsObjRequest);
    }
}
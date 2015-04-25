package ar.uba.fi.fiubappMobile.partners;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fiubapp.fiubapp.Alumno;
import com.fiubapp.fiubapp.AlumnoAdapter;
import com.fiubapp.fiubapp.R;
import com.fiubapp.fiubapp.SpinnerObject;
import com.fiubapp.fiubapp.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearch extends Fragment {
    private String urlAPI;
    private static final String TAG = AdvancedSearch.class.getSimpleName();
    private AlumnoAdapter studentAdapter;
    private List<Alumno> studentList = new ArrayList<Alumno>();
    private EditText edt_search_name;
    private EditText edt_search_lastname;
    private Spinner spnCareer;
    private EditText edt_search_email;
    private EditText edt_search_filenumber;
    private EditText edt_search_passport;
    private Button btnSearch;
    private ListView  studentsMeetCriteria;
    private SpinnerObject spinnerObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlAPI = getResources().getString(R.string.urlAPI);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View advancedSearchView = inflater.inflate(R.layout.advanced_search, container, false);

        edt_search_name = (EditText) advancedSearchView.findViewById(R.id.edt_advsearch_name);
        edt_search_lastname = (EditText) advancedSearchView.findViewById(R.id.edt_advsearch_lastname);
        edt_search_email = (EditText) advancedSearchView.findViewById(R.id.edt_search_email);
        edt_search_filenumber = (EditText) advancedSearchView.findViewById(R.id.edt_search_filenumber);
        edt_search_passport = (EditText) advancedSearchView.findViewById(R.id.edt_search_passport);

        studentsMeetCriteria = (ListView) advancedSearchView.findViewById(R.id.lst_advanced_search);
        //studentsMeetCriteria.setVisibility(View.INVISIBLE);
        studentAdapter = new AlumnoAdapter(getActivity(), studentList);
        studentsMeetCriteria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        studentsMeetCriteria.setAdapter(studentAdapter);

        spnCareer = (Spinner) advancedSearchView.findViewById(R.id.spn_search_career);

        fillCareerSpinner(spnCareer);

        btnSearch = (Button) advancedSearchView.findViewById(R.id.button_advanced_search);

        btnSearch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //studentsMeetCriteria.setVisibility(View.VISIBLE);
                fillStudentList();
            }
        });

        return advancedSearchView;
    }

    private void fillCareerSpinner(Spinner spnCareer) {
        final ArrayList<SpinnerObject> careers = new ArrayList<SpinnerObject>();
        final ArrayAdapter spnAdapter = new ArrayAdapter<SpinnerObject>(getActivity(), R.layout.simple_spinner_item,careers);
        spnAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnCareer.setAdapter(spnAdapter);

        final String urlAPI = this.getString(R.string.urlAPI);

        JsonArrayRequest careerReq = new JsonArrayRequest(urlAPI+"/careers/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        spinnerObject = new SpinnerObject(0, "Seleccione una carrera");
                        careers.add(spinnerObject);
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                spinnerObject = new SpinnerObject(obj.getInt("code"), obj.getString("name"));
                                careers.add(spinnerObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        spnAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.d(TAG, Integer.toString(error.networkResponse.statusCode));
                }

            }
        });

        //se agrega el request para obtener las carreras
        VolleyController.getInstance().addToRequestQueue(careerReq);

    }

    private void fillStudentList() {
        studentList.clear();

        String name = edt_search_name.getText().toString();
        String lastname = edt_search_lastname.getText().toString();
        String email = edt_search_email.getText().toString();
        String fileNumber = edt_search_filenumber.getText().toString();
        String passportNumber = edt_search_passport.getText().toString();
        SpinnerObject spn_object = ((SpinnerObject)spnCareer.getSelectedItem());
        int careerID = spn_object.getID();
        String careerCode;
        if (careerID == 0)
            careerCode = "";
        else
            careerCode = Integer.toString(careerID);
        // Creating volley request obj
        String requestURL = urlAPI+"/students/?name="+name+"&lastName="+lastname+"&email="+email+"&careerCode="+careerCode+"&fileNumber="+fileNumber+"&passportNumber="+passportNumber;
        JsonArrayRequest studentReq = new JsonArrayRequest(requestURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Alumno student = new Alumno();
                                student.setNombre(obj.getString("name"));
                                student.setApellido(obj.getString("lastName"));
                                student.setIntercambio(obj.getBoolean("isExchangeStudent"));
                                student.setIsMyMate(obj.getBoolean("isMyMate"));
                                if (student.isIntercambio()){
                                    student.setUsername(obj.getString("passportNumber"));
                                }else{
                                    student.setUsername(obj.getString("fileNumber"));
                                }

                                JSONArray JSONCareers = new JSONArray(obj.getString("careers"));
                                ArrayList<String> carreras = new ArrayList<>();

                                for(int j=0; j < JSONCareers.length(); j++){

                                    String carrera = JSONCareers.getString(j);

                                    carreras.add(carrera);
                                }
                                student.setCarreras(carreras);

                                studentList.add(student);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        studentAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings = getActivity().getSharedPreferences(
                        getResources().getString(R.string.prefs_name), 0);
                String token = settings.getString("token",null);
                headers.put("Authorization", token);
                return headers;

            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(studentReq);
    }

}
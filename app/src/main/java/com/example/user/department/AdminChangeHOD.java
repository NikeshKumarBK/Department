package com.example.user.department;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminChangeHOD extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    // Server user register url
    public static String URL_FETCHCT = "http://nikesh.esy.es/php_files/ChangeCT.php";

    // url to get all products list
    private static String URL_UPDATECT = "http://nikesh.esy.es/php_files/UpdateHOD.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public static final String TAG_FACULTYID = "FacultyId";
    public static final String TAG_NAME = "Name";
    public static final String TAG_DEPT = "Department";

    //JSON array name
    public static final String JSON_ARRAY = "faculty";
    private ProgressDialog pDialog;

    Button btnChangeHOD;
    Spinner spiHOD;

    String selectedCounsellor, selectedYear, dept;

    //An ArrayList for Spinner Items
    private ArrayList<String> students;

    private  SQLiteHandler db;

    //JSON Array
    private JSONArray result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_hod);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Initializing the ArrayList
        students = new ArrayList<String>();

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        dept=db.getDept();

        spiHOD =(Spinner)findViewById(R.id.spiHOD);
        spiHOD.setOnItemSelectedListener(this);

        btnChangeHOD =(Button)findViewById(R.id.btnAdminChangeHOD);

        /*spiYear=(Spinner)findViewById(R.id.spiYear);
        ArrayAdapter<String> role=new ArrayAdapter<String>(AdminChangeOD.this,android.R.layout.simple_spinner_item,Year);
        spiYear.setAdapter(role);
        spiYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.e("Check","Before get data");*/

        btnChangeHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCounsellor.matches(""))
                {
                    Toast.makeText(AdminChangeHOD.this,"Select the Faculty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateCT(selectedCounsellor,selectedYear);
                }
            }
        });

        //This method will fetch the data from the URL
        getData();


    }

    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(URL_FETCHCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Check", "Register Response1: " + response.toString());
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString(TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spiHOD.setAdapter(new ArrayAdapter<String>(AdminChangeHOD.this, android.R.layout.simple_spinner_dropdown_item, students));
    }

    //Method to get student name of a particular position
    private String getFacultyId(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(TAG_FACULTYID);
            Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCounsellor = getFacultyId(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void updateCT(final String facultyId, final String year) {

        Log.e("Register", "Inside registerUser");
        // Tag used to cancel the request
        String tag_string_req = "req_registerStaff";

        pDialog.setMessage("Updating OD-Incharge ...");
        showDialog();

        Log.e("Register", "After ShowDialog");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_UPDATECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Check", "Register Response1: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);
                    if (success==1) {
                        // User successfully stored in MySQL
                        Toast.makeText(AdminChangeHOD.this,"HOD updated",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminChangeHOD.this,AdminDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AdminChangeHOD.this,"Oops! Problem occurred",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Check", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                Log.e("Check",facultyId+"  "+dept);
                params.put(TAG_FACULTYID, facultyId);
                params.put(TAG_DEPT, dept);
                Log.e("Check","Params binded");
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminChangeHOD.this,AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}

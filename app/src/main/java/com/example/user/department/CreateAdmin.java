package com.example.user.department;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAdmin extends AppCompatActivity {

    //ui declaration
    EditText edt_facId,edt_facname,edt_facEmail,edt_facMobNo,edt_facPwd;

    //Registration declaration
    private static final String TAG = CreateStaff.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    //temp_storage
    String facId, facName, facEmail, facMobNo, facPwd, selectedDept;

    // Server user register url
    public static String URL_REGISTER = "http://nikesh.esy.es/php_files/RegisterAdmin.php";

    Spinner spiDept;
    String[] Dept={"Department","IT","CSE","ECE", "EEE", "Civil", "EIE", "Aeronautical","Mechanical"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        edt_facId=(EditText)findViewById(R.id.edtFacId);
        edt_facname=(EditText)findViewById(R.id.edtFacName);
        edt_facMobNo=(EditText)findViewById(R.id.edtFacPhno);
        edt_facEmail=(EditText)findViewById(R.id.edtFacEmail);
        edt_facPwd=(EditText)findViewById(R.id.edtFacPass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spiDept=(Spinner)findViewById(R.id.spiDept);
        ArrayAdapter<String> role=new ArrayAdapter<String>(CreateAdmin.this,android.R.layout.simple_spinner_item,Dept);
        spiDept.setAdapter(role);
        spiDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDept = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveStaff);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facId = edt_facId.getText().toString().trim();
                facName = edt_facname.getText().toString().trim();
                facMobNo = edt_facMobNo.getText().toString().trim();
                facEmail = edt_facEmail.getText().toString().trim();
                facPwd = edt_facPwd.getText().toString().trim();

                if (CheckNetwork.isInternetAvailable(CreateAdmin.this)) //returns true if internet available
                {
                    if (!facId.isEmpty() && !facName.isEmpty() && !facMobNo.isEmpty() && !facEmail.isEmpty() && !facPwd.isEmpty() && !selectedDept.matches("Department")) {
                        Log.e("Register", "Not empty");
                        registerAdmin(facId, facName, facMobNo, facEmail, facPwd, selectedDept);
                    } else {
                        Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateAdmin.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });




    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerAdmin(final String facId, final String facName, final String facMobNo,
                               final String facEmail, final String facPwd, final String selectedDept) {

        Log.e("Register", "Inside registerUser");
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        Log.e("Register", "After ShowDialog");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response1: " + response.toString());

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "Admin successfully created. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(CreateAdmin.this,Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(CreateAdmin.this,"Faculty id already exists",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("facId", facId);
                params.put("facName", facName);
                params.put("facMobNo", facMobNo);
                params.put("facEmail", facEmail);
                params.put("facPwd", facPwd);
                params.put("facDept", selectedDept);
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

}

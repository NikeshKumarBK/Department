package com.example.user.department;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudentTry extends AppCompatActivity {

    //ui declaration
    EditText edt_regno,edt_name,edt_year,edt_email,edt_phno,edt_pwd;

    //Registration declaration
    private static final String TAG = RegisterStudentTry.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    //temp_storage
    String regno, name, year, email, phno, pwd;

    // Server user register url
    public static String URL_REGISTER = "http://10.0.2.2/Department/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_regno=(EditText)findViewById(R.id.regId);
        edt_name=(EditText)findViewById(R.id.regName);
        edt_year=(EditText)findViewById(R.id.regYear);
        edt_email=(EditText)findViewById(R.id.regEmail);
        edt_phno=(EditText)findViewById(R.id.regPhno);
        edt_pwd=(EditText)findViewById(R.id.regPass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterStudentTry.this,
                    SuccessActivity.class);
            startActivity(intent);
            finish();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regno = edt_regno.getText().toString().trim();
                name = edt_name.getText().toString().trim();
                year = edt_year.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                phno = edt_phno.getText().toString().trim();
                pwd = edt_pwd.getText().toString().trim();


                if (CheckNetwork.isInternetAvailable(RegisterStudentTry.this)) //returns true if internet available
                {
                    if (!regno.isEmpty() && !name.isEmpty() && !year.isEmpty() && !email.isEmpty() && !phno.isEmpty() && !pwd.isEmpty()) {
                        Log.e("Register", "Not empty");
                        registerUser(regno, name, year, email, phno, pwd);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(RegisterStudentTry.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String regno, final String name,final String year, final String email,
                              final String phno, final String pwd) {

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
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterStudentTry.this,
                                SuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(),
                        //      errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

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
                params.put("id", regno);
                params.put("name", name);
                params.put("email", email);
                params.put("password", pwd);
                params.put("Phone_number", phno);
                params.put("year", year);

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

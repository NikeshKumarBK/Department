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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

    import com.android.volley.Request.Method;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateStaff extends AppCompatActivity {

    CheckBox chkCT,chkCounsellor,chkOD,chkHod;

    //ui declaration
    EditText edt_facId,edt_facname,edt_facEmail,edt_facMobNo,edt_facPwd;

    //Registration declaration
    private static final String TAG = CreateStaff.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    //temp_storage
    String facId, facName, facEmail, facMobNo, selectedYear, facPwd, facCounsellor="", facOD="", facHOD="";

    // Server user register url
    public static String URL_REGISTER = "http://nikesh.esy.es/php_files/RegisterStaff.php";

    Spinner spiYear;
    String[] Year={"Year","1st Year","2nd Year","3rd Year","4th Year"};

    int flagCT=0,flagCounsellor=0,flagOD=0,flagHOD=0;

    String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_staff);

        chkCT=(CheckBox)findViewById(R.id.chkCT);
        chkCounsellor=(CheckBox)findViewById(R.id.chkCounsellor);
        chkOD=(CheckBox)findViewById(R.id.chkOD);
        chkHod=(CheckBox)findViewById(R.id.chkHOD);

        edt_facId=(EditText)findViewById(R.id.edtFacId);
        edt_facname=(EditText)findViewById(R.id.edtFacName);
        edt_facMobNo=(EditText)findViewById(R.id.edtFacPhno);
        edt_facEmail=(EditText)findViewById(R.id.edtFacEmail);
        edt_facPwd=(EditText)findViewById(R.id.edtFacPass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        dept=db.getDept();
        // Check if user is already logged in or not

        spiYear=(Spinner)findViewById(R.id.spiYear);
        ArrayAdapter<String> role=new ArrayAdapter<String>(CreateStaff.this,android.R.layout.simple_spinner_item,Year);
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

        chkCounsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagCounsellor==0)
                {
                    flagCounsellor=1;
                    facCounsellor=dept;
                }
                else
                {
                    flagCounsellor=0;
                    facCounsellor="No";
                }

            }
        });

        chkOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagOD==0)
                {
                    flagOD=1;
                    facOD=dept;
                }
                else
                {
                    flagOD=0;
                    facOD="No";
                }

            }
        });

        chkHod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagHOD==0)
                {
                    flagHOD=1;
                    facHOD=dept;
                }
                else
                {
                    flagHOD=0;
                    facHOD="No";
                }
            }
        });

        chkCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flagCT==0) {
                    flagCT=1;
                    ArrayAdapter<String> role=new ArrayAdapter<String>(CreateStaff.this,android.R.layout.simple_spinner_item,Year);
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
                    spiYear.setVisibility(View.VISIBLE);
                }
                else
                {
                    flagCT=0;
                    spiYear.setVisibility(View.INVISIBLE);
                }
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

                if (CheckNetwork.isInternetAvailable(CreateStaff.this)) //returns true if internet available
                {
                    if (!facId.isEmpty() && !facName.isEmpty() && !facMobNo.isEmpty() && !facEmail.isEmpty() && !facPwd.isEmpty() ) {
                        Log.e("Register", "Not empty");
                        if (facCounsellor.matches(""))
                            facCounsellor="No";
                        if (facHOD.matches(""))
                            facHOD="No";
                        if (facOD.matches(""))
                            facOD="No";
                        if (selectedYear.matches("Year"))
                            selectedYear="No";

                        registerStaff(facId, facName, facMobNo, facEmail, facPwd, selectedYear, facCounsellor, facOD, facHOD);
                    } else {
                        Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateStaff.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerStaff(final String facId, final String facName, final String facMobNo,
                              final String facEmail, final String facPwd, final String facChkCT,
                              final String facChkCounsellor, final String facChkOD, final String facChkHoD) {

        Log.e("Register", "Inside registerUser");
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        Log.e("Register", "After ShowDialog");

        StringRequest strReq = new StringRequest(Method.POST,
                URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response1: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        //String uid = jObj.getString("RegNo");

                        JSONObject user = jObj.getJSONObject("user");
                        String uid=user.getString("FacultyId");
                        String name = user.getString("Name");
                        String email = user.getString("Email");
                        //String desig = user.getString("desig");
                        String MobNo = user.getString("MobNo");

                        // Inserting row in users table
                        db.addUser(name, email, uid, MobNo);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(CreateStaff.this,Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(CreateStaff.this,"Faculty id already exists",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(),
                        //      errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("facCT", facChkCT);
                params.put("facCounsellor", facChkCounsellor);
                params.put("facOD", facChkOD);
                params.put("facHOD", facChkHoD);
                params.put("facDept",dept);
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
        Intent intent=new Intent(CreateStaff.this,AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}

package com.example.user.department;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.design.widget.Snackbar;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.CheckBox;
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

    public class Login extends AppCompatActivity {

        EditText edtUser,edtPwd;
        Button btnLogin,btnAdmin;

        private static final String TAG = Login.class.getSimpleName();

        String reg,pwd;
        private ProgressDialog pDialog;
        private SessionManager session;
        private SQLiteHandler db;

        // Server user login url
        public static String URL_LOGIN = "http://nikesh.esy.es/php_files/login.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            edtUser=(EditText)findViewById(R.id.edtUser);
            edtPwd=(EditText)findViewById(R.id.edtPass);
            btnAdmin=(Button) findViewById(R.id.btnAdmin);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Session manager
            session = new SessionManager(getApplicationContext());


            // Check if user is already logged in or not
            if (session.isLoggedIn()) {

                SQLiteHandler repo=new SQLiteHandler(this);
                String role=repo.getRole();

                if (role.matches("Student"))
                {
                    // Launch main activity
                    Intent intent = new Intent(Login.this,StudentNav.class);
                    startActivity(intent);
                    finish();
                }
                else if (role.matches("Faculty"))
                {
                    // Launch main activity
                    Intent intent = new Intent(Login.this,StaffNav.class);
                    startActivity(intent);
                    finish();
                }
                if (role.matches("Admin"))
                {
                    // Launch main activity
                    Intent intent = new Intent(Login.this,AdminDashboard.class);
                    startActivity(intent);
                    finish();
                }


            }

            btnAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Login.this,CreateAdmin.class);
                    startActivity(intent);
                    finish();
                }
            });



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Login.this,RegisterStudent.class);
                    startActivity(intent);
                    finish();
                }
            });

            btnLogin=(Button)findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reg = edtUser.getText().toString().trim();
                    pwd = edtPwd.getText().toString().trim();

                    if(CheckNetwork.isInternetAvailable(Login.this)) //returns true if internet available
                    {
                        // Check for empty data in the form
                        if (!reg.isEmpty() && !pwd.isEmpty()) {
                            // login user
                            checkLogin(reg, pwd);
                        } else {
                            // Prompt user to enter credentials
                            Toast.makeText(getApplicationContext(),"Please enter the credentials!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        /**
         * function to verify login details in mysql db
         * */
        private void checkLogin(final String regid, final String password) {
            // Tag used to cancel the request
            String tag_string_req = "req_login";

            pDialog.setMessage("Logging in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            JSONObject user = jObj.getJSONObject("user");
                            String uid=user.getString("uniqueId");
                            String name = user.getString("Name");
                            String dept = user.getString("Department");
                            //String desig = user.getString("desig");
                            String Role = user
                                    .getString("Role");

                            // Inserting row in users table
                            db.addUser(name, dept, uid, Role);

                            if (Role.matches("Student"))
                            {
                                // Launch main activity
                                Intent intent = new Intent(Login.this,StudentNav.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (Role.matches("Faculty"))
                            {
                                // Launch main activity
                                Intent intent = new Intent(Login.this,StaffNav.class);
                                startActivity(intent);
                                finish();
                            }
                            if (Role.matches("Admin"))
                            {
                                // Launch main activity
                                Intent intent = new Intent(Login.this,AdminDashboard.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),"No Internet connection", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", regid);
                    params.put("password", password);

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

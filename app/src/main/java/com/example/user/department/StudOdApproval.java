package com.example.user.department;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class StudOdApproval extends Fragment implements View.OnClickListener {

    private DatePickerDialog StudODApprovalDatePicker;
    EditText edtEventFrom, edtEventTo,edtEventName,edtPlaceName;
    private SimpleDateFormat dateFormatter;
    FloatingActionButton odApply;
    String regno,placename,eventname,eventfrom,eventto,dept;
    private ProgressDialog pDialog;
    Date eventFromDate,eventToDate;

    private static final String TAG = RegisterStudent.class.getSimpleName();
    // Server user register url
    public static String URL_OD_APPLY = "http://nikesh.esy.es/php_files/StudentOD.php";

    public StudOdApproval() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_stud_od_approval, container, false);
        edtEventFrom=(EditText)view.findViewById(R.id.edtEventFrom);
        edtEventTo=(EditText)view.findViewById(R.id.edtEventTo);
        edtEventName=(EditText)view.findViewById(R.id.edtEventName);
        edtPlaceName=(EditText)view.findViewById(R.id.edtPlaceName);

        odApply=(FloatingActionButton)view.findViewById(R.id.btnOdApply);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        //edtEventFrom.setOnClickListener(this);

        edtEventTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatetimeFieldEventTo();
            }
        });

        edtEventFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });

        odApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    eventfrom=edtEventFrom.getText().toString();
                    eventto=edtEventTo.getText().toString();
                    eventFromDate=dateFormatter.parse(eventfrom);
                    eventToDate=dateFormatter.parse(eventto);

                    long diff = eventToDate.getTime() - eventFromDate.getTime();
                    long days = (((diff/1000) / 60) / 60) / 24;
                    Log.e("Check", String.valueOf(days));

                    placename=edtPlaceName.getText().toString();
                    eventname=edtEventName.getText().toString();

                    SQLiteHandler repo= new SQLiteHandler(getActivity());
                    regno= repo.getStudentRegNo();
                    dept=repo.getDept();

                    if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                    {
                        if (!placename.isEmpty() && !eventname.isEmpty() && !eventfrom.isEmpty() && !eventto.isEmpty()) {
                            Log.e("Register", "Not empty");
                            applyOD(regno, eventfrom, eventto, days, eventname, placename, dept);
                        } else {
                            Toast.makeText(getActivity(),"Please enter your details!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


        }
        });

        return view;
    }

    public void setDateTimeField() {

        edtEventFrom.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();

        StudODApprovalDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtEventFrom.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setDatetimeFieldEventTo()
    {
        edtEventTo.setOnClickListener(this);
        Calendar calendar=Calendar.getInstance();
        StudODApprovalDatePicker =new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                edtEventTo.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v==edtEventFrom)
        {
            StudODApprovalDatePicker.show();
        }
        if (v==edtEventTo)
        {
            StudODApprovalDatePicker.show();
        }
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void applyOD(final String regno, final String eventFromDate, final String eventToDate,
                              final long days, final String eventname, final String placename, final String dept) {

        Log.e("Register", "Inside registerUser");
        // Tag used to cancel the request
        String tag_string_req = "req_registerStaff";

        pDialog.setMessage("Registering ...");
        showDialog();

        Log.e("Register", "After ShowDialog");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_OD_APPLY, new Response.Listener<String>() {

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

                        //JSONObject user = jObj.getJSONObject("user");
                        //String uid=user.getString("RegNo");
                        //String name = user.getString("Name");
                        //String email = user.getString("Email");
                        //String desig = user.getString("desig");
                        //String CurrSem = user.getString("CurrSem");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, CurrSem);

                        Toast.makeText(getActivity(), "OD Applied successfully!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(getActivity(),Login.class);
                        startActivity(intent);

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
                Toast.makeText(getActivity(),"No Internet Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //applyOD(regno, eventFromDate, eventToDate, days, eventname, placename, dept);
                params.put("regno", regno);
                params.put("datefrom", eventFromDate);
                params.put("dateto", eventToDate);
                params.put("days", String.valueOf(days));
                params.put("eventName", eventname);
                params.put("placeName", placename);
                params.put("department", dept);
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

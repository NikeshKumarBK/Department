package com.example.user.department;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class StudAttendance extends Fragment {

    TextView txtName,txtRegNo,txtDaysAttended,txtTotalDays,txtPercent;

    public StudAttendance() {
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
        View view=inflater.inflate(R.layout.fragment_stud_attendance, container, false);
        txtName=(TextView)view.findViewById(R.id.txtName);
        txtRegNo=(TextView)view.findViewById(R.id.txtRegNo);
        txtDaysAttended=(TextView)view.findViewById(R.id.txtDaysAttended);
        txtTotalDays=(TextView)view.findViewById(R.id.txtTotalDays);
        txtPercent=(TextView)view.findViewById(R.id.txtPercent);

        txtName.setText("Nikesh Kumar B K");
        txtRegNo.setText("311013205035");
        txtDaysAttended.setText("15");
        txtTotalDays.setText("30");
        txtPercent.setText("50");
        return view;
    }


}

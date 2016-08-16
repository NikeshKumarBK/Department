package com.example.user.department;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity {

    Button btnCreateStaff,btnChangeCT, btnChangeCounsellor, btnChangeODIncharge, btnChangeHOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        SQLiteHandler repo=new SQLiteHandler(AdminDashboard.this);
        String role=repo.getDept();

        btnCreateStaff=(Button)findViewById(R.id.btnCreateStaff);
        btnChangeCT=(Button)findViewById(R.id.btnChangeCT);
        btnChangeCounsellor=(Button)findViewById(R.id.btnChangeCounsellor);
        btnChangeODIncharge=(Button)findViewById(R.id.btnChangeODIncharge);
        btnChangeHOD=(Button)findViewById(R.id.btnChangeHOD);

        btnCreateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminDashboard.this,CreateStaff.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangeCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminDashboard.this,AdminChangeCT.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangeCounsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminDashboard.this,AdminChangeCounsellor.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangeODIncharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminDashboard.this,AdminChangeOD.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangeHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminDashboard.this,AdminChangeCT.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminDashboard.this,Login.class);
        startActivity(intent);
        finish();
    }
}

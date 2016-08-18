package com.example.user.department;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StudentNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtStudRegNo,txtStudName;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SQLiteHandler repo=new SQLiteHandler(StudentNav.this);
        String StudRegNo=repo.getStudentRegNo();
        String StudName=repo.getStudentName();
        View header=navigationView.getHeaderView(0);

        txtStudRegNo=(TextView)header.findViewById(R.id.StudentRegNo);
        txtStudName=(TextView)header.findViewById(R.id.StudentName);

        txtStudRegNo.setText(StudRegNo);
        txtStudName.setText(StudName);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_assignment) {
            //AssignmentFragment assignmentFragment=new AssignmentFragment();
           // getSupportFragmentManager().beginTransaction()
             //       .add(R.id.mainFrame, assignmentFragment).commit();

            StudentAssignment studentAssignment=new StudentAssignment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentAssignment).commit();
        }

        else if (id == R.id.nav_attendance) {
            StudAttendance studAttendance=new StudAttendance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studAttendance).commit();
        }

        else if (id == R.id.nav_exam_schedule) {
            StudentExamSchedule studentExamSchedule=new StudentExamSchedule();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentExamSchedule).commit();

        } else if (id == R.id.nav_leave_apply) {
            StudentLeaveApply studentLeaveApply=new StudentLeaveApply();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentLeaveApply).commit();

        } else if (id == R.id.nav_od_leave) {
            StudOdApproval studOdApproval=new StudOdApproval();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studOdApproval).commit();

        } else if (id == R.id.nav_sem_plan) {
            StudentSemPlan studentSemPlan=new StudentSemPlan();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentSemPlan).commit();
        }else if (id == R.id.nav_time_table) {
            StudentTimeTable studentTimeTable =new StudentTimeTable();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentTimeTable).commit();
        } else if (id == R.id.nav_unit_test) {
            StudentExamResults studentExamResults =new StudentExamResults();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentExamResults).commit();
        } else if (id == R.id.nav_university) {
            StudentUnivResult studentUnivResult=new StudentUnivResult();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentUnivResult).commit();
        }
        else if (id == R.id.nav_sign_out) {
            SessionManager session = new SessionManager(StudentNav.this);
            session.setLogin(false);
            SQLiteHandler repo=new SQLiteHandler(getApplicationContext());
            repo.deleteUsers();
            Intent intent=new Intent(StudentNav.this,Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        public void btnClick(View view)
        {

            int id=view.getId();
            if(id==R.id.edtEventFrom)
            {
                StudOdApproval studOdApproval=new StudOdApproval();
                studOdApproval.setDateTimeField();
            }
        }
}

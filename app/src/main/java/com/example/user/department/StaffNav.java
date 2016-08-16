package com.example.user.department;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class StaffNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.staff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pick_a_file) {
            new MaterialFilePicker().withActivity(StaffNav.this).withRequestCode(10).start();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&&(grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            new MaterialFilePicker().withActivity(StaffNav.this).withRequestCode(10).start();
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    ProgressDialog pd;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            pd = new ProgressDialog(StaffNav.this);
            pd.setTitle("Uploading");
            pd.setMessage("please wait!!!");
            pd.show();

            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    File fo=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String file_path= fo.getAbsolutePath();
                    String content_type =  getMimeType(fo.getPath());
                    Log.e("Check",fo.getAbsolutePath());

                    OkHttpClient client = new OkHttpClient();

                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),fo);

                    RequestBody request_body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                            .build();



                    Request request = new Request.Builder().url("http://nikesh.esy.es/PDF_Files/file_upoad.php").post(request_body)
                            .build();


                    try {
                        Log.e("Check","Check request");
                        Response response = client.newCall(request).execute();
                        Log.e("Check","After request");

                        if(!response.isSuccessful()){
                            throw new IOException("Error:"+response);
                        }
                        pd.dismiss();
                    }

                    catch(IOException e){
                        e.printStackTrace();

                    }
                }
            });

            Log.e("Check","Before thread start");
            t.start();
            Log.e("Check","After thread start");

        }
    }

    private String getMimeType(String path) {
        String extension =  MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_staff_mark_entry) {
            // Handle the camera action
            StaffMarkEntry staffMarkEntry=new StaffMarkEntry();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, staffMarkEntry).commit();
        } else if (id == R.id.nav_staff_od_approvals) {
            StaffODApprovals staffOdApproval=new StaffODApprovals();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, staffOdApproval).commit();
        } else if (id == R.id.nav_staff_stu_attend) {
            StaffAttendance staffAttendance=new StaffAttendance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, staffAttendance).commit();
        } else if (id == R.id.nav_staff_od) {
            StudOdApproval studOdApproval=new StudOdApproval();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studOdApproval).commit();
        } else if (id == R.id.nav_staff_leave) {
            StudentLeaveApply studentLeaveApply=new StudentLeaveApply();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, studentLeaveApply).commit();
        } else if (id == R.id.nav_staff_time_table) {
            StaffTimeTable staffTimeTable=new StaffTimeTable();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, staffTimeTable).commit();
        }else if (id == R.id.nav_staff_new_assign) {
            StaffNewAssign staffNewAssign=new StaffNewAssign();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, staffNewAssign).commit();
        }
        else if (id == R.id.nav_sign_out) {
            SessionManager session = new SessionManager(StaffNav.this);
            session.setLogin(false);
            SQLiteHandler repo=new SQLiteHandler(getApplicationContext());
            repo.deleteUsers();
            Intent intent=new Intent(StaffNav.this,Login.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

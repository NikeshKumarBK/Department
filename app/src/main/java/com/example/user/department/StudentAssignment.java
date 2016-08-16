package com.example.user.department;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class StudentAssignment extends Fragment {

    ListView listView;
    String[] assign={"Assignment-1","Assignment-1","Assignment-1","Assignment-1"};
    String[] subCode={"CS6001","IT6001","CD6002","IT6002"};
    String[] subName={"Data Analytics","Cloud Computing","R Programming","Python"};
    String[] date={"18th April","17th April","16th April","15th April"};
    ArrayAdapter<String> adapter;
    List<RowItem> rowItems;

    public StudentAssignment() {
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
        View view=inflater.inflate(R.layout.fragment_student_assignment, container, false);
        listView=(ListView)view.findViewById(R.id.list);

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < assign.length; i++) {
            RowItem item = new RowItem(assign[i], subCode[i],subName[i], date[i]);
            rowItems.add(item);
        }

        CustomListAssignment adapter = new CustomListAssignment(getActivity(),
                R.layout.single_row_assign, rowItems);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), StudentAssignmentDesc.class);
                startActivity(intent);
            }
        });
        return view;
    }


}

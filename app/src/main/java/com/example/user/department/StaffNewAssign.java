package com.example.user.department;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffNewAssign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StaffNewAssign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffNewAssign extends Fragment {
    Spinner spinner;
    String[] sub={"Select a subject","Data Analytics","R programming","Cloud Computing"};

    public StaffNewAssign() {
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
        View view=inflater.inflate(R.layout.fragment_staff_new_assign, container, false);
        spinner=(Spinner)view.findViewById(R.id.spiSub);
        ArrayAdapter<String> my=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,sub);
        spinner.setAdapter(my);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


}

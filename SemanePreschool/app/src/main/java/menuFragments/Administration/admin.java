package menuFragments.Administration;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.main;
import menuFragments.Administration.message.sendAll;
import menuFragments.GlobalMessages;

/**
 * A simple {@link Fragment} subclass.
 */
public class admin extends android.app.Fragment implements View.OnClickListener{
    View myview;
    CardView Message;
    CardView Apply;
    CardView Reports;
    CardView Trips;
    CardView Meetings;
    CardView Badger;
    TextView badgerText;



    public admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_admin, container, false);
        main.TabToExit = true;
        Badger = myview.findViewById(R.id.Badger);
        badgerText = myview.findViewById(R.id.badgerCount);
        LaunchStartUps.ApplicationsChecker.MonitorNotifications(getActivity(),Badger,badgerText);
        Message = myview.findViewById(R.id.messages);
        Apply = myview.findViewById(R.id.apply);
        Reports = myview.findViewById(R.id.reports);
        Trips = myview.findViewById(R.id.trips);
        Meetings = myview.findViewById(R.id.meetings);
        Badger.setVisibility(View.GONE);
        Message.setOnClickListener(this);
        Apply.setOnClickListener(this);
        Reports.setOnClickListener(this);
        Trips.setOnClickListener(this);
        Meetings.setOnClickListener(this);
        if(main.StartAppByNotification){
            methods.globalMethods.loadFragments(R.id.main, new GlobalMessages(), getActivity());
            main.StartAppByNotification = false;
        }
        return myview;

    }

    @Override
    public void onAttach(Context context) {
        main.TabToExit = true;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        main.TabToExit = false;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        main.TabToExit = false;
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.messages) {
            methods.globalMethods.loadFragments(R.id.main, new messages(), getActivity());
        }else if(id == R.id.apply){
            methods.globalMethods.loadFragments(R.id.main, new applications(), getActivity());
        }else if(id == R.id.reports){
            methods.globalMethods.loadFragments(R.id.main, new academicReports(), getActivity());
        }else if(id == R.id.meetings){
            methods.globalMethods.loadFragments(R.id.main, new meeting(), getActivity());
        }else {
            methods.globalMethods.loadFragments(R.id.main, new trips(), getActivity());
        }

    }
}

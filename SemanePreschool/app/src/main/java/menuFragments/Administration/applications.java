package menuFragments.Administration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.Application.accepted;
import menuFragments.Administration.Application.declined;
import menuFragments.Administration.Application.pending;
import menuFragments.Administration.message.history;
import menuFragments.Administration.message.sendAll;
import menuFragments.Administration.message.sendGrades;
import menuFragments.Administration.message.sendUser;
import menuFragments.parents.kidsApplication.allApplications;

/**
 * A simple {@link Fragment} subclass.
 */
public class applications extends android.app.Fragment implements View.OnClickListener{
    View myview;
    CardView All;
    CardView Pending;
    CardView Declined;
    CardView Accepted;


    public applications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_applications, container, false);
        All = myview.findViewById(R.id.MyApplications);
        Pending = myview.findViewById(R.id.pending);
        Declined = myview.findViewById(R.id.declined);
        Accepted = myview.findViewById(R.id.accepted);
        All.setOnClickListener(this);
        Pending.setOnClickListener(this);
        Declined.setOnClickListener(this);
        Accepted.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.pending){
            methods.globalMethods.loadFragments(R.id.main, new pending(), getActivity());
        }else if (id == R.id.declined){
            methods.globalMethods.loadFragments(R.id.main, new declined(), getActivity());
        }else if (id == R.id.accepted){
            methods.globalMethods.loadFragments(R.id.main, new accepted(), getActivity());
        }else{
            methods.globalMethods.loadFragments(R.id.main, new menuFragments.Administration.Application.allApplications(), getActivity());
        }

    }

}

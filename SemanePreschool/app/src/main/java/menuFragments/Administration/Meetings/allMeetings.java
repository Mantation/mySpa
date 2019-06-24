package menuFragments.Administration.Meetings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import connectionHandler.external.allMeetings_;
import io.eyec.bombo.semanepreschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class allMeetings extends android.app.Fragment {
    View myview;
    RecyclerView recyclerView;
    TextView None;


    public allMeetings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview =  inflater.inflate(R.layout.fragment_all_meetings, container, false);
        recyclerView = myview.findViewById(R.id.reports_meetings);
        None = myview.findViewById(R.id.none);
        None.setVisibility(View.GONE);
        allMeetings_.getAllMeetingsDocuments(getActivity(),recyclerView,None);
        return myview;
    }

}

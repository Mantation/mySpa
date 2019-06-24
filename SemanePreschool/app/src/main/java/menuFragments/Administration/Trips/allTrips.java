package menuFragments.Administration.Trips;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.eyec.bombo.semanepreschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class allTrips extends android.app.Fragment {
    View myview;
    RecyclerView recyclerView;
    TextView None;


    public allTrips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_all_trips, container, false);
        recyclerView = myview.findViewById(R.id.reports_trips);
        None = myview.findViewById(R.id.none);
        None.setVisibility(View.GONE);
        connectionHandler.external.allSentTrips_.getAllTripsDocuments(getActivity(),recyclerView,None);
        return myview;
    }

}

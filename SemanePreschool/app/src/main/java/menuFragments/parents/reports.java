package menuFragments.parents;


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
public class reports extends android.app.Fragment {
View myview;
TextView textView;
RecyclerView recyclerView;

    public reports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_reports, container, false);
        textView = myview.findViewById(R.id.none);
        recyclerView = myview.findViewById(R.id.recycler_reports);
        textView.setVisibility(View.GONE);
        connectionHandler.external.reports_.getAllDocuments(getActivity(),recyclerView,textView);
        return myview;
    }

}

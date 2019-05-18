package menuFragments.parents.kidsApplication;


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
public class allApplications extends android.app.Fragment {
View myview;
RecyclerView recyclerView;
TextView textView;

    public allApplications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_all_applications, container, false);
        recyclerView = myview.findViewById(R.id.recycler_applications);
        textView = myview.findViewById(R.id.empty);
        textView.setVisibility(View.GONE);
        connectionHandler.external.myApplications_.getAllDocuments(getActivity(),recyclerView,textView);
        return myview;
    }

}

package menuFragments.Administration.Application;


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
public class declined extends android.app.Fragment {
View myview;
RecyclerView recyclerView;
TextView None;

    public declined() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_declined, container, false);
        recyclerView = (RecyclerView) myview.findViewById(R.id.applications_declined);
        None = myview.findViewById(R.id.none);
        None.setVisibility(View.GONE);
        connectionHandler.external.applicationMessages_.getAllDocuments(getActivity(),recyclerView,"rejected",None);
        return myview;
    }

}

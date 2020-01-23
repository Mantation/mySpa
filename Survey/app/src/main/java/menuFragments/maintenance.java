package menuFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.eyec.bombo.survey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class maintenance extends android.app.Fragment implements View.OnClickListener {
    View myview;
    CardView Start;
    TextView None;
    RecyclerView recyclerView;


    public maintenance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_maintenance, container, false);
        Start = myview.findViewById(R.id.start);
        None = myview.findViewById(R.id.none);
        recyclerView = myview.findViewById(R.id.recyclerview_tasks);
        None.setVisibility(View.GONE);
        connectionHandler.external.openTasks.getAllDocuments(getActivity(),recyclerView,None);
        Start.setOnClickListener(this);
        //recyclerView.setNestedScrollingEnabled(false);
        return myview;
    }

    @Override
    public void onClick(View v) {
        methods.globalMethods.loadFragmentsWithTag(R.id.main, new start(),getActivity());
    }
}

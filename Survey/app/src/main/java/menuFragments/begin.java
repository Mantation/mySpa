package menuFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.eyec.bombo.survey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class begin extends android.app.Fragment implements View.OnClickListener {
    CardView Submit;
    View myview;

    public begin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_begin, container, false);
        Submit = myview.findViewById(R.id.Submit);
        Submit.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        methods.globalMethods.loadFragmentsWithTag(R.id.main, new survey(), getActivity());
    }
}

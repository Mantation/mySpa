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
    CardView Maintain;
    CardView Piping;
    View myview;

    public begin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_begin, container, false);
        Submit = myview.findViewById(R.id.Submit);
        Maintain = myview.findViewById(R.id.Maintain);
        Piping = myview.findViewById(R.id.Piping);
        Submit.setOnClickListener(this);
        Maintain.setOnClickListener(this);
        Piping.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.Submit) {
            methods.globalMethods.loadFragmentsWithTag(R.id.main, new survey(), getActivity());
        } else if(id == R.id.Maintain){
            methods.globalMethods.loadFragmentsWithTag(R.id.main, new maintenance(), getActivity());
        } else{
            methods.globalMethods.loadFragmentsWithTag(R.id.main, new piping(), getActivity());
        }
    }
}

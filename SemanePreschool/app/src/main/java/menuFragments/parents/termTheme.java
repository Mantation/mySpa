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
public class termTheme extends android.app.Fragment {
View myview;
RecyclerView recyclerview;
static TextView themeTerm;
public static void setTerm(final String term){
    themeTerm.setText(term);
}
    public termTheme() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_term_theme, container, false);
        recyclerview = myview.findViewById(R.id.recycler_theme);
        themeTerm = myview.findViewById(R.id.themeName);
        connectionHandler.external.themes_.getAllDocuments(getActivity(),recyclerview);
        return myview;
    }

}

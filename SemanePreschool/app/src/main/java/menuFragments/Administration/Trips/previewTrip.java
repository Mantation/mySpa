package menuFragments.Administration.Trips;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.eyec.bombo.semanepreschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class previewTrip extends android.app.Fragment {
    View myview;
    TextView Heading;
    TextView Body;
    public static String heading;
    public static String body;

    public static String getHeading() {
        return heading;
    }

    public static void setHeading(String heading) {
        previewTrip.heading = heading;
    }

    public static String getBody() {
        return body;
    }

    public static void setBody(String body) {
        previewTrip.body = body;
    }

    public previewTrip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_preview_trip, container, false);
        Heading = myview.findViewById(R.id.heading);
        Body = myview.findViewById(R.id.body);
        Heading.setText(getHeading());
        Body.setText(getBody());
        return myview;
    }

}

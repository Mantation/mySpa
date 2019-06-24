package menuFragments.Administration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.message.All;
import menuFragments.Administration.message.Grades;
import menuFragments.Administration.message.User;
import menuFragments.Administration.message.history;
import menuFragments.Administration.message.sendAll;
import menuFragments.Administration.message.sendGrades;
import menuFragments.Administration.message.sendUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class messages extends android.app.Fragment implements View.OnClickListener{
    View myview;
    CardView Alls;
    CardView Grade;
    CardView Users;
    CardView MessageHistory;

    public messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_messages, container, false);
        Alls = myview.findViewById(R.id.messages);
        Grade = myview.findViewById(R.id.grades);
        Users = myview.findViewById(R.id.user);
        MessageHistory = myview.findViewById(R.id.MyMessages);
        Alls.setOnClickListener(this);
        Grade.setOnClickListener(this);
        Users.setOnClickListener(this);
        MessageHistory.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.messages){
            methods.globalMethods.loadFragments(R.id.main, new All(), getActivity());
        }else if (id == R.id.grades){
            methods.globalMethods.loadFragments(R.id.main, new Grades(), getActivity());
        }else if (id == R.id.user){
            methods.globalMethods.loadFragments(R.id.main, new User(), getActivity());
        }else{
            methods.globalMethods.loadFragments(R.id.main, new history(), getActivity());
        }

    }
}

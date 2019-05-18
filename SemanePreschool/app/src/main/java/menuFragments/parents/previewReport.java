package menuFragments.parents;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.eyec.bombo.semanepreschool.R;
import methods.globalMethods;

/**
 * A simple {@link Fragment} subclass.
 */
public class previewReport extends android.app.Fragment {
View myview;
TextView Term;
TextView Idno;
TextView Body;
TextView Status;
ImageView StatusPreview;

public static String term;
public static String identity;
public static String[] subjects;
public static String status;
public static String overall;

    public static String getTerm() {
        return term;
    }

    public static void setTerm(String term) {
        previewReport.term = term;
    }

    public static String getIdentity() {
        return identity;
    }

    public static void setIdentity(String identity) {
        previewReport.identity = identity;
    }

    public static String[] getSubjects() {
        return subjects;
    }

    public static void setSubjects(String[] subjects) {
        previewReport.subjects = subjects;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        previewReport.status = status;
    }

    public static String getOverall() {
        return overall;
    }

    public static void setOverall(String overall) {
        previewReport.overall = overall;
    }

    public previewReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_preview_report, container, false);
        Term = myview.findViewById(R.id.term);
        Idno = myview.findViewById(R.id.identity);
        Body = myview.findViewById(R.id.subjects);
        Status = myview.findViewById(R.id.status);
        StatusPreview = myview.findViewById(R.id.Imagestatus);
        Term.setText(getTerm());
        Idno.setText(identity);
        //Body.setText(getSubjects());
        if(getStatus().equalsIgnoreCase("pass")){
            StatusPreview.setImageResource(R.drawable.accept);
            Status.setText(globalMethods.InitializeFirstLetter(getStatus()));
            Status.setTextColor(Color.GREEN);
        }else{
            StatusPreview.setImageResource(R.drawable.decline);
            Status.setText(globalMethods.InitializeFirstLetter(getStatus()));
            Status.setTextColor(Color.RED);
        }
        String AllSubjects = "";
        String grade ="";
        int count = 0;
        for (int i = 0; i < getSubjects().length; i++) {
            String[] value = getSubjects()[i].split("-");
            int total=Integer.parseInt(value[1]);
            if (total <=45){
                grade = "Not yet achieved";
            }else if(total >=46 && total < 49){
                grade = "Partial achievement";
            }else if(total >=50 && total < 59){
                grade = "Adequate achievement";
            }else if(total >=60 && total < 69){
                grade = "Grade requirements achieved";
            }else if(total >=70 && total < 79){
                grade = "Grade requirements exceeded";
            }else if (total >=80 && total < 84){
                grade ="Excellent achievement";
            }else{
                grade = "Outstanding achievement";
            }

            AllSubjects+= (++count)+ ".   " + globalMethods.InitializeFirstLetter(value[0]) + " : " + value[1]+"\n"+grade + "\n\n";
        }
        AllSubjects+="\n\n\n\nOverAll Achievement\n"+getOverall();
        Body.setText(AllSubjects);


        return myview;
    }

}

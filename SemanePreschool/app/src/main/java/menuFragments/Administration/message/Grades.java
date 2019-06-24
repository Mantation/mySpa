package menuFragments.Administration.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import io.eyec.bombo.semanepreschool.R;
import properties.accessKeys;

/**
 * A simple {@link Fragment} subclass.
 */
public class Grades extends android.app.Fragment implements View.OnClickListener, View.OnTouchListener{
    View myview;
    CardView GradeRR;
    CardView GradeR;
    CardView Grade1;
    CardView Grade2;
    CardView Grade3;
    CardView Grade4;
    CardView Grade5;
    CardView Grade6;
    CardView Grade7;
    CardView Grade8;
    CardView Grade9;
    CardView Grade10;
    CardView Grade11;
    CardView Grade12;
    EditText Subject;
    EditText Body;
    CardView Submit;

    public Grades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_grades, container, false);
        GradeRR = myview.findViewById(R.id.gradeR);
        GradeR = myview.findViewById(R.id.grade0);
        Grade1 = myview.findViewById(R.id.grade1);
        Grade2 = myview.findViewById(R.id.grade2);
        Grade3 = myview.findViewById(R.id.grade3);
        Grade4 = myview.findViewById(R.id.grade4);
        Grade5 = myview.findViewById(R.id.grade5);
        Grade6 = myview.findViewById(R.id.grade6);
        Grade7 = myview.findViewById(R.id.grade7);
        Grade8 = myview.findViewById(R.id.grade8);
        Grade9 = myview.findViewById(R.id.grade9);
        Grade10 = myview.findViewById(R.id.grade10);
        Grade11 = myview.findViewById(R.id.grade11);
        Grade12 = myview.findViewById(R.id.grade12);
        Subject = myview.findViewById(R.id.Subject);
        Body = myview.findViewById(R.id.body);
        Submit = myview.findViewById(R.id.MySubmit);
        Submit.setOnClickListener(this);
        GradeRR.setOnTouchListener(this);
        GradeR.setOnTouchListener(this);
        Grade1.setOnTouchListener(this);
        Grade2.setOnTouchListener(this);
        Grade3.setOnTouchListener(this);
        Grade4.setOnTouchListener(this);
        Grade5.setOnTouchListener(this);
        Grade6.setOnTouchListener(this);
        Grade7.setOnTouchListener(this);
        Grade8.setOnTouchListener(this);
        Grade9.setOnTouchListener(this);
        Grade10.setOnTouchListener(this);
        Grade11.setOnTouchListener(this);
        Grade12.setOnTouchListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        String newSubject = Subject.getText().toString();
        String newBody = Body.getText().toString();
        if(newSubject.isEmpty() || newSubject.length()<3) {
            Subject.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message heading", Toast.LENGTH_SHORT).show();
        }else if (newBody.isEmpty() || newBody.length()< 20) {
            Body.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message body", Toast.LENGTH_SHORT).show();
        }else{
            if(getGrades().equalsIgnoreCase("")||getGrades().isEmpty()){
                Toast.makeText(getActivity(), "select grade(s) which this message is intended!", Toast.LENGTH_SHORT).show();
            }else{
                All.sendMessageToAll(getActivity(),view, accessKeys.getDefaultUserId(),getGrades(),newSubject,newBody);
            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        switch(id){
            case R.id.gradeR :
                if (GradeRR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    GradeRR.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    GradeRR.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade0 :
                if (GradeR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    GradeR.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    GradeR.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade1 :
                if (Grade1.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade1.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade1.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade2 :
                if (Grade2.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade2.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade2.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade3 :
                if (Grade3.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade3.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade3.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade4 :
                if (Grade4.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade4.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade4.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade5 :
                if (Grade5.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade5.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade5.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade6 :
                if (Grade6.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade6.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade6.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade7 :
                if (Grade7.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade7.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade7.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade8 :
                if (Grade8.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade8.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade8.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade9 :
                if (Grade9.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade9.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade9.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade10 :
                if (Grade10.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade10.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade10.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade11 :
                if (Grade11.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade11.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade11.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
            case R.id.grade12 :
                if (Grade12.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
                    Grade12.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorwhite));
                }else{
                    Grade12.setCardBackgroundColor(getActivity().getResources().getColor(R.color.Purple));
                }
                break;
        }
        return false;
    }

    private String getGrades(){
        String results="";
        if (GradeRR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade RR*";
        }
        if (GradeRR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade R*";
        }
        if (Grade1.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 1*";
        }
        if (Grade2.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 2*";
        }
        if (Grade3.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 3*";
        }
        if (Grade4.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 4*";
        }
        if (Grade5.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 5*";
        }
        if (Grade6.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 6*";
        }
        if (Grade7.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 7*";
        }
        if (Grade8.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 8*";
        }
        if (Grade9.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 9*";
        }
        if (Grade10.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 10*";
        }
        if (Grade11.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 11*";
        }
        if (Grade12.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 12*";
        }
        return results;

    }
}

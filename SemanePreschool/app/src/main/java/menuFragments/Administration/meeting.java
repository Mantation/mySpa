package menuFragments.Administration;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.Meetings.allMeetings;
import menuFragments.Administration.Trips.allTrips;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class meeting extends android.app.Fragment implements View.OnTouchListener,View.OnClickListener{
View myview;
EditText Agenda;
EditText Venue;
EditText Date;
EditText Time;
CardView Submit;
CardView Meetings;
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

    public meeting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_meeting, container, false);
        Agenda = myview.findViewById(R.id.trip);
        Venue = myview.findViewById(R.id.destination);
        Date = myview.findViewById(R.id.date);
        Time = myview.findViewById(R.id.time);
        Submit = myview.findViewById(R.id.MySubmit);
        Meetings = myview.findViewById(R.id.MyMeetings);
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
        Meetings.setOnClickListener(this);
        Submit.setOnClickListener(this);
        Date.setOnTouchListener(this);
        Time.setOnTouchListener(this);
        return myview;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.MyMeetings){
            methods.globalMethods.loadFragments(R.id.main, new allMeetings(),getActivity());
        }else {
            String newAgenda = Agenda.getText().toString();
            String newVenue = Venue.getText().toString();
            String newDate = Date.getText().toString();
            String newTime = Time.getText().toString();

            if (newAgenda.isEmpty() || newAgenda.length() < 10) {
                Agenda.requestFocus();
                Toast.makeText(getActivity(), "enter a descriptive Agenda", Toast.LENGTH_SHORT).show();
            } else if (newVenue.isEmpty() || newVenue.length() < 3) {
                Venue.requestFocus();
                Toast.makeText(getActivity(), "enter a venue", Toast.LENGTH_SHORT).show();
            } else if (newDate.isEmpty()) {
                Date.requestFocus();
                Toast.makeText(getActivity(), "select the date", Toast.LENGTH_SHORT).show();
            } else if (newTime.isEmpty()) {
                Time.requestFocus();
                Toast.makeText(getActivity(), "select the time", Toast.LENGTH_SHORT).show();
            } else {
                if (getGrades().equalsIgnoreCase("") || getGrades().isEmpty()) {
                    Toast.makeText(getActivity(), "select grade(s) which this message is intended!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMeeting(getActivity(), view, getGrades(), newDate, newTime, newAgenda, newVenue);
                }
            }
        }

    }

    public static void sendMeeting(final Activity activity,final View view, final String attendants, final String date, final String time,final String topic, final String venue){
        ShowDialog(activity);
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("attendants", attendants);
            user.put("date", date);
            user.put("time", time);
            user.put("topic",topic);
            user.put("venue",venue);
            user.put("submission date", ToDate());
            user.put("submission time", Time());
            user.put("document ref",defaultvalue);

            // Add a new document with a generated ID
            db.collection(constants.meeting)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.meeting);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        sendMeetingMessage(activity,view,attendants,date,time,topic,venue);
                                    }
                                }

                            });
                            //Client_id(activity,name,surname,email, documentReference.getId(),sex);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            globalMethods.stopProgress = true;
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }


    public static void sendMeetingMessage(final Activity activity,final View view,final String messageTarget,final String date, final String time,final String topic, final String venue){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            String Message = "";
            if(AllSelected){
                Message = "Dear Parent,\n\nKindly avail yourself for this upcoming meeting:\n\n"+
                        "Agenda : "+topic+
                        "\nVenue : "+venue+
                        "\nDate : "+date+
                        "\nTime : "+time+
                        "\n\nYour presence and attendance on this matter would highly be appreciated.\n\nKind Regards,\nManagement";
                user.put("userid", "all");
            }else{
                Message = "Dear Parent,\n\nKindly avail yourself for this upcoming meeting:\n\n"+
                        "Agenda : "+topic+
                        "\nVenue : "+venue+
                        "\nDate : "+date+
                        "\nTime : "+time+
                        "\n\nYour presence and attendance on this matter would highly be appreciated.\n\nKind Regards,\nManagement";
                user.put("userid", messageTarget);

            }
            user.put("subject", "Meeting");
            user.put("message", Message);
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("read", false);
            user.put("from school",true);
            user.put("school Id", accessKeys.getDefaultUserId());
            user.put("document ref",defaultvalue);

            // Add a new document with a generated ID
            db.collection(constants.message)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.message);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        globalMethods.stopProgress = true;
                                        ConfirmResolution(view,"Meetings successfully set");
                                        methods.globalMethods.loadFragments(R.id.main, new allMeetings(),activity);
                                        ConfirmResolution(view,"Message(s) submitted successfully");
                                    }
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            globalMethods.stopProgress = true;
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id==R.id.date || id==R.id.time) {
            trips.InitiateDate(getActivity(), Date, Time);
        }else{
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

        }
        return false;
    }

    static boolean AllSelected;
    private String getGrades(){
        String results="";
        if (GradeRR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade RR*";
        }else{
            AllSelected = false;
        }
        if (GradeRR.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade R*";
        }else{
            AllSelected = false;
        }
        if (Grade1.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 1*";
        }else{
            AllSelected = false;
        }
        if (Grade2.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 2*";
        }else{
            AllSelected = false;
        }
        if (Grade3.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 3*";
        }else{
            AllSelected = false;
        }
        if (Grade4.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 4*";
        }else{
            AllSelected = false;
        }
        if (Grade5.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 5*";
        }else{
            AllSelected = false;
        }
        if (Grade6.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 6*";
        }else{
            AllSelected = false;
        }
        if (Grade7.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 7*";
        }else{
            AllSelected = false;
        }
        if (Grade8.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 8*";
        }else{
            AllSelected = false;
        }
        if (Grade9.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 9*";
        }else{
            AllSelected = false;
        }
        if (Grade10.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 10*";
        }else{
            AllSelected = false;
        }
        if (Grade11.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 11*";
        }else{
            AllSelected = false;
        }
        if (Grade12.getCardBackgroundColor().getDefaultColor() == (getActivity().getResources().getColor(R.color.Purple))){
            results+="grade 12*";
        }else{
            AllSelected = false;
        }
        return results;

    }
}

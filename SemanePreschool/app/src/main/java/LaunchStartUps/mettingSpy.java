package LaunchStartUps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.main;
import menuFragments.parents.home;
import menuFragments.parents.kidsApplication.applicationInfo;
import menuFragments.parents.profile;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

public class mettingSpy {
    public static List profiles = new ArrayList<String>();
    public static List grades = new ArrayList<String>();
    public static final Handler handler = new Handler();
    public static Runnable runnable;
    final static int delay = 1000; //milliseconds
    static int count;


    //Notifications

    public static void MonitorMeetings(final Activity activity){
        handler.postDelayed(new Runnable(){
            public void run(){
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    //get child ID number
    public static void getSelfProfiles(final Activity activity){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                int Total = document.getData().size();
                                if( document.get("userid")!=null){
                                    String Id = document.get("userid").toString();
                                    if (Id.equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                        for (int i = 0; i < Total; i++) {
                                            if(document.get("profile_"+i)!=null){
                                                profiles.add(document.get("profile_"+i).toString());
                                                break;
                                            }

                                        }
                                    }
                                    //profiles.add(document.get("profile_"+Total).toString());
                                    //break;
                                }
                                /*for (int i = 0; i < Total; i++) {
                                    if(document.get("profile_"+i)!=null){
                                        profiles.add(document.get("profile_"+i).toString());
                                        break;
                                    }

                                }*/
                            }
                            if(profiles.size()>0){
                                getAllGrades(activity,profiles);
                            }

                        }

                    }
                });
    }

    //get grades for all IDs
    public static void getSelfGrades(final Activity activity,final List profiles){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String Grades = "";
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("grade") != null && document.get("id") != null) {
                                    for (int i = 0; i < profiles.size(); i++) {
                                        if (profiles.get(i).toString().equalsIgnoreCase(document.get("id").toString())) {
                                            if (grades.size() == 0) {
                                                grades.add("grade " + document.get("grade").toString());
                                            } else {
                                                for (int j = 0; j < grades.size(); j++) {
                                                    if (grades.get(j).toString().equalsIgnoreCase(document.get("grade").toString())) {
                                                        break;
                                                    } else {
                                                        if ((j + 1) == grades.size()) {
                                                            grades.add("grade " + document.get("grade").toString());
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }

                                }
                            }
                            getMeetings(activity,grades);
                        }
                    }
                });
    }



    //get all grades from firestore
    private static  void getAllGrades(final Activity activity,final List profiles){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("", "Error : " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.d("Brand Name: ", doc.getDocument().getId());
                        doc.getDocument().getReference().collection(doc.getDocument().getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("", "Error : " + e.getMessage());
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Log.d("SubBrands Name: ", doc.getDocument().getId());
                                    }
                                }

                            }
                        });
                    }

                }
            }});
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.children)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Documents.add(document.getId());
                            }
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                //Documents.add(document.getId());
                            }
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            //Documents.addAll(myListOfDocuments);
                            //getCompanyInformation(activity);
                            getSelfGrades(activity,profiles);
                        }
                    }
                });
    }

    public static List<String> topic = new ArrayList<String>();
    public static List<String> attendants = new ArrayList<String>();
    public static List<String> venue = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> Document = new ArrayList<String>();
    public static List<String> Parents = new ArrayList<String>();
    public static boolean updateMeeting;
    //get the closest meeting
    public static void getClosestMeeting(final Activity activity, final List Grades){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.meeting)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean found= false;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("attendants") != null && document.get("venue") != null && document.get("topic") != null && document.get("time") != null && document.get("date") != null) {
                                int total = document.getData().size();
                                    for (int i = 0; i < total; i++) {
                                        if(document.get("parent_" + i)!=null){
                                            if(document.get("parent_" + i).toString().equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                                date.add(document.get("date").toString());
                                                time.add(document.get("time").toString());
                                                venue.add(document.get("venue").toString());
                                                topic.add(document.get("topic").toString());
                                                attendants.add(document.get("attendants").toString());
                                                Document.add(document.get("document ref").toString());
                                                Parents.add(document.get("parent_" + i).toString());
                                                found=true;
                                                break;
                                            }else{
                                                date.add(document.get("date").toString());
                                                time.add(document.get("time").toString());
                                                venue.add(document.get("venue").toString());
                                                topic.add(document.get("topic").toString());
                                                attendants.add(document.get("attendants").toString());
                                                Document.add(document.get("document ref").toString());
                                                Parents.add(null);
                                                break;
                                            }
                                        }else{
                                            date.add(document.get("date").toString());
                                            time.add(document.get("time").toString());
                                            venue.add(document.get("venue").toString());
                                            topic.add(document.get("topic").toString());
                                            attendants.add(document.get("attendants").toString());
                                            Document.add(document.get("document ref").toString());
                                            Parents.add(null);
                                            break;
                                        }
                                    }
                                }


                            }
                            final List<String> newDate = new ArrayList<String>();
                            if(!found){
                                //List<String> counter = new ArrayList<String>();
                                int maxdays = 32;
                                for (int i = 0; i < date.size(); i++) {
                                    try {
                                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                        DateTimeZone Mzansi = DateTimeZone.forID("Africa/Johannesburg");
                                        Date now = new Date();
                                        String calledMeetingDay = date.get(i).replace("/","-") + " " + time.get(i)+":00";
                                        //String MeetindDay = dateformat.format(calledMeetingDay);
                                        //String MeetindDay = date.get(i).replace("/","-")+" "+time.get(i)+":00";
                                        Date meetingDate = dateformat.parse(calledMeetingDay);
                                        DateTime Today = new DateTime(now,Mzansi);
                                        DateTime MeetingDay = new DateTime(meetingDate,Mzansi);
                                        //int difference = Days.daysBetween(MeetingDay.withTimeAtStartOfDay(), Today.withTimeAtStartOfDay()).getDays();
                                        //int difference = Days.daysBetween(MeetingDay.toLocalDate(), Today.toLocalDate()).getDays();
                                        int difference = Days.daysBetween(Today.toLocalDate(), MeetingDay.toLocalDate()).getDays();
                                        if ((difference < maxdays) && (difference >= 0)) {
                                            for (int j = 0; j < Grades.size(); j++) {
                                                if(attendants.get(i).equalsIgnoreCase("All")|| attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                    newDate.add(calledMeetingDay + "_" + i);
                                                    //counter.add(String.valueOf(i));
                                                    break;
                                                }
                                            }

                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    /*int MinDay = 1000000;
                                    int MaxDay = 7000000;
                                    String []today = ToDate().split("/");
                                    int Today = Integer.parseInt(today[2]+""+today[1]+""+today[0]);
                                    int forecastDay = Integer.parseInt(date.get(i).replace("/",""));
                                    if(forecastDay - Today>= MinDay && forecastDay - Today < MaxDay){
                                        for (int j = 0; j < Grades.size(); j++) {
                                            if(attendants.get(i).equalsIgnoreCase("All")|| attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                newDate.add(forecastDay + "_" + i);
                                                counter.add(String.valueOf(i));
                                                break;
                                            }
                                        }
                                    }*/

                                }
                                if(newDate.size()>0){
                                    updateMeeting = true;
                                    Collections.sort(newDate);
                                    Random random = new Random();
                                    int number = random.nextInt( newDate.size());
                                    String []index = newDate.get(number).split("_");
                                    if (home.getMeeting()!=null) {
                                        home.getMeeting().setText("Topic : " + topic.get(Integer.parseInt(index[1])) + "\nVenue : " + venue.get(Integer.parseInt(index[1])) + "\n" + date.get(Integer.parseInt(index[1])) + " @ " + time.get(Integer.parseInt(index[1])));
                                        home.getMeeting().setTextColor(Color.RED);
                                    }
                                    home.setMeetingInfo("Topic : "+topic.get(Integer.parseInt(index[1]))+"\nVenue : "+venue.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1]))+" @ "+time.get(Integer.parseInt(index[1])));
                                    count=0;
                                    handler.postDelayed(new Runnable(){
                                        public void run(){
                                            handler.postDelayed(this, delay);
                                            if(count >= newDate.size()){
                                                handler.removeCallbacks(runnable);
                                                handler.removeCallbacksAndMessages(null);
                                            }else {
                                                runnable = this;
                                                String []newIndex = newDate.get(count).split("_");
                                                int index = Integer.parseInt(newIndex[1]);
                                                if (updateMeeting) {
                                                    updateMeeting = false;
                                                    if (Parents.get(index) == null) {
                                                        checkParents(activity, Document.get(index), topic.get(index), venue.get(index), date.get(index), time.get(index));
                                                        count++;
                                                    }else if(Parents.get(index) != null){
                                                        if(!Parents.get(index).equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                                            checkParents(activity, Document.get(index), topic.get(index), venue.get(index), date.get(index), time.get(index));
                                                            count++;
                                                        }else{
                                                            count++;
                                                            updateMeeting = true;
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }, delay);
                                    //checkParents(activity,Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])));
                                    //Toast.makeText(activity, "date : "+ date.get(Integer.parseInt(index[1])) + "\nvanue : "+ venue.get(Integer.parseInt(index[1])), Toast.LENGTH_SHORT).show();
                                }else{
                                    if (home.getMeeting()!=null)
                                        home.getMeeting().setText("Not Scheduled");
                                    home.setMeetingInfo("Not Scheduled");
                                    //Toast.makeText(activity, "No meetings forecasted", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                //List<String> newDate = new ArrayList<String>();
                                int maxdays = 32;
                                for (int i = 0; i < date.size(); i++) {
                                    try {
                                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                        DateTimeZone Mzansi = DateTimeZone.forID("Africa/Johannesburg");
                                        Date now = new Date();
                                        String calledMeetingDay = date.get(i).replace("/","-") + " " + time.get(i)+":00";
                                        //String MeetindDay = dateformat.format(calledMeetingDay);
                                        Date meetingDate = dateformat.parse(calledMeetingDay);
                                        DateTime Today = new DateTime(now, Mzansi);
                                        DateTime MeetingDay = new DateTime(meetingDate, Mzansi);
                                        //int difference = Days.daysBetween(MeetingDay.withTimeAtStartOfDay(), Today.withTimeAtStartOfDay()).getDays();
                                        //int difference = Days.daysBetween(MeetingDay.toLocalDate(), Today.toLocalDate()).getDays();
                                        int difference = Days.daysBetween(Today.toLocalDate(), MeetingDay.toLocalDate()).getDays();
                                        if ((difference < maxdays) && (difference >= 0)) {
                                            for (int j = 0; j < Grades.size(); j++) {
                                                if (attendants.get(i).equalsIgnoreCase("All") || attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                    newDate.add(calledMeetingDay + "_" + i);
                                                    //counter.add(String.valueOf(i));
                                                    break;
                                                }
                                            }

                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //List<String> counter = new ArrayList<String>();
                                /*for (int i = 0; i < date.size(); i++) {
                                    int MinDay = 1000000;
                                    int MaxDay = 7000000;
                                    String []today = ToDate().split("/");
                                    int Today = Integer.parseInt(today[2]+""+today[1]+""+today[0]);
                                    int forecastDay = Integer.parseInt(date.get(i).replace("/",""));
                                    if(forecastDay - Today>= MinDay && forecastDay - Today < MaxDay){
                                        for (int j = 0; j < Grades.size(); j++) {
                                            if(attendants.get(i).equalsIgnoreCase("All")|| attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                newDate.add(forecastDay + "_" + i);
                                                //counter.add(String.valueOf(i));
                                                break;
                                            }
                                        }
                                    }

                                }*/
                                if(newDate.size()>0){
                                    updateMeeting = true;
                                    Collections.sort(newDate);
                                    Random random = new Random();
                                    int number = random.nextInt( newDate.size());
                                    String []index = newDate.get(number).split("_");
                                    if (home.getMeeting()!=null){
                                        home.getMeeting().setText("Topic : " + topic.get(Integer.parseInt(index[1])) + "\nVenue : " + venue.get(Integer.parseInt(index[1])) + "\n" + date.get(Integer.parseInt(index[1])) + " @ " + time.get(Integer.parseInt(index[1])));
                                        home.getMeeting().setTextColor(Color.RED);
                                    }
                                    home.setMeetingInfo("Topic : "+topic.get(Integer.parseInt(index[1]))+"\nVenue : "+venue.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1]))+" @ "+time.get(Integer.parseInt(index[1])));
                                    count=0;
                                    handler.postDelayed(new Runnable(){
                                        public void run(){
                                            handler.postDelayed(this, delay);
                                            if(count >= newDate.size()){
                                                handler.removeCallbacks(runnable);
                                                handler.removeCallbacksAndMessages(null);
                                            }else {
                                                runnable = this;
                                                String []newIndex = newDate.get(count).split("_");
                                                int index = Integer.parseInt(newIndex[1]);
                                                if (updateMeeting) {
                                                    updateMeeting = false;
                                                    if (Parents.get(index) == null) {
                                                        checkParents(activity, Document.get(index), topic.get(index), venue.get(index), date.get(index), time.get(index));
                                                        count++;
                                                    }else if(Parents.get(index) != null){
                                                        if(!Parents.get(index).equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                                            checkParents(activity, Document.get(index), topic.get(index), venue.get(index), date.get(index), time.get(index));
                                                            count++;
                                                        }else{
                                                            count++;
                                                            updateMeeting = true;
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }, delay);
                                    //if(Parents.get(Integer.parseInt(index[1]))==null || !Parents.get(Integer.parseInt(index[1])).equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                    //    checkParents(activity,Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])));
                                    //}
                                    //checkParents(Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])),textView);
                                    //Toast.makeText(activity, "date : "+ date.get(Integer.parseInt(index[1])) + "\nvanue : "+ venue.get(Integer.parseInt(index[1])), Toast.LENGTH_SHORT).show();
                                }else{
                                    if (home.getMeeting()!=null)
                                        home.getMeeting().setText("Not Scheduled");
                                    home.setMeetingInfo("Not Scheduled");
                                    //Toast.makeText(activity, "No meetings forecasted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }

    //get all images from firestore
    private static  void getMeetings(final Activity activity, final List Grades){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.meeting).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("", "Error : " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.d("Brand Name: ", doc.getDocument().getId());
                        doc.getDocument().getReference().collection(doc.getDocument().getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("", "Error : " + e.getMessage());
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Log.d("SubBrands Name: ", doc.getDocument().getId());
                                    }
                                }

                            }
                        });
                    }

                }
            }});
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.meeting)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Documents.add(document.getId());
                            }
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                //Documents.add(document.getId());
                            }
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            //Documents.addAll(myListOfDocuments);
                            //getCompanyInformation(activity);
                            getClosestMeeting(activity, Grades);
                        }
                    }
                });
    }

    //getParents count

    public static void checkParents(final Activity activity,final String documnetRef,final String Agenda,final String Venue, final String dAte, final String Time){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.meeting)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean Notfound = true;
                            int countParents = 0;
                            int count =0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("attendants") != null && document.get("venue") != null && document.get("topic") != null && document.get("time") != null && document.get("date") != null) {
                                    int total = document.getData().size();
                                    while(count < total ){
                                        if (document.get("parent_" + countParents) != null) {
                                            if (accessKeys.getDefaultUserId().equalsIgnoreCase(document.get("parent_" + countParents).toString())) {
                                                Notfound = false;
                                                break;
                                            } else {
                                                countParents++;
                                                Notfound = true;
                                            }
                                        }
                                        count++;
                                    }
                                }
                            }
                            if(Notfound){
                                updateProfiles(activity,documnetRef,countParents,Agenda,Venue,dAte,Time);
                            }else{
                                updateMeeting = true;
                            }


                            /*else{

                                textView.setText("Topic : "+Agenda +"\nVenue : "+Venue+"\nDate : "+dAte+" @ "+Time);
                                textView.setTextColor(Color.RED);
                                home.setMeetingInfo("Topic : "+Agenda +"\nVenue : "+Venue+ "\nDate : "+dAte+" @ "+Time);
                            }*/
                        }
                    }
                });
    }

    //update Parents count with my number adding + 1
    private static void updateProfiles(final Activity activity,final String documnetRef, final int number,final String Agenda,final String Venue, final String dAte, final String Time) {
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("parent_"+ number, accessKeys.getDefaultUserId());

            CollectionReference collectionReference = db.collection(constants.meeting);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "profile successfully updated");
                        Loginfo("profile added successfully");
                        sendApplicationMessage(activity,Agenda,Venue,dAte,Time);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Logerror("unable to add user comments, " + e.getMessage());

                }
            });
        } catch (Exception exception) {
            exception.getMessage();
            exception.printStackTrace();

        }

    }


    public static void sendApplicationMessage(final Activity activity, final String Agenda,final String Venue, final String dAte, final String Time){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("subject", "Meeting");
            user.put("message", "Dear Parent,\n\nKindly avail yourself for this upcoming meeting:\n\nAgenda : "+Agenda+ "\nVenue : "+Venue+"\nDate : "+dAte+"\nTime : " + Time+
                    "\n\nYour presence and attendance on this matter would highly be appreciated.\n\nKind Regards,\nManagement");
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("read", false);
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
                                        Log.i(TAG, "message successfully sent");
                                        Loginfo("message sent successfully");
                                        //globalMethods.setReminder(activity,"Meeting",Agenda,dAte + " " + time);
                                        main.permissionfor = constants.calendar;
                                        main.reminderTitle = constants.AppName + " - Meeting";
                                        main.reminderDescription = Agenda;
                                        main.reminderDate = dAte + " " + Time+":00";
                                        globalMethods.calendarPermissions(activity);
                                    }
                                }

                            });
                            //Client_id(activity,name,surname,email, documentReference.getId(),sex);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error sending message", e);
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }



}

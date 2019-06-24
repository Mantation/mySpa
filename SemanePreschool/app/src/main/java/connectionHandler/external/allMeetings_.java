package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.AdminMeetingAdapter;
import adapters.AdminTripAdapter;
import constants.constants;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class allMeetings_ extends Application {

    public static List<String> topic = new ArrayList<String>();
    public static List<String> attendants = new ArrayList<String>();
    public static List<String> venue = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> SubmittedDate = new ArrayList<String>();
    public static List<String> SubmittedTime = new ArrayList<String>();
    public static AdminMeetingAdapter adminMeetingAdapter;

    public static void getAllMeetingsDocuments(final Activity activity, final RecyclerView recyclerView, final TextView textView) {
        clearList(topic);
        clearList(attendants);
        clearList(venue);
        clearList(date);
        clearList(time);
        clearList(SubmittedTime);
        clearList(SubmittedDate);
        //gets all documents from firestore
        getMeetings(activity,recyclerView, textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }



    public static void checkMeeting(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.meeting)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("attendants") != null && document.get("venue") != null && document.get("topic") != null && document.get("time") != null && document.get("date") != null && document.get("submission date") != null && document.get("submission time") != null) {
                                    topic.add(document.get("topic").toString());
                                    attendants.add(document.get("attendants").toString());
                                    venue.add(document.get("venue").toString());
                                    date.add(document.get("date").toString());
                                    time.add(document.get("time").toString());
                                    SubmittedTime.add(document.get("submission time").toString());
                                    SubmittedDate.add(document.get("submission date").toString());
                                }
                            }
                            String[] mytopic = new String[topic.size()];
                            String[] myattendance = new String[attendants.size()];
                            String[] myvenue = new String[venue.size()];
                            String[] mydate = new String[date.size()];
                            String[] mytime = new String[time.size()];
                            String[] mySubdate = new String[SubmittedDate.size()];
                            String[] mySubtime = new String[SubmittedTime.size()];

                            for (int i = 0; i < topic.size(); i++) {
                                mytopic[i] = topic.get(i);
                                myattendance[i] = attendants.get(i);
                                myvenue[i] = venue.get(i);
                                mydate[i] = date.get(i);
                                mytime[i] = time.get(i);
                                mySubdate[i] = SubmittedDate.get(i);
                                mySubtime[i] = SubmittedTime.get(i);
                            }

                            //sort ascending
                            String temptopic = "";
                            String tempdate = "";
                            String temptime = "";
                            String tempprice = "";
                            String tempvenue= "";
                            String tempattendance="";
                            String tempSubdate= "";
                            String tempSubtime="";
                            for (int i = 0; i < mytopic.length; i++) {
                                for (int j = i; j < mytopic.length - 1; j++) {
                                    int MyDate = Integer.parseInt(mydate[j+1].replace("/",""));
                                    int MyTime = Integer.parseInt(mytime[j+1].replace(":",""));
                                    int MyOldDate = Integer.parseInt(mydate[i].replace("/",""));
                                    int MyOldTime = Integer.parseInt(mytime[i].replace(":",""));
                                    if(MyOldDate<MyDate)
                                    {
                                        temptopic = mytopic [j + 1];
                                        mytopic [j + 1]= mytopic [i];
                                        mytopic [i] = temptopic;

                                        tempattendance = myattendance [j + 1];
                                        myattendance [j + 1]= myattendance [i];
                                        myattendance [i] = tempattendance;

                                        tempvenue = myvenue [j + 1];
                                        myvenue [j + 1]= myvenue [i];
                                        myvenue [i] = tempvenue;

                                        tempdate = mydate [j + 1];
                                        mydate [j + 1]= mydate [i];
                                        mydate [i] = tempdate;

                                        temptime = mytime [j + 1];
                                        mytime [j + 1]= mytime [i];
                                        mytime [i] = temptime;

                                        tempSubdate = mySubdate [j + 1];
                                        mySubdate [j + 1]= mySubdate [i];
                                        mySubdate [i] = tempSubdate;

                                        tempSubtime = mySubtime [j + 1];
                                        mySubtime [j + 1]= mySubtime [i];
                                        mySubtime [i] = tempSubtime;

                                    }else{
                                        if (MyOldDate==MyDate){
                                            if(MyOldTime<MyTime)
                                            {
                                                temptopic = mytopic [j + 1];
                                                mytopic [j + 1]= mytopic [i];
                                                mytopic [i] = temptopic;

                                                tempattendance = myattendance [j + 1];
                                                myattendance [j + 1]= myattendance [i];
                                                myattendance [i] = tempattendance;

                                                tempvenue = myvenue [j + 1];
                                                myvenue [j + 1]= myvenue [i];
                                                myvenue [i] = tempvenue;

                                                tempdate = mydate [j + 1];
                                                mydate [j + 1]= mydate [i];
                                                mydate [i] = tempdate;

                                                temptime = mytime [j + 1];
                                                mytime [j + 1]= mytime [i];
                                                mytime [i] = temptime;

                                                tempSubdate = mySubdate [j + 1];
                                                mySubdate [j + 1]= mySubdate [i];
                                                mySubdate [i] = tempSubdate;

                                                tempSubtime = mySubtime [j + 1];
                                                mySubtime [j + 1]= mySubtime [i];
                                                mySubtime [i] = tempSubtime;

                                            }

                                        }

                                    }

                                }
                            }

                            adminMeetingAdapter = new AdminMeetingAdapter(activity, activity);
                            adminMeetingAdapter.setTopic(mytopic);
                            adminMeetingAdapter.setAttendants(myattendance);
                            adminMeetingAdapter.setVenue(myvenue);
                            adminMeetingAdapter.setDate(mydate);
                            adminMeetingAdapter.setTime(mytime);
                            adminMeetingAdapter.setSubmissionDate(mySubdate);
                            adminMeetingAdapter.setSubmissionTime(mySubtime);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(adminMeetingAdapter);
                            if(mytopic.length == 0)
                                textView.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }


    //get all images from firestore
    private static  void getMeetings(final Activity activity, final RecyclerView recyclerView, final TextView textView){
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
                            checkMeeting(activity,recyclerView,textView);
                        }
                    }
                });
    }




}

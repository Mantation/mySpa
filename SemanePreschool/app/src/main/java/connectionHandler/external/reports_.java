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

import adapters.KidsAdapter;
import adapters.ReportAdapter;
import adapters.ThemeAdapter;
import constants.constants;
import menuFragments.parents.termTheme;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class reports_ extends Application {

    //get Kids under my profile
    public static List<String> Profile = new ArrayList<String>();

    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView, final TextView textView) {
        clearList(Profile);
        //gets all documents from firestore
        getFirestoreCategory(activity,recyclerView,textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategories(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        //gets all documents from firestore
        clearList(Profile);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.get("userid")!=null){
                                    String user = document.get("userid").toString();
                                    if (user.equals(accessKeys.getDefaultUserId())) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        int Total = document.getData().size();
                                        String profile = "profile_";
                                        String status = "status_";
                                        String name = "name_";
                                        String surname = "surname_";
                                        for (int i = 0; i < Total; i++) {
                                            if (document.get(profile+i)!=null && document.get(status+i)!=null && document.get(name+i)!=null && document.get(surname+i)!=null){
                                                if(document.get(status+i).toString().equalsIgnoreCase("Accepted")){
                                                    Profile.add(document.get(profile+i).toString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            getAllReportsDocuments(activity,recyclerView,textView);
                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity,final RecyclerView recyclerView, final TextView textView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.users)
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
                            getFirestoreCategories(activity,recyclerView,textView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }

    //Get All Reports

    public static List<String> subject = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> term = new ArrayList<String>();
    public static List<String> pupil = new ArrayList<String>();
    public static List<String> total = new ArrayList<String>();
    public static List<String> docRef = new ArrayList<String>();
    static ReportAdapter reportAdapter;


    public static void getAllReportsDocuments(final Activity activity, final RecyclerView recyclerView, final TextView textView) {
        clearList(subject);
        clearList(date);
        clearList(time);
        clearList(term);
        clearList(pupil);
        clearList(total);
        clearList(docRef);
        //gets all documents from firestore
        getFirestoreCategoryReports(activity,recyclerView,textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategoriesReports(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        //gets all documents from firestore
        clearList(subject);
        clearList(date);
        clearList(time);
        clearList(term);
        clearList(pupil);
        clearList(total);
        clearList(docRef);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.report)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (Profile.size() < 1) {
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                for (DocumentSnapshot document : task.getResult()) {
                                    if(document.get("pupil")!=null && document.get("date") !=null && document.get("time") !=null && document.get("term") !=null && document.get("document ref") !=null && document.get("total")!=null){
                                        for (int i = 0; i < Profile.size(); i++) {
                                            if(document.get("pupil").toString().equalsIgnoreCase(Profile.get(i))){
                                                    int Total = Integer.parseInt(document.get("total").toString());
                                                    String Subject ="";
                                                    for (int j = 0; j < Total; j++) {
                                                        if(document.get("subject "+j)!=null) {
                                                            Subject+=document.get("subject "+j).toString()+"*";
                                                        }else{
                                                            break;
                                                        }
                                                    }
                                                    pupil.add(document.get("pupil").toString());
                                                    total.add(String.valueOf(Total));
                                                    subject.add(Subject);
                                                    date.add(document.get("date").toString());
                                                    time.add(document.get("time").toString());
                                                    term.add(document.get("term").toString());
                                                    docRef.add(document.get("document ref").toString());
                                                    textView.setVisibility(View.GONE);
                                            }
                                        }

                                    }
                                }

                                String[] myPupil = new String[pupil.size()];
                                String[] myTotal = new String[total.size()];
                                String[] mySubject = new String[subject.size()];
                                String[] mydate = new String[date.size()];
                                String[] mytime = new String[time.size()];
                                String[] myterm = new String[term.size()];
                                String[] mydocRef = new String[docRef.size()];

                                for (int i = 0; i < pupil.size(); i++) {
                                    myPupil[i] = pupil.get(i);
                                    myTotal[i] = total.get(i);
                                    mySubject[i] = subject.get(i);
                                    mydate[i] = date.get(i);
                                    mytime[i] = time.get(i);
                                    myterm[i] = term.get(i);
                                    mydocRef[i] = docRef.get(i);
                                }

                                //sort ascending
                                String tempPupil = "";
                                String temptotal= "";
                                String tempsubject = "";
                                String tempdate = "";
                                String temptime = "";
                                String tempterm= "";
                                String tempDocument="";
                                for (int i = 0; i < myPupil.length; i++) {
                                    for (int j = i; j < myPupil.length - 1; j++) {
                                        int MyDate = Integer.parseInt(mydate[j+1].replace("/",""));
                                        int MyTime = Integer.parseInt(mytime[j+1].replace(":",""));
                                        int MyOldDate = Integer.parseInt(mydate[i].replace("/",""));
                                        int MyOldTime = Integer.parseInt(mytime[i].replace(":",""));
                                        if(MyOldDate<MyDate)
                                        {
                                            tempPupil = myPupil [j + 1];
                                            myPupil [j + 1]= myPupil [i];
                                            myPupil [i] = tempPupil;

                                            temptotal = myTotal [j + 1];
                                            myTotal [j + 1]= myTotal [i];
                                            myTotal [i] = temptotal;

                                            tempsubject = mySubject [j + 1];
                                            mySubject [j + 1]= mySubject [i];
                                            mySubject [i] = tempsubject;

                                            tempdate = mydate [j + 1];
                                            mydate [j + 1]= mydate [i];
                                            mydate [i] = tempdate;

                                            temptime = mytime [j + 1];
                                            mytime [j + 1]= mytime [i];
                                            mytime [i] = temptime;


                                            tempterm = myterm [j + 1];
                                            myterm [j + 1]= myterm [i];
                                            myterm [i] = tempterm;


                                            tempDocument = mydocRef [j + 1];
                                            mydocRef [j + 1]= mydocRef [i];
                                            mydocRef [i] = tempDocument;


                                        }else{
                                            if (MyOldDate==MyDate){
                                                if(MyOldTime<MyTime)
                                                {
                                                    tempPupil = myPupil [j + 1];
                                                    myPupil [j + 1]= myPupil [i];
                                                    myPupil [i] = tempPupil;

                                                    temptotal = myTotal [j + 1];
                                                    myTotal [j + 1]= myTotal [i];
                                                    myTotal [i] = temptotal;

                                                    tempsubject = mySubject [j + 1];
                                                    mySubject [j + 1]= mySubject [i];
                                                    mySubject [i] = tempsubject;

                                                    tempdate = mydate [j + 1];
                                                    mydate [j + 1]= mydate [i];
                                                    mydate [i] = tempdate;

                                                    temptime = mytime [j + 1];
                                                    mytime [j + 1]= mytime [i];
                                                    mytime [i] = temptime;


                                                    tempterm = myterm [j + 1];
                                                    myterm [j + 1]= myterm [i];
                                                    myterm [i] = tempterm;


                                                    tempDocument = mydocRef [j + 1];
                                                    mydocRef [j + 1]= mydocRef [i];
                                                    mydocRef [i] = tempDocument;
                                                }

                                            }

                                        }

                                    }
                                }

                                reportAdapter = new ReportAdapter(activity, activity);
                                reportAdapter.setPupil(myPupil);
                                reportAdapter.setTotal(myTotal);
                                reportAdapter.setSubject(mySubject);
                                reportAdapter.setDate(mydate);
                                reportAdapter.setTime(mytime);
                                reportAdapter.setTerm(myterm);
                                reportAdapter.setDocumentRef(mydocRef);
                                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                                recyclerView.setAdapter(reportAdapter);


                            }
                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategoryReports(final Activity activity,final RecyclerView recyclerView, final TextView textView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.report).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.report)
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
                            getFirestoreCategoriesReports(activity,recyclerView,textView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }






}

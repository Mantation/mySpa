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
import com.google.android.gms.tasks.OnSuccessListener;
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

import adapters.AdminReportsAdapter;
import adapters.ApplicationsAdapter;
import adapters.ReportAdapter;
import constants.constants;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class childReport_ extends Application {

    public static void getAllGrades(final Activity activity, final RecyclerView recyclerView,final String Id) {
        //gets all documents from firestore
        getFirestoreGrade(activity,recyclerView,Id);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }



    //Grades

    //get all documents from firestore
    private static  void getFirestoreGrade(final Activity activity,final RecyclerView recyclerView,final String Id){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.grade).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.grade)
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
                            getGrades(activity,recyclerView,Id);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }

    public static void getGrades(final Activity activity, final RecyclerView recyclerView,final String Id){
        //gets all documents from firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.grade).document("")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int Total = 0;
                        String Subjects="";
                        if (documentSnapshot.get("total")!=null) {
                            Total = Integer.parseInt(documentSnapshot.get("total").toString());
                            for (int i = 0; i < Total; i++) {
                                Subjects+=documentSnapshot.get("subject "+i).toString()+"";
                            }
                            //getAllReportsDocuments(activity,recyclerView,Id);
                        }

                    }
                });

    }


//Reports
    public static List<String> Term = new ArrayList<String>();
    public static List<String> All = new ArrayList<String>();
    public static List<String> ID = new ArrayList<String>();
    public static List<String> Subjects = new ArrayList<String>();
    public static List<String> documentRef = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> year = new ArrayList<String>();
    public static List<String> pupil = new ArrayList<String>();
    static AdminReportsAdapter adminReportAdapter;


    public static void getAllReportsDocuments(final Activity activity, final RecyclerView recyclerView,final String Id) {
        clearList(Term);
        clearList(All);
        clearList(ID);
        clearList(Subjects);
        clearList(documentRef);
        clearList(date);
        clearList(time);
        clearList(year);
        clearList(pupil);
        //gets all documents from firestore
        getFirestoreCategoryReports(activity,recyclerView,Id);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategoriesReports(final Activity activity, final RecyclerView recyclerView, final String Id){
        //gets all documents from firestore
        clearList(Term);
        clearList(All);
        clearList(ID);
        clearList(Subjects);
        clearList(documentRef);
        clearList(date);
        clearList(time);
        clearList(year);
        clearList(pupil);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.report)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    if(document.get("pupil")!=null && document.get("date") !=null && document.get("time") !=null && document.get("term") !=null && document.get("document ref") !=null && document.get("total")!=null){
                                        if (document.get("pupil").toString().equalsIgnoreCase(Id)) {
                                            int Total = Integer.parseInt(document.get("total").toString());
                                            String Subject = "";
                                            for (int j = 0; j < Total; j++) {
                                                if (document.get("subject " + j) != null) {
                                                    Subject += document.get("subject " + j).toString() + "*";
                                                } else {
                                                    break;
                                                }
                                            }
                                            String term = document.get("term").toString();
                                            pupil.add(document.get("pupil").toString());
                                            Term.add(term);
                                            All.add(String.valueOf(Total));
                                            ID.add(Id);
                                            Subjects.add(Subject);
                                            documentRef.add(document.get("document ref").toString());
                                            date.add(document.get("date").toString());
                                            time.add(document.get("time").toString());
                                            year.add(document.get("date").toString().substring(0,4));
                                        }
                                    }
                                }
                                String[] myPupil = new String[pupil.size()];
                                String[] myterm = new String[Term.size()];
                                String[] myID = new String[ID.size()];
                                String[] myTotal = new String[All.size()];
                                String[] mySubject = new String[Subjects.size()];
                                String[] mydocRef = new String[documentRef.size()];
                                String[] mydate = new String[date.size()];
                                String[] mytime = new String[time.size()];
                                String[] myyear = new String[year.size()];

                                for (int i = 0; i < Term.size(); i++) {
                                    myPupil[i] = pupil.get(i);
                                    myID[i] = ID.get(i);
                                    myTotal[i] = All.get(i);
                                    mySubject[i] = Subjects.get(i);
                                    myterm[i] = Term.get(i);
                                    mydocRef[i] = documentRef.get(i);
                                    mydate[i] = date.get(i);
                                    mytime[i] = time.get(i);
                                    myyear[i] = year.get(i);
                                }

                                //sort ascending
                                String tempPupil = "";
                                String tempID= "";
                                String temptotal= "";
                                String tempsubject = "";
                                String tempdate = "";
                                String temptime = "";
                                String tempyear = "";
                                String tempterm= "";
                                String tempDocument="";
                                for (int i = 0; i < myterm.length; i++) {
                                    for (int j = i; j < myterm.length - 1; j++) {
                                        int MyDate = Integer.parseInt(mydate[j+1].replace("/",""));
                                        int MyTime = Integer.parseInt(mytime[j+1].replace(":",""));
                                        int MyOldDate = Integer.parseInt(mydate[i].replace("/",""));
                                        int MyOldTime = Integer.parseInt(mytime[i].replace(":",""));
                                        if(MyOldDate<MyDate)
                                        {
                                            tempPupil = myPupil [j + 1];
                                            myPupil [j + 1]= myPupil [i];
                                            myPupil [i] = tempPupil;

                                            tempID = myID [j + 1];
                                            myID [j + 1]= myID [i];
                                            myID [i] = tempID;

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

                                            tempyear = myyear [j + 1];
                                            myyear [j + 1]= myyear [i];
                                            myyear [i] = tempyear;

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

                                                    tempID = myID [j + 1];
                                                    myID [j + 1]= myID [i];
                                                    myID [i] = tempID;

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

                                                    tempyear = myyear [j + 1];
                                                    myyear [j + 1]= myyear [i];
                                                    myyear [i] = tempyear;

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

                            adminReportAdapter = new AdminReportsAdapter(activity, activity);
                            adminReportAdapter.setID(myID);
                            adminReportAdapter.setPupil(myPupil);
                            adminReportAdapter.setTotal(myTotal);
                            adminReportAdapter.setSubject(mySubject);
                            adminReportAdapter.setDate(mydate);
                            adminReportAdapter.setTime(mytime);
                            adminReportAdapter.setYear(myyear);
                            adminReportAdapter.setTerm(myterm);
                            adminReportAdapter.setDocumentRef(mydocRef);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(adminReportAdapter);
                            globalMethods.stopProgress = true;


                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategoryReports(final Activity activity,final RecyclerView recyclerView,final String Id){
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
                            getFirestoreCategoriesReports(activity,recyclerView,Id);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }

}

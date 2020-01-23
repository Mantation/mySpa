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

import adapters.MaintenanceAdapter;
import adapters.PipesAdapter;
import constants.constants;

import static methods.globalMethods.clearList;

public class openPipeTasks extends Application {
    public static List<String> Image = new ArrayList<String>();
    public static List<String> Time = new ArrayList<String>();
    public static List<String> Date = new ArrayList<String>();
    public static List<String> Status = new ArrayList<String>();
    public static List<String> documentRef = new ArrayList<String>();
    public static List<String> description = new ArrayList<String>();
    public static List<String> pause = new ArrayList<String>();
    public static List<String> phases = new ArrayList<String>();
    public static List<String> jobtype = new ArrayList<String>();
    static PipesAdapter pipesAdapter;
    /*Relevant-Knowledge

[Main Instruction]
    Relevant-Knowledge has stopped working

[Content]
    Windows is checking for a solution to the problem...

            [Cancel];*/

    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView, final TextView textView) {
        clearList(Image);
        clearList(Time);
        clearList(Date);
        clearList(Status);
        clearList(documentRef);
        clearList(description);
        clearList(pause);
        clearList(phases);
        clearList(jobtype);
        //gets all documents from firestore
        getFirestoreOpenedTasks(activity,recyclerView,textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }
    public static void getFirestoreTasks(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        //gets all documents from firestore
        clearList(Image);
        clearList(Time);
        clearList(Date);
        clearList(Status);
        clearList(documentRef);
        clearList(description);
        clearList(pause);
        clearList(phases);
        clearList(jobtype);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.pipes)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.get("InitialPicture") != null && document.get("InitialDate") != null && document.get("InitialDescription") != null && document.get("InitialTime") != null && document.get("document ref") != null && document.get("status") != null) {
                                    if (document.get("status").toString().equalsIgnoreCase("paused") || document.get("status").toString().equalsIgnoreCase("open")) {
                                        Image.add(document.get("InitialPicture").toString());
                                        Time.add(document.get("InitialTime").toString());
                                        Date.add(document.get("InitialDate").toString());
                                        Status.add(document.get("status").toString());
                                        documentRef.add(document.get("document ref").toString());
                                        description.add(document.get("InitialDescription").toString());
                                        phases.add(document.get("phase").toString());
                                        jobtype.add(document.get("jobType").toString());
                                        int total = document.getData().size();
                                        for (int i = 0; i < total; i++) {
                                            if(document.get("pause_"+i)==null && document.get("reopen_"+i)==null){
                                                if(i==0){
                                                    pause.add("unavailable");
                                                    break;
                                                }else{
                                                    String Reopen = document.get("reopen_"+(i-1)).toString();
                                                    pause.add(Reopen);
                                                    break;
                                                }
                                            }else{
                                                if(document.get("reopen_"+i)==null){
                                                    if(i==0){
                                                        String Pause = document.get("pause_0").toString();
                                                        pause.add(Pause);
                                                        break;
                                                    }else {
                                                        if(document.get("pause_" + (i+1))==null){
                                                            String Pause = document.get("pause_" + (i)).toString();
                                                            pause.add(Pause);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                            String[] MyStatus = new String[Status.size()];
                            String[] MyImage = new String[Image.size()];
                            String[] Mydate = new String[Date.size()];
                            String[] MyDocRef = new String[documentRef.size()];
                            String[] Mytime = new String[Time.size()];
                            String[] MyDescription = new String[description.size()];
                            String[] MyPause = new String[pause.size()];
                            String[] MyPhase = new String[phases.size()];
                            String[] MyJobtype = new String[jobtype.size()];

                            for (int i = 0; i < Status.size(); i++) {
                                MyStatus[i] = Status.get(i);
                                MyImage[i] = Image.get(i);
                                Mytime[i] = Time.get(i);
                                Mydate[i] = Date.get(i);
                                MyDocRef[i] = documentRef.get(i);
                                MyDescription[i] = description.get(i);
                                MyPause[i] = pause.get(i);
                                MyPhase[i] = phases.get(i);
                                MyJobtype[i] = jobtype.get(i);

                            }

                            //sort ascending
                            String tempstatus = "";
                            String tempdate = "";
                            String temptime = "";
                            String tempimage = "";
                            String tempDocument="";
                            String tempDescription="";
                            String tempPause="";
                            String tempPhase="";
                            String tempJObtype="";
                            for (int i = 0; i < MyStatus.length; i++) {
                                for (int j = i; j < MyStatus.length - 1; j++) {
                                    int MyDate = Integer.parseInt(Mydate[j+1].replace("/",""));
                                    int MyTime = Integer.parseInt(Mytime[j+1].replace(":",""));
                                    int MyOldDate = Integer.parseInt(Mydate[i].replace("/",""));
                                    int MyOldTime = Integer.parseInt(Mytime[i].replace(":",""));
                                    if(MyOldDate<MyDate)
                                    {
                                        tempdate = Mydate [j + 1];
                                        Mydate [j + 1]= Mydate [i];
                                        Mydate [i] = tempdate;

                                        temptime = Mytime [j + 1];
                                        Mytime [j + 1]= Mytime [i];
                                        Mytime [i] = temptime;

                                        tempstatus = MyStatus [j + 1];
                                        MyStatus [j + 1]= MyStatus [i];
                                        MyStatus [i] = tempstatus;

                                        tempimage = MyImage [j + 1];
                                        MyImage [j + 1]= MyImage [i];
                                        MyImage [i] = tempimage;

                                        tempDocument = MyDocRef [j + 1];
                                        MyDocRef [j + 1]= MyDocRef [i];
                                        MyDocRef [i] = tempDocument;

                                        tempDescription = MyDescription [j + 1];
                                        MyDescription [j + 1]= MyDescription [i];
                                        MyDescription [i] = tempDescription;

                                        tempPause = MyPause [j + 1];
                                        MyPause [j + 1]= MyPause [i];
                                        MyPause [i] = tempPause;

                                        tempPhase = MyPhase [j + 1];
                                        MyPhase [j + 1]= MyPhase [i];
                                        MyPhase [i] = tempPhase;

                                        tempJObtype = MyJobtype [j + 1];
                                        MyJobtype [j + 1]= MyJobtype [i];
                                        MyJobtype [i] = tempJObtype;

                                    }else{
                                        if (MyOldDate==MyDate){
                                            if(MyOldTime<MyTime)
                                            {
                                                tempdate = Mydate [j + 1];
                                                Mydate [j + 1]= Mydate [i];
                                                Mydate [i] = tempdate;

                                                temptime = Mytime [j + 1];
                                                Mytime [j + 1]= Mytime [i];
                                                Mytime [i] = temptime;

                                                tempstatus = MyStatus [j + 1];
                                                MyStatus [j + 1]= MyStatus [i];
                                                MyStatus [i] = tempstatus;

                                                tempimage = MyImage [j + 1];
                                                MyImage [j + 1]= MyImage [i];
                                                MyImage [i] = tempimage;

                                                tempDocument = MyDocRef [j + 1];
                                                MyDocRef [j + 1]= MyDocRef [i];
                                                MyDocRef [i] = tempDocument;

                                                tempDescription = MyDescription [j + 1];
                                                MyDescription [j + 1]= MyDescription [i];
                                                MyDescription [i] = tempDescription;

                                                tempPause = MyPause [j + 1];
                                                MyPause [j + 1]= MyPause [i];
                                                MyPause [i] = tempPause;

                                                tempPhase = MyPhase [j + 1];
                                                MyPhase [j + 1]= MyPhase [i];
                                                MyPhase [i] = tempPhase;

                                                tempJObtype = MyJobtype [j + 1];
                                                MyJobtype [j + 1]= MyJobtype [i];
                                                MyJobtype [i] = tempJObtype;

                                            }

                                        }

                                    }

                                }
                            }


                            pipesAdapter = new PipesAdapter(activity);
                            pipesAdapter.setImage(MyImage);
                            pipesAdapter.setDate(Mydate);
                            pipesAdapter.setTime(Mytime);
                            pipesAdapter.setStatus(MyStatus);
                            pipesAdapter.setDocumentRef(MyDocRef);
                            pipesAdapter.setDescription(MyDescription);
                            pipesAdapter.setPauses(MyPause);
                            pipesAdapter.setPhases(MyPhase);
                            pipesAdapter.setJobtype(MyJobtype);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(pipesAdapter);
                            if (Status.size() > 0) textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private static  void getFirestoreOpenedTasks(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.pipes).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.pipes)
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
                            getFirestoreTasks(activity,recyclerView,textView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }




}

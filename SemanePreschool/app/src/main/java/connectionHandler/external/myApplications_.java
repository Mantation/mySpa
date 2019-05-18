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

import adapters.ApplicationsAdapter;
import adapters.KidsAdapter;
import constants.constants;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class myApplications_ extends Application {


    public static List<String> Profile = new ArrayList<String>();
    public static List<String> Status = new ArrayList<String>();
    public static List<String> Name = new ArrayList<String>();
    public static List<String> Surname = new ArrayList<String>();
    public static List<String> IdNumber = new ArrayList<String>();
    static ApplicationsAdapter applicationsAdapter;


    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView, final TextView textView) {
        clearList(Profile);
        clearList(Status);
        clearList(Name);
        clearList(Surname);
        clearList(IdNumber);
        //gets all documents from firestore
        getFirestoreCategory(activity,recyclerView,textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategories(final Activity activity, final RecyclerView recyclerView, final TextView textView){
        //gets all documents from firestore
        clearList(Profile);
        clearList(Status);
        clearList(Name);
        clearList(Surname);
        clearList(IdNumber);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.apply)
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
                                        if (document.get("child_name")!=null && document.get("child_surname")!=null && document.get("child_id")!=null && document.get("child_Image")!=null && document.get("proof Of Payment")!=null && document.get("status")!=null ){
                                            if(!document.get("child_Image").toString().equalsIgnoreCase("null") && !document.get("child_Image").toString().equalsIgnoreCase("n/a")&& !document.get("proof Of Payment").toString().equalsIgnoreCase("null") && !document.get("proof Of Payment").toString().equalsIgnoreCase("n/a"))
                                                Profile.add(document.get("child_Image").toString());
                                                Status.add(document.get("status").toString());
                                                Name.add(document.get("child_name").toString());
                                                Surname.add(document.get("child_surname").toString());
                                                IdNumber.add(document.get("child_id").toString());
                                                textView.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                            if(Profile.size()<1)
                                textView.setVisibility(View.VISIBLE);


                            String []MyProfile = new String [Profile.size()];
                            String []MyStatus = new String [Status.size()];
                            String []MyName = new String [Name.size()];
                            String []MySurname = new String [Surname.size()];
                            String []MyIdNumber = new String [IdNumber.size()];

                            for (int i = 0; i < Profile.size(); i++) {
                                MyProfile[i] = Profile.get(i);
                                MyStatus[i] = Status.get(i);
                                MyName[i] = Name.get(i);
                                MySurname[i] = Surname.get(i);
                                MyIdNumber[i] = IdNumber.get(i);
                            }
                            applicationsAdapter = new ApplicationsAdapter(activity,activity);
                            applicationsAdapter.setName(MyName);
                            applicationsAdapter.setSurname(MySurname);
                            applicationsAdapter.setProfile(MyProfile);
                            applicationsAdapter.setStatus(MyStatus);
                            applicationsAdapter.setIDNumber(MyIdNumber);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(applicationsAdapter);


                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity,final RecyclerView recyclerView, final TextView textView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.apply).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.apply)
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





}

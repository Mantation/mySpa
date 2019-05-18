package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import constants.constants;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class myKids_ extends Application {


    public static List<String> Profile = new ArrayList<String>();
    public static List<String> Status = new ArrayList<String>();
    public static List<String> Name = new ArrayList<String>();
    public static List<String> Surname = new ArrayList<String>();
    static KidsAdapter kidsAdapter;


    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView) {
        clearList(Profile);
        clearList(Status);
        clearList(Name);
        clearList(Surname);
        //gets all documents from firestore
        getFirestoreCategory(activity,recyclerView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategories(final Activity activity, final RecyclerView recyclerView){
        //gets all documents from firestore
        clearList(Profile);
        clearList(Status);
        clearList(Name);
        clearList(Surname);
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
                                            if (document.get(profile+i)!=null && document.get(status+i)!=null){
                                                Profile.add(document.get(profile+i).toString());
                                                Status.add(document.get(status+i).toString());
                                                Name.add(document.get(name+i).toString());
                                                Surname.add(document.get(surname+i).toString());
                                            }
                                        }
                                    }
                                }
                            }
                            String []MyProfile = new String [Profile.size()];
                            String []MyStatus = new String [Status.size()];
                            String []MyName = new String [Name.size()];
                            String []MySurname = new String [Surname.size()];

                            for (int i = 0; i < Profile.size(); i++) {
                                MyProfile[i] = Profile.get(i);
                                MyStatus[i] = Status.get(i);
                                MyName[i] = Name.get(i);
                                MySurname[i] = Surname.get(i);
                            }
                            kidsAdapter = new KidsAdapter(activity,activity);
                            kidsAdapter.setName(MyName);
                            kidsAdapter.setSurname(MySurname);
                            kidsAdapter.setProfile(MyProfile);
                            kidsAdapter.setStatus(MyStatus);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(kidsAdapter);


                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity,final RecyclerView recyclerView){
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
                            getFirestoreCategories(activity,recyclerView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }




}

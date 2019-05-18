package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import adapters.ThemeAdapter;
import constants.constants;
import menuFragments.parents.termTheme;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class themes_ extends Application {

    public static List<String> name = new ArrayList<String>();
    public static List<String> description = new ArrayList<String>();
    static ThemeAdapter themeAdapter;


    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView) {
        clearList(name);
        clearList(description);
        //gets all documents from firestore
        getFirestoreCategory(activity,recyclerView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategories(final Activity activity, final RecyclerView recyclerView){
        //gets all documents from firestore
        clearList(name);
        clearList(description);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.themes)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                termTheme.setTerm(document.get("term").toString());
                                    for (int j = 0; j < document.getData().size(); j++) {
                                        if(document.get("name"+j)!=null && document.get("description"+j)!=null){
                                            name.add(document.get("name"+j).toString());
                                            description.add(document.get("description"+j).toString());
                                        }
                                    }
                                }
                            String []Name = new String [name.size()];
                            String []Description = new String [description.size()];

                            for (int i = 0; i < name.size(); i++) {
                                Name[i] = name.get(i);
                                Description[i] = description.get(i);
                            }
                            themeAdapter = new ThemeAdapter(activity,activity);
                            themeAdapter.setName(Name);
                            themeAdapter.setDescription(Description);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(themeAdapter);


                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity,final RecyclerView recyclerView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.themes).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.themes)
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

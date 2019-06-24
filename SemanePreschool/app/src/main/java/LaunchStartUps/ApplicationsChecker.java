package LaunchStartUps;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import java.util.List;

import constants.constants;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;

public class ApplicationsChecker {

    public static final Handler handler = new Handler();
    final static int delay = 500; //milliseconds

    //Notifications

    public static void MonitorNotifications(final Activity activity, final CardView cardView, final TextView textView){
        handler.postDelayed(new Runnable(){
            public void run(){
                getNotifications(activity, cardView,  textView);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    public static void getPendingMessageNotifications(final Activity activity, final CardView cardView, final TextView textView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.apply)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            cardView.setVisibility(View.GONE);
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("child_name")!=null && document.get("child_surname")!=null && document.get("child_id")!=null && document.get("child_Image")!=null && document.get("proof Of Payment")!=null && document.get("status")!=null && document.get("date")!=null && document.get("time")!=null&& document.get("Guardian2_Workphone")!=null &&
                                        document.get("Guardian2_addresss1")!=null && document.get("Guardian2_addresss2")!=null && document.get("Guardian2_addresss3")!=null && document.get("Guardian2_addresssCode")!=null && document.get("Guardian2_addresssTown")!=null && document.get("Guardian2_addresssProvince")!=null && document.get("Guardian2_AddressSameAsChild")!=null &&
                                        document.get("Guardian2_employer")!=null && document.get("Guardian2_email")!=null&& document.get("Guardian2_relationship")!=null&& document.get("Guardian2_name")!=null&& document.get("Guardian2_surname")!=null && document.get("Guardian2_Homephone")!=null&& document.get("child_grade")!=null && document.get("child_gender")!=null &&
                                        document.get("child_addresss1")!=null && document.get("child_addresss2")!=null&& document.get("child_addresss3")!=null&& document.get("child_addresssCode")!=null&& document.get("child_addresssTown")!=null && document.get("child_addresssProvince")!=null&& document.get("child_MedicalConditions")!=null && document.get("child_HasAllergies")!=null &&
                                        document.get("Child_Allergies")!=null && document.get("userid")!=null){
                                        if(document.get("status").toString().equalsIgnoreCase("pending")) {
                                            count++;
                                            cardView.setVisibility(View.VISIBLE);
                                            textView.setText("");
                                            textView.setText(String.valueOf(count));
                                        }

                                }

                            }
                            //begin monitoring
                            //MonitorNewMessages(activity,cardView,textView);
                        }

                    }
                });
    }

    //get all images from firestore
    private static  void getNotifications(final Activity activity, final CardView cardView, final TextView textView){
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
                            getPendingMessageNotifications(activity,cardView,textView);
                        }
                    }
                });
    }







}

package menuFragments.Administration;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.generateReport.generateReport;
import methods.globalMethods;

import static methods.globalMethods.ShowDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class academicReports extends android.app.Fragment implements View.OnClickListener{
    View myview;
    EditText Child;
    CardView Add;
    RecyclerView recyclerView;


    public academicReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_academic_reports, container, false);
        Child = myview.findViewById(R.id.search_child);
        Add = myview.findViewById(R.id.addReport);
        Add.setVisibility(View.GONE);
        recyclerView = myview.findViewById(R.id.reports_history);
        trackR_ID(getActivity(),recyclerView);
        Add.setOnClickListener(this);
        return myview;
    }

    private void trackR_ID(final Activity activity,final RecyclerView recyclerView) {
        Child.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Child.getText().toString().length() < 13) {
                    Child.setTextColor(Color.RED);
                    Add.setVisibility(View.GONE);
                } else {
                    getFirestorePupils(activity,recyclerView,s.toString(),Add);
                    Child.setTextColor(Color.BLACK);
                    Add.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                /*if (Child.getText().toString().length() < 13) {
                    Child.setTextColor(Color.RED);
                } else {
                    getFirestorePupils(activity,s.toString(),textView,constraintLayout);
                    Child.setTextColor(Color.BLACK);
                }*/

            }
        });
    }

    //get all documents from firestore
    private static void getFirestorePupils(final Activity activity,final RecyclerView recyclerView, final String id,final CardView cardView) {
        ShowDialog(activity);
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
            }
        });
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.children).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    getKidsInfo(activity,recyclerView,id,cardView);
                    //getIssues(activity,context, view, recyclerView); //ammended
                }
            }
        });
    }

    public static void getKidsInfo(final Activity activity,final RecyclerView recyclerView, final String id, final CardView cardView) {
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean isFound = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.get("id") != null && document.get("name") != null && document.get("surname") != null && document.get("gender") != null && document.get("grade") != null && document.get("addressline1") != null && document.get("addressline2") != null &&
                                document.get("addressline3") != null && document.get("town") != null && document.get("code") != null) {
                            if (document.get("id").toString().equalsIgnoreCase(id)) {
                                isFound = true;
                                Grade = "grade"+document.get("grade").toString();
                                Id = id;
                                break;
                            }

                        }
                    }
                    if (!isFound) {
                        cardView.setVisibility(View.GONE);
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "No such child enrolled in this school", Toast.LENGTH_SHORT).show();
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                        connectionHandler.external.childReport_.getAllReportsDocuments(activity,recyclerView,id);
                    }
                }
            }
        });

    }

    static String Grade;
    static String Id;
    public static void getGrade(final Activity activity,  final String id, final String grade) {
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.grade).document(grade).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int total = Integer.parseInt(documentSnapshot.get("total").toString());
                String Subject = "";
                for (int i = 0; i < total; i++) {
                    Subject+= documentSnapshot.get("subject "+i).toString() + "*";
                }
                generateReport.setID(id);
                generateReport.setTotal(total);
                generateReport.setSubjects(Subject);
                methods.globalMethods.loadFragments(R.id.main, new generateReport(), activity);

            }
        });

    }


    @Override
    public void onClick(View view) {
        getGrade(getActivity(),Id,Grade);
    }
}

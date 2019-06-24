package menuFragments.Administration.message;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.sendMessages;
import io.eyec.bombo.semanepreschool.sendMultiMessages;
import menuFragments.Administration.messages;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class User extends android.app.Fragment implements View.OnClickListener {
    View myview;
    EditText Child;
    TextView ChildInfo;
    ConstraintLayout ChildLayout;
    EditText Subject;
    EditText Body;
    CardView Submit;
    static String Id;
    static ArrayList<String> Users = new ArrayList<>();


    public User() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_user, container, false);
        Child = myview.findViewById(R.id.search_child);
        ChildInfo = myview.findViewById(R.id.info);
        ChildLayout = myview.findViewById(R.id.childLayout);
        Subject = myview.findViewById(R.id.Subject);
        Body = myview.findViewById(R.id.body);
        Submit = myview.findViewById(R.id.MySubmit);
        Submit.setOnClickListener(this);
        ChildLayout.setVisibility(View.GONE);
        trackR_ID(getActivity(), ChildInfo, ChildLayout);
        return myview;
    }

    private void trackR_ID(final Activity activity, final TextView textView, final ConstraintLayout constraintLayout) {
        Child.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Child.getText().toString().length() < 13) {
                    Child.setTextColor(Color.RED);
                    constraintLayout.setVisibility(View.GONE);
                } else {
                    getFirestorePupils(activity, s.toString(), textView, constraintLayout);
                    Child.setTextColor(Color.BLACK);
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

    static boolean launched = false;
    static boolean threadIsDone = false;
    static int count;
    @Override
    public void onClick(View view) {
        String newSubject = Subject.getText().toString();
        String newBody = Body.getText().toString();
        if (newSubject.isEmpty() || newSubject.length() < 3) {
            Subject.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message heading", Toast.LENGTH_SHORT).show();
        } else if (newBody.isEmpty() || newBody.length() < 20) {
            Subject.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message body", Toast.LENGTH_SHORT).show();
        } else {
            count =0;
            getFirestoreParents(getActivity(), view, accessKeys.getDefaultUserId(), Id, newSubject, newBody);
            //if(Users.size()<1){
            //    Toast.makeText(getActivity(), "Pupil not registered under any parent", Toast.LENGTH_SHORT).show();
            //}else{

            //}
        }

    }

    //get all documents from firestore
    private static void getFirestorePupils(final Activity activity, final String id, final TextView textView, final ConstraintLayout constraintLayout) {
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
                    getKidsInfo(activity, id, textView, constraintLayout);
                    //getIssues(activity,context, view, recyclerView); //ammended
                }
            }
        });
    }

    public static void getKidsInfo(final Activity activity, final String id, final TextView textView, final ConstraintLayout constraintLayout) {
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int total = task.getResult().size();
                    int count = 0;
                    boolean isFound = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                        if (document.get("id") != null) {
                            if (document.get("id").toString().equalsIgnoreCase(id)) {
                                isFound = true;
                                String Profile = "";
                                if (document.get("name") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("name").toString()) + " ";
                                }
                                if (document.get("surname") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("surname").toString() + "\n");
                                }
                                if (document.get("id") != null) {
                                    Profile += "ID no: " + globalMethods.InitializeFirstLetter(document.get("id").toString() + "\n");
                                }
                                if (document.get("gender") != null) {
                                    Profile += "Sex : " + globalMethods.InitializeFirstLetter(document.get("gender").toString() + "\n");
                                }
                                if (document.get("grade") != null) {
                                    Profile += "Grade : " + globalMethods.InitializeFirstLetter(document.get("grade").toString() + "\n");
                                }
                                    Profile += "Address Details\n";
                                if (document.get("addressline1") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("addressline1").toString() + "\n");
                                }
                                if (document.get("addressline2") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("addressline2").toString() + "\n");
                                }
                                if (document.get("addressline3") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("addressline3").toString() + "\n");
                                }
                                if (document.get("town") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("town").toString() + "\n");
                                }
                                if (document.get("code") != null) {
                                    Profile += globalMethods.InitializeFirstLetter(document.get("code").toString());
                                }
                                textView.setText(Profile);
                                constraintLayout.setVisibility(View.VISIBLE);
                                Id = id;
                            }

                        }
                        if (isFound) {
                            constraintLayout.setVisibility(View.VISIBLE);
                            globalMethods.stopProgress = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        constraintLayout.setVisibility(View.GONE);
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "No such child enrolled in this school", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //get all documents from firestore
    private static void getFirestoreParents(final Activity activity,final View view, final String userId,final String messageTarget, final String subject, final String body) {
        ShowDialog(activity);
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
            }
        });
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.users).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    getParentsInfo(activity,view,userId,messageTarget,subject,body);
                    //getIssues(activity,context, view, recyclerView); //ammended
                }
            }
        });
    }
    static ArrayList<String> Token = new ArrayList<>();
    public static void getParentsInfo(final Activity activity,final View view, final String userId,final String messageTarget, final String subject, final String body) {
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int total = task.getResult().size();
                    int count = 0;
                    boolean isFound = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        int documentTotal = document.getData().size();
                        for (int i = 0; i < documentTotal; i++) {
                            if (document.get("profile_" + i) != null) {
                                if (document.get("profile_" + i).toString().equalsIgnoreCase(messageTarget)) {
                                    Users.add(document.get("userid").toString());
                                    Token.add(document.get("userid").toString());
                                    break;
                                }
                            }

                        }
                    }
                    for (int i = 0; i < Users.size(); i++) {
                        count++;
                        if (Users.size()==1) {
                            sendMessageToAll(activity, view, userId, Users.get(i), subject, body);
                            break;
                        }else {
                            while (!launched) {
                                if (threadIsDone) {
                                    threadIsDone = false;
                                    sendMessageToAll(activity, view, userId, Users.get(i), subject, body);
                                }
                            }
                        }

                    }
                    if (Users.size()==0) {
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "Pupil not registered under any parent", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public static void sendMessageToAll(final Activity activity,final View view, final String userId,final String messageTarget, final String subject, final String body){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", messageTarget);
            user.put("subject", subject);
            user.put("message", body);
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("read", false);
            user.put("from school",true);
            user.put("school Id",userId);
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
                                        threadIsDone = true;
                                        if(count == Users.size()){
                                            launched = true;
                                            globalMethods.stopProgress = true;
                                            ConfirmResolution(view,"Message(s) submitted successfully");
                                            methods.globalMethods.loadFragments(R.id.main, new messages(),activity);
                                            launched = false;
                                            threadIsDone =false;
                                            if(Token.size()>1){
                                                sendMultiMessages send = new sendMultiMessages();
                                                send.Tokens = Token;
                                                new sendMultiMessages().execute(subject,body);
                                            }else{
                                                new sendMessages().execute(subject,body,Token.get(0));
                                            }
                                        }else{
                                            globalMethods.stopProgress = true;
                                            ConfirmResolution(view,"Message(s) submitted successfully");
                                            methods.globalMethods.loadFragments(R.id.main, new messages(),activity);

                                        }
                                    }
                                }

                            });
                            //Client_id(activity,name,surname,email, documentReference.getId(),sex);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            globalMethods.stopProgress = true;
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }
}


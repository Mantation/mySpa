package menuFragments.parents.userProfile;


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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import menuFragments.parents.home;
import menuFragments.parents.profile;
import menuFragments.registration.stepsInfo;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.InitializeFirstLetter;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class userKids extends android.app.Fragment implements View.OnClickListener{
View myview;
EditText Child;
TextView ChildInfo;
ConstraintLayout ChildLayout;
Spinner Relationship;
CardView Add;
static String Id;
static int ChildNo;
static ArrayList <String> children = new ArrayList<>();
static String DocumentRef;

    public userKids() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_user_kids, container, false);
        Child = myview.findViewById(R.id.search_child);
        ChildInfo = myview.findViewById(R.id.info);
        ChildLayout = myview.findViewById(R.id.childLayout);
        Relationship = myview.findViewById(R.id.Relationship);
        Add = myview.findViewById(R.id.MySubmit);
        getuserRefference();
        Add.setOnClickListener(this);
        ChildLayout.setVisibility(View.GONE);
        trackR_ID(getActivity(),ChildInfo,ChildLayout);
        String[] relationship = getActivity().getResources().getStringArray(R.array.relationship);
        ArrayAdapter<String> relationshipAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, relationship);
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Relationship.setAdapter(relationshipAdapter);
        return myview;
    }

    private  void trackR_ID(final Activity activity, final TextView textView, final ConstraintLayout constraintLayout) {
        Child.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Child.getText().toString().length() < 13) {
                    Child.setTextColor(Color.RED);
                    constraintLayout.setVisibility(View.GONE);
                } else {
                    getFirestorePupils(activity,s.toString(),textView,constraintLayout);
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

    //get all documents from firestore
    private static  void getFirestorePupils(final Activity activity ,final String id, final TextView textView, final ConstraintLayout constraintLayout){
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
            }});
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.children)
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
                            getKidsInfo(activity,id,textView,constraintLayout);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }

    private static String KidName;
    private static String KidSurname;

    public static String getKidName() {
        return KidName;
    }

    public static void setKidName(String kidName) {
        KidName = kidName;
    }

    public static String getKidSurname() {
        return KidSurname;
    }

    public static void setKidSurname(String kidSurname) {
        KidSurname = kidSurname;
    }

    public static void getKidsInfo(final Activity activity, final String id, final TextView textView, final ConstraintLayout constraintLayout){
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total = task.getResult().size();
                            int count = 0;
                            boolean isFound = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                                if(document.get("id")!=null ) {
                                    if (document.get("id").toString().equalsIgnoreCase(id)) {
                                        isFound = true;
                                        String Profile = "";
                                        if (document.get("name") != null) {
                                            Profile += globalMethods.InitializeFirstLetter(document.get("name").toString()) + " ";
                                            setKidName(document.get("name").toString());
                                        }
                                        if (document.get("surname") != null) {
                                            Profile += globalMethods.InitializeFirstLetter(document.get("surname").toString() + "\n");
                                            setKidSurname(document.get("surname").toString());
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
                                if (isFound){
                                    constraintLayout.setVisibility(View.VISIBLE);
                                    globalMethods.stopProgress = true;
                                    break;
                                }
                            }
                            if (!isFound){
                                constraintLayout.setVisibility(View.GONE);
                                globalMethods.stopProgress = true;
                                Toast.makeText(activity, "No such child enrolled in this school", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public static void getuserRefference(){
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.get("document ref")!=null && document.get("userid")!=null) {
                                    String userId = document.get("userid").toString();
                                    if (userId.equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                        DocumentRef = document.get("document ref").toString();
                                        int documents = document.getData().size();
                                        for (int i = 0; i < documents; i++) {
                                            String ID = "profile_"+i;
                                            if (document.get(ID) != null){
                                                ChildNo = 1;
                                                children.add(document.get(ID).toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        String relationship = Relationship.getSelectedItem().toString();
        if (relationship.equalsIgnoreCase("-- select one --")) {
            Relationship.requestFocus();
            Toast.makeText(getActivity(), "Relationship field is required, please make a selection", Toast.LENGTH_SHORT).show();
        }else{
            boolean found = false;
            for (int i = 0; i < children.size(); i++) {
                if(Id.equalsIgnoreCase(children.get(i))){
                    found = true;
                    break;
                }
            }
            if(found){
                Toast.makeText(getActivity(), "You cannot add a profile you already have!", Toast.LENGTH_SHORT).show();
            }else{
                updateChildProfiles(getActivity(),view,ChildNo,Id,relationship,DocumentRef);
            }

        }

    }



    private static void updateChildProfiles(final Activity activity, final View view, final int Number, final String IdNumber, final String relationship, final String documnetRef) {
        ShowDialog(activity);
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("profile_"+ Number, IdNumber);
            user.put("status_"+Number, "pending");
            user.put("relationship_"+Number, relationship);
            user.put("name_"+Number, getKidName());
            user.put("surname_"+Number, getKidSurname());

            CollectionReference collectionReference = db.collection(constants.users);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        globalMethods.stopProgress = true;
                        ConfirmResolution(view, "Child information successfully added");
                        methods.globalMethods.loadFragments(R.id.main, new profile(), activity);
                        sendApplicationMessage(activity,accessKeys.getDefaultUserId(),IdNumber);
                    } else {
                        globalMethods.stopProgress = true;
                        ConfirmResolution(view, "unable to add child Information, please retry!");

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Logerror("unable to add user comments, " + e.getMessage());
                    globalMethods.stopProgress = true;

                }
            });
        } catch (Exception exception) {
            exception.getMessage();
            exception.printStackTrace();

        }

    }

    public static void sendApplicationMessage(final Activity activity, final String userId,final String Id){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", userId);
            user.put("subject", "Addition of "+InitializeFirstLetter(getKidName())+" " + InitializeFirstLetter(getKidSurname()));
            user.put("message", "Dear Parent,\n\nKindly note that your application for child with ID no. "+Id+", is being reviewed. We will respond to you shortly.\n\nKind Regards,\nManagement");
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("read", false);
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
                                        home.updateBadger(1);
                                        //Toast.makeText(this, "Document created/updated", Toast.LENGTH_SHORT).show();
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

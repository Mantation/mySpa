package menuFragments.Administration.generateReport;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.sendMessages;
import io.eyec.bombo.semanepreschool.sendMultiMessages;
import menuFragments.Administration.academicReports;
import methods.globalMethods;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.clearList;

/**
 * A simple {@link Fragment} subclass.
 */
public class generateReport extends android.app.Fragment implements View.OnClickListener{
View myview;
Spinner Term;
TextView Id;
TextView Sub1;
EditText Subject1;
TextView Sub2;
EditText Subject2;
TextView Sub3;
EditText Subject3;
TextView Sub4;
EditText Subject4;
TextView Sub5;
EditText Subject5;
TextView Sub6;
EditText Subject6;
TextView Sub7;
EditText Subject7;
TextView Sub8;
EditText Subject8;
TextView Sub9;
EditText Subject9;
TextView Sub10;
EditText Subject10;
CardView Submit;

static int Total;
static String ID;
static String term;
static String subjects;

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        generateReport.ID = ID;
    }

    public static String getTerm() {
        return term;
    }

    public static void setTerm(String term) {
        generateReport.term = term;
    }

    public static String getSubjects() {
        return subjects;
    }

    public static void setSubjects(String subjects) {
        generateReport.subjects = subjects;
    }

    public static int getTotal() {
        return Total;
    }

    public static void setTotal(int total) {
        Total = total;
    }

    public generateReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_generate, container, false);
        Term = myview.findViewById(R.id.term);
        Id = myview.findViewById(R.id.identity);
        Submit = myview.findViewById(R.id.MySubmit);
        //Term.setText(getTerm());
        String[] term = getActivity().getResources().getStringArray(R.array.terms);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, term);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Term.setAdapter(termAdapter);
        Id.setText(getID());
        Sub1 = myview.findViewById(R.id.sub1);
        Subject1 = myview.findViewById(R.id.subject1);
        Sub2 = myview.findViewById(R.id.sub2);
        Subject2 = myview.findViewById(R.id.subject2);
        Sub3 = myview.findViewById(R.id.sub3);
        Subject3 = myview.findViewById(R.id.subject3);
        Sub4 = myview.findViewById(R.id.sub4);
        Subject4 = myview.findViewById(R.id.subject4);
        Sub5 = myview.findViewById(R.id.sub5);
        Subject5 = myview.findViewById(R.id.subject5);
        Sub6 = myview.findViewById(R.id.sub6);
        Subject6 = myview.findViewById(R.id.subject6);
        Sub7 = myview.findViewById(R.id.sub7);
        Subject7 = myview.findViewById(R.id.subject7);
        Sub8 = myview.findViewById(R.id.sub8);
        Subject8 = myview.findViewById(R.id.subject8);
        Sub9 = myview.findViewById(R.id.sub9);
        Subject9 = myview.findViewById(R.id.subject9);
        Sub10 = myview.findViewById(R.id.sub10);
        Subject10 = myview.findViewById(R.id.subject10);
        setAll();
        Submit.setOnClickListener(this);

        return myview;
    }

    private void setAll(){
        String[]subs = getSubjects().split("\\*");
        switch (getTotal()){
            case 1 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            break;
            case 2 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            break;
            case 3 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            break;
            case 4 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            break;
            case 5 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            break;
            case 6 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub6.setVisibility(View.VISIBLE);
            Subject6.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            Sub6.setText(subs[5]);
            break;
            case 7 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub6.setVisibility(View.VISIBLE);
            Subject6.setVisibility(View.VISIBLE);
            Sub7.setVisibility(View.VISIBLE);
            Subject7.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            Sub6.setText(subs[5]);
            Sub7.setText(subs[6]);
            break;
            case 8 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub6.setVisibility(View.VISIBLE);
            Subject6.setVisibility(View.VISIBLE);
            Sub7.setVisibility(View.VISIBLE);
            Subject7.setVisibility(View.VISIBLE);
            Sub8.setVisibility(View.VISIBLE);
            Subject8.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            Sub6.setText(subs[5]);
            Sub7.setText(subs[6]);
            Sub8.setText(subs[7]);
            break;
            case 9 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub6.setVisibility(View.VISIBLE);
            Subject6.setVisibility(View.VISIBLE);
            Sub7.setVisibility(View.VISIBLE);
            Subject7.setVisibility(View.VISIBLE);
            Sub8.setVisibility(View.VISIBLE);
            Subject8.setVisibility(View.VISIBLE);
            Sub9.setVisibility(View.VISIBLE);
            Subject9.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            Sub6.setText(subs[5]);
            Sub7.setText(subs[6]);
            Sub8.setText(subs[7]);
            Sub9.setText(subs[8]);
            break;
            case 10 :Sub1.setVisibility(View.VISIBLE);
            Subject1.setVisibility(View.VISIBLE);
            Sub2.setVisibility(View.VISIBLE);
            Subject2.setVisibility(View.VISIBLE);
            Sub3.setVisibility(View.VISIBLE);
            Subject3.setVisibility(View.VISIBLE);
            Sub4.setVisibility(View.VISIBLE);
            Subject4.setVisibility(View.VISIBLE);
            Sub5.setVisibility(View.VISIBLE);
            Subject5.setVisibility(View.VISIBLE);
            Sub6.setVisibility(View.VISIBLE);
            Subject6.setVisibility(View.VISIBLE);
            Sub7.setVisibility(View.VISIBLE);
            Subject7.setVisibility(View.VISIBLE);
            Sub8.setVisibility(View.VISIBLE);
            Subject8.setVisibility(View.VISIBLE);
            Sub9.setVisibility(View.VISIBLE);
            Subject9.setVisibility(View.VISIBLE);
            Sub10.setVisibility(View.VISIBLE);
            Subject10.setVisibility(View.VISIBLE);
            Sub1.setText(subs[0]);
            Sub2.setText(subs[1]);
            Sub3.setText(subs[2]);
            Sub4.setText(subs[3]);
            Sub5.setText(subs[4]);
            Sub6.setText(subs[5]);
            Sub7.setText(subs[6]);
            Sub8.setText(subs[7]);
            Sub9.setText(subs[8]);
            Sub10.setText(subs[9]);
            break;
        }
    }

    @Override
    public void onClick(View view) {
        String term = Term.getSelectedItem().toString();
        //Points
        myEditText[0] = Subject1;
        myEditText[1] = Subject2;
        myEditText[2] = Subject3;
        myEditText[3] = Subject4;
        myEditText[4] = Subject5;
        myEditText[5] = Subject6;
        myEditText[6] = Subject7;
        myEditText[7] = Subject8;
        myEditText[8] = Subject9;
        myEditText[9] = Subject10;
        //Subjects
        myTextview[0] = Sub1;
        myTextview[1] = Sub2;
        myTextview[2] = Sub3;
        myTextview[3] = Sub4;
        myTextview[4] = Sub5;
        myTextview[5] = Sub6;
        myTextview[6] = Sub7;
        myTextview[7] = Sub8;
        myTextview[8] = Sub9;
        myTextview[9] = Sub10;


        if (term.isEmpty() || term.equalsIgnoreCase("-- select one --")) {
            Term.requestFocus();
            Toast.makeText(getActivity(), "please select a term", Toast.LENGTH_SHORT).show();
        }else{
            //SubjectSet Trigger
            subjectsSet = true;
            for (int i = 0; i < getTotal(); i++) {
                if (myEditText[i].getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "please enter marks", Toast.LENGTH_SHORT).show();
                    myEditText[i].requestFocus();
                    subjectsSet = false;
                    break;
                }
            }
            if(subjectsSet) {
                getAmounts(getActivity(), view, getID(), term);
            }
        }


    }

    EditText[] myEditText = new EditText[10];
    TextView[] myTextview = new TextView[10];
    boolean subjectsSet;
    private void getAmounts(final Activity activity, final View view, final String Student, final String Term){
        ShowDialog(getActivity());
            try {
                String defaultvalue = "n/a";
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
               
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("pupil", Student);
                user.put("term", Term);
                user.put("total", getTotal());
                user.put("date", ToDate());
                user.put("time", Time());
                user.put("document ref", defaultvalue);
                for (int i = 0; i < getTotal(); i++) {
                    if (myEditText[i]!=null || myTextview[i]!=null) {
                        String value = myEditText[i].getText().toString();
                        String subject = myTextview[i].getText().toString();
                        if (!value.isEmpty() || !subject.isEmpty()) {
                            user.put("subject " + i, myTextview[i].getText().toString() + "-" + myEditText[i].getText().toString());
                        }
                    }else{
                        break;
                    }
                }
                // Add a new document with a generated ID
                db.collection(constants.report).add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        final String document = documentReference.getId();
                        CollectionReference collectionReference = db.collection(constants.report);
                        collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ConfirmResolution(view, "Message(s) submitted successfully");
                                    getParentsInfo(activity,view,Student,Term);
                                }
                            }

                        });
                        //Client_id(activity,name,surname,email, documentReference.getId(),sex);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        globalMethods.stopProgress = true;
                    }
                });
            } catch (Exception exception) {
                exception.getMessage();
                exception.printStackTrace();
            }

        }

    static ArrayList<String> Users = new ArrayList<>();
    static ArrayList<String> Token = new ArrayList<>();
    static boolean launched = false;
    static boolean threadIsDone = false;
    static int count;
    public static void getParentsInfo(final Activity activity, final View view, final String child, final String Term) {
        clearList(Users);
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean isFound = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        int documentTotal = document.getData().size();
                        for (int i = 0; i < documentTotal; i++) {
                            if (document.get("profile_" + i) != null && document.get("token")!= null) {
                                if (document.get("profile_" + i).toString().equalsIgnoreCase(child)) {
                                    Users.add(document.get("userid").toString());
                                    Token.add(document.get("token").toString());
                                    break;
                                }
                            }

                        }
                    }
                    for (int i = 0; i < Users.size(); i++) {
                        count++;
                        if (Users.size()==1) {
                            //send message
                            sendMessageToAll(activity, view, Users.get(i),Term);
                            //send notification
                            break;
                        }else {
                            //send notification
                            while (!launched) {
                                if (threadIsDone) {
                                    threadIsDone = false;
                                    sendMessageToAll(activity, view, Users.get(i),Term);
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

    public static void sendMessageToAll(final Activity activity,final View view, final String messageTarget,final String Term){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", messageTarget);
            user.put("subject", Term + " - Report");
            user.put("message", "Dear Parent\n\nKindly find progress report for the "+Term+". Kindly let us know should you have any questions whatsoever.\n\nKind Regards,\nManagement");
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("read", false);
            user.put("document ref",defaultvalue);
            final String Message = "Dear Parent\n\nKindly find progress report for the "+Term+". Kindly let us know should you have any questions whatsoever.\n\nKind Regards,\nManagement";

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
                                            globalMethods.stopProgress = true;
                                            launched = true;
                                            ConfirmResolution(view,"Message(s) submitted successfully");
                                            methods.globalMethods.loadFragments(R.id.main, new academicReports(),activity);
                                            launched = false;
                                            threadIsDone =false;
                                            if(Token.size()>1){
                                                sendMultiMessages send = new sendMultiMessages();
                                                send.Tokens = Token;
                                                new sendMultiMessages().execute(Term + " - Report",Message);
                                            }else{
                                                new sendMessages().execute(Term + " - Report",Message,Token.get(0));
                                            }
                                        }else{
                                            globalMethods.stopProgress = true;
                                            ConfirmResolution(view,"Message(s) submitted successfully");
                                            methods.globalMethods.loadFragments(R.id.main, new academicReports(),activity);
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

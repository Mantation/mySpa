package menuFragments.Administration.message;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.messages;
import menuFragments.parents.home;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.InitializeFirstLetter;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class All extends android.app.Fragment implements View.OnClickListener{
View myview;
EditText Subject;
EditText Body;
CardView Submit;

    public All() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_all, container, false);
        Subject = myview.findViewById(R.id.Subject);
        Body = myview.findViewById(R.id.body);
        Submit = myview.findViewById(R.id.MySubmit);
        Submit.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
        String newSubject = Subject.getText().toString();
        String newBody = Body.getText().toString();
        if(newSubject.isEmpty() || newSubject.length()<3) {
            Subject.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message heading", Toast.LENGTH_SHORT).show();
        }else if (newBody.isEmpty() || newBody.length()< 20) {
            Body.requestFocus();
            Toast.makeText(getActivity(), "enter a descriptive message body", Toast.LENGTH_SHORT).show();
        }else{
            sendMessageToAll(getActivity(),view, accessKeys.getDefaultUserId(),"all",newSubject,newBody);
        }

    }

    public static void sendMessageToAll(final Activity activity,final View view, final String userId,final String messageTarget, final String subject, final String body){
        ShowDialog(activity);
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
                                        globalMethods.stopProgress = true;
                                        ConfirmResolution(view,"Message(s) submitted successfully");
                                        methods.globalMethods.loadFragments(R.id.main, new messages(),activity);
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

    public static void submitPushNotification(){

    }
}

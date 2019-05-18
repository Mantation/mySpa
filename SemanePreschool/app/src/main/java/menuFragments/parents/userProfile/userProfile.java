package menuFragments.parents.userProfile;


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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.parents.profile;
import menuFragments.registration.stepsInfo;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class userProfile extends android.app.Fragment implements View.OnClickListener{
    View myview;
    EditText Name;
    EditText Surname;
    RadioButton Male;
    RadioButton Female;
    EditText Phone;
    CardView Submit;

    static String name;
    static String surname;
    static String gender;
    static String phone;
    static String DocumentRef;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        userProfile.name = name;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        userProfile.surname = surname;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        userProfile.gender = gender;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        userProfile.phone = phone;
    }

    public userProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Name = myview.findViewById(R.id.Name);
        Surname = myview.findViewById(R.id.Surname);
        Male = myview.findViewById(R.id.male);
        Female = myview.findViewById(R.id.female);
        Phone  = myview.findViewById(R.id.Phone);
        Submit = myview.findViewById(R.id.MySubmit);
        getProfileInfo(Name,Surname,Male,Female,Phone);
        Submit.setOnClickListener(this);
        return myview;
    }

    public static void getProfileInfo(final EditText Name, final EditText Surname, final RadioButton Male, final RadioButton Female, final EditText Phone){
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
                                        if (document.get("name") != null) {
                                            Name.setText(document.get("name").toString());
                                            setName(document.get("name").toString());
                                        }
                                        if (document.get("surname") != null) {
                                            Surname.setText(document.get("surname").toString());
                                            setSurname(document.get("surname").toString());
                                        }
                                        if (document.get("gender") != null) {
                                            if (document.get("gender").toString().equalsIgnoreCase("male")) {
                                                Male.setChecked(true);
                                            } else {
                                                Female.setChecked(true);
                                            }
                                            setGender(document.get("gender").toString());
                                        }
                                        if (document.get("phone") != null) {
                                            Phone.setText(document.get("phone").toString());
                                            setPhone(document.get("phone").toString());
                                        }

                                        if (document.get("document ref") != null) {
                                            DocumentRef = document.get("document ref").toString();
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
        String newName = Name.getText().toString();
        String newSurname = Surname.getText().toString();
        String newPhone = Phone.getText().toString();
        String newGender = "";
        boolean update = false;

        if (Male.isChecked()) {
            newGender = "Male";
        }else{
            newGender = "Male";
        }

        if (!newName.equalsIgnoreCase(getName()) && !newName.isEmpty()){
            update = true;
        }
        if (!newSurname.equalsIgnoreCase(getSurname()) && !newSurname.isEmpty()){
            update = true;
        }
        if (!newPhone.equalsIgnoreCase(getPhone()) && !newPhone.isEmpty()){
            update = true;
        }
        if (!newGender.equalsIgnoreCase(getGender()) && !newGender.isEmpty()){
            update = true;
        }

        if(update){
            updateUserInfo(getActivity(),view,newName,newSurname,newGender,newPhone,DocumentRef);
        }else{
            Toast.makeText(getActivity(), "No information has been updated", Toast.LENGTH_SHORT).show();
        }
    }

    private static void updateUserInfo(final Activity activity, final View view, final String name, final String surname, final String gender, final String phone, final String documnetRef) {
        ShowDialog(activity);
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("surname", surname);
            user.put("gender", gender);
            user.put("phone", phone);


             CollectionReference collectionReference = db.collection(constants.users);
                    collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                accessKeys.setName(name);
                                accessKeys.setSurname(surname);
                                accessKeys.setGender(gender);
                                globalMethods.stopProgress = true;
                                ConfirmResolution(view, "Information successfully updated");
                                methods.globalMethods.loadFragments(R.id.main, new profile(), activity);
                            } else {
                                globalMethods.stopProgress = true;
                                ConfirmResolution(view, "unable to update Information, please retry!");

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

}

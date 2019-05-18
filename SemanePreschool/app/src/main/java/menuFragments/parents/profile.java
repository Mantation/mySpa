package menuFragments.parents;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.parents.userProfile.userAddress;
import menuFragments.parents.userProfile.userKids;
import menuFragments.parents.userProfile.userProfile;
import menuFragments.registration.stepsInfo;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class profile extends android.app.Fragment implements View.OnClickListener{
View myview;
CardView Kids;
CardView Personal;
CardView Address;
RecyclerView recyclerView;
TextView Name;
TextView Surname;
TextView Gender;
TextView Phone;
TextView AddressLine1;
TextView AddressLine2;
TextView AddressLine3;
TextView Town;
TextView Code;
TextView Province;



    public profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = myview.findViewById(R.id.recycler_kids);
        connectionHandler.external.myKids_.getAllDocuments(getActivity(),recyclerView);
        Kids = myview.findViewById(R.id.MyKids);
        Personal = myview.findViewById(R.id.Myprofile);
        Address = myview.findViewById(R.id.MyAddress);
        Name = myview.findViewById(R.id.name);
        Surname = myview.findViewById(R.id.surname);
        Gender = myview.findViewById(R.id.gender);
        Phone = myview.findViewById(R.id.phone);
        AddressLine1 = myview.findViewById(R.id.addressLine1);
        AddressLine2 = myview.findViewById(R.id.addressLine2);
        AddressLine3 = myview.findViewById(R.id.addressLine3);
        Town = myview.findViewById(R.id.city);
        Code = myview.findViewById(R.id.code);
        Province = myview.findViewById(R.id.Province);
        determineAdmin(Name,Surname,Gender,Phone,AddressLine1,AddressLine2,AddressLine3,Town,Code,Province);
        Kids.setOnClickListener(this);
        Personal.setOnClickListener(this);
        Address.setOnClickListener(this);
        return myview;
    }

    public static void determineAdmin(final TextView Name,final TextView Surname,final TextView Gender,final TextView Phone,final TextView Address1,final TextView Address2,final TextView Address3,
                                      final TextView Town,final TextView Code,final TextView Province){
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.get("name")!=null){
                                    Name.setText(document.get("name").toString());
                                }
                                if(document.get("surname")!=null){
                                    Surname.setText(document.get("surname").toString());
                                }
                                if(document.get("gender")!=null){
                                    Gender.setText(document.get("gender").toString());
                                }
                                if(document.get("phone")!=null){
                                    Phone.setText(document.get("phone").toString());
                                }
                                if(document.get("addressLine1")!=null){
                                    Address1.setText(document.get("addressLine1").toString());
                                }
                                if(document.get("addressLine2")!=null){
                                    if(!document.get("addressLine2").toString().equalsIgnoreCase("")) {
                                        Address2.setText(document.get("addressLine2").toString());
                                    }
                                }
                                if(document.get("addressLine3")!=null){
                                    if(!document.get("addressLine3").toString().equalsIgnoreCase("")) {
                                        Address3.setText(document.get("addressLine3").toString());
                                    }
                                }
                                if(document.get("city")!=null){
                                    Town.setText(document.get("city").toString());
                                }
                                if(document.get("code")!=null){
                                    Code.setText(document.get("code").toString());
                                }
                                if(document.get("province")!=null){
                                    Province.setText(document.get("province").toString());
                                }

                            }
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.MyKids){
            methods.globalMethods.loadFragments(R.id.main, new userKids(), getActivity());
        }else if (id == R.id.Myprofile) {
            methods.globalMethods.loadFragments(R.id.main, new userProfile(), getActivity());
        }else{
            methods.globalMethods.loadFragments(R.id.main, new userAddress(), getActivity());
        }

    }
}

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class userAddress extends android.app.Fragment implements View.OnClickListener{
    View myview;
    EditText Addr1;
    EditText Addr2;
    EditText Addr3;
    EditText Town;
    EditText Code;
    Spinner Province;
    CardView Submit;

    static String address1;
    static String address2;
    static String address3;
    static String town;
    static String code;
    static String province;
    static String DocumentRef;


    public static String getAddress1() {
        return address1;
    }

    public static void setAddress1(String address1) {
        userAddress.address1 = address1;
    }

    public static String getAddress2() {
        return address2;
    }

    public static void setAddress2(String address2) {
        userAddress.address2 = address2;
    }

    public static String getAddress3() {
        return address3;
    }

    public static void setAddress3(String address3) {
        userAddress.address3 = address3;
    }

    public static String getTown() {
        return town;
    }

    public static void setTown(String town) {
        userAddress.town = town;
    }

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        userAddress.code = code;
    }

    public static String getProvince() {
        return province;
    }

    public static void setProvince(String province) {
        userAddress.province = province;
    }

    public userAddress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_user_address, container, false);
        Addr1 = myview.findViewById(R.id.AddressLine1);
        Addr2 = myview.findViewById(R.id.AddressLine2);
        Addr3 = myview.findViewById(R.id.AddressLine3);
        Town = myview.findViewById(R.id.City);
        Code  = myview.findViewById(R.id.Code);
        Province  = myview.findViewById(R.id.Province);
        Submit = myview.findViewById(R.id.MySubmit);
        getAddressInfo(getActivity(),Addr1,Addr2,Addr3,Town,Code,Province);
        Submit.setOnClickListener(this);
        return myview;
    }

    public static void getAddressInfo(final Activity activity,final EditText Address1, final EditText Address2, final EditText Address3, final EditText Town, final EditText Code, final Spinner Province){
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
                                        if (document.get("addressLine1") != null) {
                                            Address1.setText(document.get("addressLine1").toString());
                                            setAddress1(document.get("addressLine1").toString());
                                        }
                                        if (document.get("addressLine2") != null) {
                                            Address2.setText(document.get("addressLine2").toString());
                                            setAddress2(document.get("addressLine2").toString());
                                        }
                                        if (document.get("addressLine3") != null) {
                                            Address3.setText(document.get("addressLine3").toString());
                                            setAddress3(document.get("addressLine3").toString());
                                        }
                                        if (document.get("city") != null) {
                                            Town.setText(document.get("city").toString());
                                            setTown(document.get("city").toString());
                                        }
                                        if (document.get("code") != null) {
                                            Code.setText(document.get("code").toString());
                                            setCode(document.get("code").toString());
                                        }
                                        if (document.get("province") != null) {
                                            //Province
                                            String[] province = activity.getResources().getStringArray(R.array.Province);
                                            String[] Province_ = new String[province.length - 1];
                                            for (int i = 0; i < province.length; i++) {
                                                if (i > 0) Province_[i - 1] = province[i];
                                            }
                                            ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, Province_);
                                            provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            Province.setAdapter(provinceAdapter);
                                            int myProvince = provinceAdapter.getPosition(document.get("province").toString());
                                            Province.setSelection(myProvince);
                                            setProvince(document.get("province").toString());
                                        }else{
                                            String[] province = activity.getResources().getStringArray(R.array.Province);
                                            ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_item, province);
                                            provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            Province.setAdapter(provinceAdapter);
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
        String newAddress1 = Addr1.getText().toString();
        String newAddress2 = Addr2.getText().toString();
        String newAddress3 = Addr3.getText().toString();
        String newTown = Town.getText().toString();
        String newCode = Code.getText().toString();
        String newProvince = Province.getSelectedItem().toString();

        if (newAddress1.equalsIgnoreCase(getAddress1()) || newAddress1.isEmpty()) {
            Addr1.requestFocus();
            Toast.makeText(getActivity(), "Address Line 1 field is required", Toast.LENGTH_SHORT).show();
        } else if (newTown.equalsIgnoreCase(getTown()) || newTown.isEmpty()) {
            Town.requestFocus();
            Toast.makeText(getActivity(), "Town field is required", Toast.LENGTH_SHORT).show();
        } else if (newCode.equalsIgnoreCase(getCode()) || newCode.isEmpty()) {
            Code.requestFocus();
            Toast.makeText(getActivity(), "Code field is required", Toast.LENGTH_SHORT).show();
        } else if (newProvince.equalsIgnoreCase(getProvince()) || newProvince.equalsIgnoreCase("-- select one --")) {
            Province.requestFocus();
            Toast.makeText(getActivity(), "Province field is required, please make a selection", Toast.LENGTH_SHORT).show();
        } else {

            if (newAddress1.equals(getAddress1()) && newTown.equals(getTown()) && newCode.equals(getCode()) && newProvince.equals(getProvince())) {
                Toast.makeText(getActivity(), "No information has been updated", Toast.LENGTH_SHORT).show();
            } else {
                updateUserAddress(getActivity(), view, newAddress1, newAddress2, newAddress3, newTown,newCode,newProvince, DocumentRef);
            }

        }
    }

    private static void updateUserAddress(final Activity activity, final View view, final String AddressLine1, final String AddressLine2, final String AddressLine3, final String Town,
                                       final String Code, final String Province,final String documnetRef) {
        ShowDialog(activity);
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("addressLine1", AddressLine1);
            user.put("addressLine2", AddressLine2);
            user.put("addressLine3", AddressLine3);
            user.put("city", Town);
            user.put("code", Code);
            user.put("province", Province);


            CollectionReference collectionReference = db.collection(constants.users);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
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

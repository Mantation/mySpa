package menuFragments.parents.kidsApplication;


import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.main;
import menuFragments.parents.home;
import menuFragments.previewGlobalMessage;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.InitializeFirstLetter;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.getCameraPermissions;
import static methods.globalMethods.getReadWritePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class applyFinal extends android.app.Fragment implements View.OnClickListener{
View myview;
EditText Name;
EditText Surname;
RadioButton Male;
RadioButton Female;
Spinner Relationship;
EditText Cellphone;
EditText Homephone;
EditText Workphone;
EditText Employer;
EditText Email;
RadioButton Yes;
RadioButton No;
EditText Addr1;
EditText Addr2;
EditText Addr3;
EditText Town;
EditText Code;
Spinner Province;
RadioButton Myself;
RadioButton Guardian;
RadioButton Other;
EditText OtherDescription;
ImageView Proof;
CardView Apply;
static TextView ProofConfirmation;
static String getImage;
static boolean ImageSet;
static Activity activity;

    public static String getGetImage() {
        return getImage;
    }

    public static void setGetImage(String getImage) {
        applyFinal.getImage = getImage;
        ProofConfirmation.setText(getImage);
        ProofConfirmation.setTextColor(activity.getResources().getColor(R.color.Purple));
    }


    public static boolean isImageSet() {
        return ImageSet;
    }

    public static void setImageSet(boolean imageSet) {
        ImageSet = imageSet;
    }



    public applyFinal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_apply_final, container, false);
        activity=getActivity();
        Name = myview.findViewById(R.id.childname);
        Surname = myview.findViewById(R.id.childsurname);
        Male = myview.findViewById(R.id.male);
        Female = myview.findViewById(R.id.female);
        Relationship = myview.findViewById(R.id.Relationship);
        Cellphone = myview.findViewById(R.id.cellno);
        Homephone = myview.findViewById(R.id.homeno);
        Workphone = myview.findViewById(R.id.workno);
        Employer = myview.findViewById(R.id.employer);
        Email = myview.findViewById(R.id.email);
        Yes  = myview.findViewById(R.id.yes);
        No  = myview.findViewById(R.id.no);
        Addr1 = myview.findViewById(R.id.AddressLine1);
        Addr2 = myview.findViewById(R.id.AddressLine2);
        Addr3 = myview.findViewById(R.id.AddressLine3);
        Town = myview.findViewById(R.id.City);
        Code  = myview.findViewById(R.id.Code);
        Province  = myview.findViewById(R.id.Province);
        Apply  = myview.findViewById(R.id.apply);
        Myself = myview.findViewById(R.id.me);
        Guardian = myview.findViewById(R.id.guardian);
        Other = myview.findViewById(R.id.other);
        OtherDescription = myview.findViewById(R.id.collect);
        Proof  = myview.findViewById(R.id.proof);
        ProofConfirmation  = myview.findViewById(R.id.proofOfPayment);
        Male.setChecked(true);
        Other.setChecked(true);
        No.setChecked(true);
        String[] relationship = getActivity().getResources().getStringArray(R.array.relationship);
        ArrayAdapter<String> relationshipAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, relationship);
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Relationship.setAdapter(relationshipAdapter);
        String[] province = getActivity().getResources().getStringArray(R.array.Province);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, province);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Province.setAdapter(provinceAdapter);
        Apply.setOnClickListener(this);
        Proof.setOnClickListener(this);
        track_PickUp();
        track_SameAddress();
        return myview;
    }

    private  void track_SameAddress() {
        Yes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Addr1.setEnabled(false);
                Addr2.setEnabled(false);
                Addr3.setEnabled(false);
                Town.setEnabled(false);
                Code.setEnabled(false);
                Province.setEnabled(false);
                return false;
            }
        });
        No.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Addr1.setEnabled(true);
                Addr2.setEnabled(true);
                Addr3.setEnabled(true);
                Town.setEnabled(true);
                Code.setEnabled(true);
                Province.setEnabled(true);
                return false;
            }
        });
    }

    private  void track_PickUp() {
        Myself.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                OtherDescription.setEnabled(false);
                return false;
            }
        });
        Guardian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                OtherDescription.setEnabled(false);
                return false;
            }
        });
        Other.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                OtherDescription.setEnabled(true);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.apply){
            String name = Name.getText().toString();
            String surname = Surname.getText().toString();
            String gender = "";
            String relationship = Relationship.getSelectedItem().toString();
            String cell = Cellphone.getText().toString();
            String homephone = Homephone.getText().toString();
            String workphone = Workphone.getText().toString();
            String employer = Employer.getText().toString();
            String email = Email.getText().toString();
            String SimillarAddress = "";
            String newAddress1 = Addr1.getText().toString();
            String newAddress2 = Addr2.getText().toString();
            String newAddress3 = Addr3.getText().toString();
            String newTown = Town.getText().toString();
            String newCode = Code.getText().toString();
            String newProvince = Province.getSelectedItem().toString();
            String collector = "";
            String SpecifyOther = OtherDescription.getText().toString();

            if(name.isEmpty() || name.length()<3) {
                Name.requestFocus();
                Toast.makeText(getActivity(), "enter full name", Toast.LENGTH_SHORT).show();
            }else if(surname.isEmpty() || surname.length()<3) {
                Surname.requestFocus();
                Toast.makeText(getActivity(), "enter full surname", Toast.LENGTH_SHORT).show();
            }else if(relationship.isEmpty() || relationship.equalsIgnoreCase("-- select one --")) {
                Province.requestFocus();
                Toast.makeText(getActivity(), "please select province", Toast.LENGTH_SHORT).show();
            }else if(cell.isEmpty() || cell.length()<10 || !cell.substring(0,1).equalsIgnoreCase("0")) {
                Cellphone.requestFocus();
                Toast.makeText(getActivity(), "invalid number format", Toast.LENGTH_SHORT).show();
            }else if(employer.isEmpty() || employer.length()<3) {
                    Employer.requestFocus();
                    Toast.makeText(getActivity(), "enter full employer name", Toast.LENGTH_SHORT).show();
            }else if(!globalMethods.ValidateEmail(email)) {
                Email.requestFocus();
                Toast.makeText(getActivity(), "invalid email format", Toast.LENGTH_SHORT).show();
            }else{
                if (Male.isChecked()){
                    gender = "Male";
                }else{
                    gender = "Female";
                }

                if (Myself.isChecked()){
                    collector = "Myself";
                }else if(Guardian.isChecked()){
                    collector = "Guardian";
                }else{
                    collector = SpecifyOther;
                }

                if((SpecifyOther.isEmpty() || SpecifyOther.length()<3) && !collector.equalsIgnoreCase("Myself") && !collector.equalsIgnoreCase("Guardian")){
                    OtherDescription.requestFocus();
                    Toast.makeText(getActivity(), "enter full description", Toast.LENGTH_SHORT).show();
                }else{
                    if((!homephone.isEmpty() && homephone.length()<10) || (!homephone.isEmpty() && homephone.length()==10 && !homephone.substring(0,1).equalsIgnoreCase("0"))) {
                        Homephone.requestFocus();
                        Toast.makeText(getActivity(), "invalid number format", Toast.LENGTH_SHORT).show();
                    }else if((!workphone.isEmpty() && workphone.length()<10) || (!workphone.isEmpty() && workphone.length()==10 && !workphone.substring(0,1).equalsIgnoreCase("0"))) {
                        Workphone.requestFocus();
                        Toast.makeText(getActivity(), "invalid number format", Toast.LENGTH_SHORT).show();
                    }else if(isImageSet()) {
                        if (Yes.isChecked()) {
                            SimillarAddress = "Yes";
                            submitApplication(getActivity(), view, name, surname, gender, relationship, cell, homephone, workphone, email, employer, collector, ProofConfirmation.getText().toString(), SimillarAddress, applicationInfo.getChild_Addr1(),
                                    applicationInfo.getChild_Addr2(), applicationInfo.getChild_Addr3(),applicationInfo.getChild_Town(), applicationInfo.getChild_Code(), applicationInfo.getChild_Province());
                        } else {
                            SimillarAddress = "No";
                             if (newAddress1.isEmpty()) {
                                Addr1.requestFocus();
                                Toast.makeText(getActivity(), "enter address here", Toast.LENGTH_SHORT).show();
                            } else if (newTown.isEmpty()) {
                                Town.requestFocus();
                                Toast.makeText(getActivity(), "enter address here", Toast.LENGTH_SHORT).show();
                            } else if (newCode.isEmpty() || newCode.length() < 4) {
                                Code.requestFocus();
                                Toast.makeText(getActivity(), "enter address code here", Toast.LENGTH_SHORT).show();
                            } else if (newProvince.isEmpty() || newProvince.equalsIgnoreCase("-- select one --")) {
                                Province.requestFocus();
                                Toast.makeText(getActivity(), "please select province", Toast.LENGTH_SHORT).show();
                            }else
                            submitApplication(getActivity(), view, name, surname, gender, relationship, cell, homephone, workphone, email, employer, collector, ProofConfirmation.getText().toString(), SimillarAddress, newAddress1,
                                    newAddress2, newAddress3, newTown, newCode, newProvince);
                        }
                    }else{
                        Toast.makeText(getActivity(), "Kindly add a proof of payment that accompanies this application", Toast.LENGTH_SHORT).show();
                    }

                }

            }
            }else{
            main.selectPic = true;
            main.permissionfor = constants.choosepdffile;
            getReadWritePermissions(getActivity());
            main.className = "applyFinal";

        }
    }

    private void submitApplication(final Activity activity, final View view, final String name, final String surname, final String gender, final String relationship, final String cellphone, final String homephone, final String workphone,
                                   final String email, final String employer, final String childCollector, final String paymentProof, final String sameAddress, final String addressLine1, final String addressLine2, final String addressLine3,
                                   final String town, final String code, final String province){
        ShowDialog(activity);
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("token", accessKeys.getMessagingToken());
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("document ref", defaultvalue);
            user.put("child_name", applicationInfo.getChild_Name());
            user.put("child_surname", applicationInfo.getChild_Surname());
            user.put("child_gender", applicationInfo.getChild_child_Gender());
            user.put("child_id", applicationInfo.getChild_Idnumber());
            user.put("child_grade", applicationInfo.getChild_Grade());
            user.put("child_Image", applicationInfo.getChild_Image());
            user.put("child_addresss1", applicationInfo.getChild_Addr1());
            user.put("child_addresss2", applicationInfo.getChild_Addr2());
            user.put("child_addresss3", applicationInfo.getChild_Addr3());
            user.put("child_addresssTown", applicationInfo.getChild_Town());
            user.put("child_addresssCode", applicationInfo.getChild_Code());
            user.put("child_addresssProvince", applicationInfo.getChild_Province());
            user.put("child_HasAllergies", applicationInfo.getChild_isAllergic());
            user.put("Child_Allergies", applicationInfo.getChild_AllergyDescription());
            user.put("child_MedicalConditions", applicationInfo.getChild_MedicalCondition());
            user.put("status", "pending");
            user.put("Guardian2_name", name);
            user.put("Guardian2_surname", surname);
            user.put("Guardian2_gender", gender);
            user.put("Guardian2_relationship", relationship);
            user.put("Guardian2_Cellphone", cellphone);
            user.put("Guardian2_Workphone", workphone);
            user.put("Guardian2_Homephone", homephone);
            user.put("Guardian2_email", email);
            user.put("Guardian2_employer", employer);
            user.put("child_collectedBy", childCollector);
            user.put("proof Of Payment", paymentProof);
            user.put("Guardian2_AddressSameAsChild", sameAddress);
            user.put("Guardian2_addresss1", addressLine1);
            user.put("Guardian2_addresss2", addressLine2);
            user.put("Guardian2_addresss3", addressLine3);
            user.put("Guardian2_addresssTown", town);
            user.put("Guardian2_addresssCode", code);
            user.put("Guardian2_addresssProvince", province);


            // Add a new document with a generated ID
            db.collection(constants.apply)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.apply);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        saveChildImage(activity,view,applicationInfo.getChild_Image(),paymentProof,document);
                                    }else{
                                        Toast.makeText(activity, "unable to send application, please retry", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(activity, "Error sending appplication to server", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception exception){
            globalMethods.stopProgress = true;
            exception.getMessage();
            exception.printStackTrace();
        }


    }

    //send child image to storage
    public static String saveChildImage(final Activity activity,final View view, final String ChildPicture,final String ProofofPayMent, final String documentRef){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(constants.apply)
                .child(accessKeys.getDefaultUserId()).child("applicants_images").child(accessKeys.getDefaultUserId());
        UploadTask uploadTask = ref.putFile(Uri.parse(ChildPicture));
        final String url = String.valueOf(ref.getDownloadUrl());

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveProofOfPayment(activity,view,downloadUri.toString(),ProofofPayMent,documentRef);
                } else {
                    // Handle failures
                    // ...
                    globalMethods.stopProgress = true;
                    Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return url;
    }

    //send proof of payment to storage

    public static String saveProofOfPayment(final Activity activity,final View view, final String ChildPicture,final String ProofofPayMent, final String documentRef){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(constants.apply)
                .child(accessKeys.getDefaultUserId()).child("applicants_proofOfPayments").child(accessKeys.getDefaultUserId());
        UploadTask uploadTask = ref.putFile(Uri.parse(ProofofPayMent));
        final String url = String.valueOf(ref.getDownloadUrl());

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    updatePictureDocument(activity,view,ChildPicture,downloadUri.toString(),documentRef);
                } else {
                    // Handle failures
                    // ...
                    globalMethods.stopProgress = true;
                    Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return url;
    }

    //update PicturePath
    public static void updatePictureDocument(final Activity activity,final View view, final String ChildPicture, final String ProofOfPayment, final String documentRef){
        try {
            Map<String, Object> user = new HashMap<>();
            user.put("child_Image", ChildPicture);
            user.put("proof Of Payment", ProofOfPayment);
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(constants.apply);
            collectionReference.document(documentRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + accessKeys.getDefaultUserId());
                        sendApplicationMessage(activity,view);

                    }else {
                        // Handle failures
                        // ...
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "Error adding sending application to server", Toast.LENGTH_SHORT).show();
                    }
                }



                // Create a new user with a first and last name
                //Map<String, Object> user = new HashMap<>();
                //user.put("userPhone", Phone);
                //user.put("userPicture", Picture);

                // Add a new document with a generated ID
                //db.collection(constants.users).document(userId)
                //        .update("userid", userId)
                //        .addOnCompleteListener(new OnCompleteListener<Void>() {
                //.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                //        public void onComplete(@NonNull Task<Void> task) {
                //            if (task.isSuccessful()) {
                //Toast.makeText(this, "Document created/updated", Toast.LENGTH_SHORT).show();
                //               Log.d(TAG, "DocumentSnapshot added with ID: " + userId);
                //               setPhoneNumber(activity, getDefaultUserEmail(), Phone, Picture);
                //           }
                //       }
                        /*@Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //final String userid = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.users);
                            collectionReference.document(userId).update("userid", userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(this, "Document created/updated", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + userId);
                                        setPhoneNumber(activity, getDefaultUserEmail(), Phone, Picture);
                                    }
                                }
                            });
                        }*/
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    globalMethods.stopProgress = true;
                    Toast.makeText(activity, "Error adding sending application to server", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
            globalMethods.stopProgress = true;
            Toast.makeText(activity, "Error adding sending application to server", Toast.LENGTH_SHORT).show();
        }

    }

    public static void sendApplicationMessage(final Activity activity,final View view){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("subject", "Application for " + applicationInfo.getChild_Grade());
            user.put("message", "Dear Parent,\n\nKindly note that we have received your application and is under review, your application details are as follows:\n\nChild Name: "+applicationInfo.getChild_Name()+
                    "\nChild Surname: " + applicationInfo.getChild_Surname() +"\nChild Identity no: " + applicationInfo.getChild_Idnumber()+"\nChild Sex: " + applicationInfo.getChild_child_Gender()+"\nGrade Requested : "+ applicationInfo.getChild_Grade()+
                    "\n\nKindly note that should your application be successful or otherwise, we will notify you through the App.\n\nKind Regards,\nManagement");
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
                                        globalMethods.stopProgress = true;
                                        ConfirmResolution(view,"application submitted successfully");
                                        methods.globalMethods.loadFragments(R.id.main, new home(),activity);
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

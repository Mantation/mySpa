package menuFragments;


import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.survey.MainActivity;
import io.eyec.bombo.survey.R;
import methods.globalMethods;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.getCameraPermissions;
import static methods.globalMethods.getReadWritePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class start extends android.app.Fragment implements View.OnClickListener{
    View myview;
    TextView document;
    static ImageView Image;
    EditText description;
    CardView Submit;
    TextView Close;
    Spinner Type;
    TextView Titles;
    static Activity activity;
    private static String documentId;
    static String selectedImage;

    public static String getDocumentId() {
        return documentId;
    }

    public static void setDocumentId(String documentId) {
        start.documentId = documentId;
    }

    //set selected image on imageview
    public static void setMyImage(String myImage) {
        Glide.with(activity).load(myImage).into(Image);
        selectedImage = myImage;
    }

    public start() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_start, container, false);
        activity = getActivity();
        document = myview.findViewById(R.id.workid);
        Close = myview.findViewById(R.id.close);
        Image = myview.findViewById(R.id.image);
        description = myview.findViewById(R.id.body);
        Submit = myview.findViewById(R.id.MySubmit);
        Type = myview.findViewById(R.id.type);
        Titles = myview.findViewById(R.id.titles);
        String[] type = getResources().getStringArray(R.array.type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, type);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Type.setAdapter(typeAdapter);
        if(getDocumentId()!=null){
            document.setText(getDocumentId());
            Close.setVisibility(View.VISIBLE);
            Titles.setVisibility(View.GONE);
            Type.setVisibility(View.GONE);
        }else{
            document.setVisibility(View.GONE);
            Close.setVisibility(View.GONE);
        }
        Image.setOnClickListener(this);
        Submit.setOnClickListener(this);
        return myview;
    }

    public static void InitiateCamera(final Activity Myactivity){
        final Dialog dialog = new Dialog(Myactivity);
        dialog.setContentView(R.layout.camera_custom_layout);
        dialog.setCancelable(true);
        TextView dialogCamera = (TextView) dialog.findViewById(R.id.camera);
        TextView dialogfromPhone = (TextView) dialog.findViewById(R.id.fromphone);
        TextView dialogCancel = (TextView) dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        dialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.capturePic = true;
                MainActivity.permissionfor = constants.camera;
                MainActivity.activity = "start";
                getCameraPermissions(Myactivity);
            }
        });
        dialogfromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.selectPic = true;
                MainActivity.permissionfor = constants.choosefile;
                MainActivity.activity = "start";
                getReadWritePermissions(Myactivity);
                //getActivity().startActivityForResult(getFileChooserIntent(), constants.CHOOSE_FILE_REQUESTCODE);

            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image){
            InitiateCamera(getActivity());
        }else{
            String Description = description.getText().toString();
            if(selectedImage == null){
                Toast.makeText(getActivity(), "picture not attached!", Toast.LENGTH_LONG).show();
                Image.requestFocus();
            }else if(Description.isEmpty() || Description.length()< 20){
                Toast.makeText(getActivity(), "please provide a detailed description!", Toast.LENGTH_LONG).show();
                description.requestFocus();
            }else{
                if(getDocumentId()!=null){
                    closeMaintenanceIssue(getActivity(),Description,getDocumentId(),selectedImage);
                }else{
                    String JobType = Type.getSelectedItem().toString();
                     if(Type.getSelectedItem().toString().isEmpty()||Type.getSelectedItem().toString().equalsIgnoreCase("-- select one --")) {
                        Type.requestFocus();
                        Toast.makeText(getActivity(), "Select Job type", Toast.LENGTH_SHORT).show();
                    }else{
                         initiateMaintenance(getActivity(),Description,selectedImage,JobType);
                     }

                }
            }

        }
    }

    //update PicturePath
    public static void closeMaintenanceIssue(final Activity activity,final String description,final String documentRef,final String Image){
        ShowDialog(activity);
        try {
            Map<String, Object> user = new HashMap<>();
            user.put("status", "closed");
            user.put("CompletionDescription", description);
            user.put("CompletionTime", Time());
            user.put("CompletionDate", ToDate());
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(constants.maintenance);
            collectionReference.document(documentRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentRef);
                        saveDocument(activity, "impala", Image,  documentRef);
                        //globalMethods.stopProgress = true;
                        //methods.globalMethods.loadFragmentsWithTag(R.id.main, new maintenance(),activity);
                    }else {
                        // Handle failures
                        // ...
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
            globalMethods.stopProgress = true;
            Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
        }

    }




    //set Picture details
    public static void initiateMaintenance(final Activity activity,final String description, final String Image, final String JobType){
        ShowDialog(activity);
        String defaultvalue = "n/a";
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final Map<String, Object> user = new HashMap<>();
            user.put("InitialPicture", Image);
            user.put("InitialDescription", description);
            user.put("InitialTime", Time());
            user.put("InitialDate", ToDate());
            user.put("document ref", defaultvalue);
            user.put("status", "open");
            user.put("jobType", JobType);
            // Add a new document with a generated ID
            db.collection(constants.maintenance)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.maintenance);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + document);
                                        saveDocument(activity, JobType, Image,  document);
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
                            Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception exception){
            globalMethods.stopProgress = true;
            exception.getMessage();
            exception.printStackTrace();
        }
    }

    //set picture to storage
    public static String saveDocument(final Activity activity,final String project,final String picture, final String documentRef){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(constants.maintenance)
                .child(project).child("images").child(documentRef);
        UploadTask uploadTask = ref.putFile(Uri.parse(picture));
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
                    updatePictureDocument(activity,downloadUri.toString(),documentRef);
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
    public static void updatePictureDocument(final Activity activity, final String picture, final String documentRef){
        try {
            Map<String, Object> user = new HashMap<>();
            if(getDocumentId()!=null){
                user.put("CompletionPicture", picture);
            }else{
                user.put("InitialPicture", picture);
            }

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(constants.maintenance);
            collectionReference.document(documentRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentRef);
                        globalMethods.stopProgress = true;
                        setDocumentId(null);
                        selectedImage = null;
                        methods.globalMethods.loadFragmentsWithTag(R.id.main, new maintenance(),activity);
                    }else {
                        // Handle failures
                        // ...
                        globalMethods.stopProgress = true;
                        Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
            globalMethods.stopProgress = true;
            Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
        }

    }



}

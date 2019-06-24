package menuFragments.Administration.Application;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authentication.auth;
import authentication.stayLoggedIn;
import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.main;
import io.eyec.bombo.semanepreschool.sendMessages;
import menuFragments.Administration.admin;
import menuFragments.parents.kidsApplication.applicationInfo;
import menuFragments.parents.profile;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.runSuccessfulToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class previewApplication extends android.app.Fragment implements  View.OnClickListener{
View myview;
static TextView Submitter;
TextView Date;
ImageView Child;
TextView Profile;
CardView Download;
CardView Accept;
CardView Decline;
EditText Reason;

static String UserId;
static String profile;
static String proof;
static String image;
static String date;
static String Status;
static String documentRefference;
static String Name;
static String MessagingToken;
static  Activity activity;

private static void submittedBy(final String value){
    Submitter.setText(value);
}


    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static String getProfile() {
        return profile;
    }

    public static void setProfile(String profile) {
        previewApplication.profile = profile;
    }

    public static String getProof() {
        return proof;
    }

    public static void setProof(String proof) {
        previewApplication.proof = proof;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        previewApplication.image = image;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        date = date;
    }

    public static String getStatus() {
        return Status;
    }

    public static void setStatus(String status) {
        Status = status;
    }

    public static String getDocumentRefference() {
        return documentRefference;
    }

    public static void setDocumentRefference(String documentRefference) {
        previewApplication.documentRefference = documentRefference;
    }

    public static String getMessagingToken() {
        return MessagingToken;
    }

    public static void setMessagingToken(String messagingToken) {
        MessagingToken = messagingToken;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public previewApplication() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_preview_application, container, false);
        activity =getActivity();
        Submitter = myview.findViewById(R.id.submitedBy);
        getFirestoreUsers(getActivity(),getUserId());
        Date = myview.findViewById(R.id.date);
        Child = myview.findViewById(R.id.child_image);
        Profile = myview.findViewById(R.id.profile);
        Download = myview.findViewById(R.id.MyProofOfPayment);
        Accept = myview.findViewById(R.id.MySubmit);
        Decline = myview.findViewById(R.id.MyDecline);
        Reason = myview.findViewById(R.id.reason);
        Glide.with(getActivity()).load(getImage()).into(Child);
        Profile.setText(getProfile());
        Date.setText(getDate());
        if (!getStatus().equalsIgnoreCase("pending")) {
            Accept.setVisibility(View.GONE);
            Decline.setVisibility(View.GONE);
            Reason.setVisibility(View.GONE);
        }else{
            Accept.setVisibility(View.VISIBLE);
            Decline.setVisibility(View.VISIBLE);
            Reason.setVisibility(View.VISIBLE);
        }
        Download.setOnClickListener(this);
        Accept.setOnClickListener(this);
        Decline.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View view) {
    int id = view.getId();
    if(id==R.id.MyProofOfPayment){
        //final DownloadProof downloadProof = new DownloadProof();
        //downloadProof.execute();
        getPermissions(activity);
    }else if (id==R.id.MySubmit){
        //accept
        String reason = Reason.getText().toString();
        if (reason.isEmpty())
            reason ="";
        submitApplication(getActivity(),view,"accepted",reason,documentRefference);

    }else{
        //decline
        String reason = Reason.getText().toString();
        if(reason.isEmpty() || reason.length()< 10){
            Reason.requestFocus();
            Toast.makeText(getActivity(), "please provide a descriptive reason", Toast.LENGTH_SHORT).show();
        }else {
            submitApplication(getActivity(), view, "rejected",reason, documentRefference);
        }
    }

    }

    //get Resd/write permissions from user
    private void getPermissions(Activity activity) {
        //permissions read/write external storage
        main.permissionfor = constants.AppName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void submitApplication(final Activity activity, final View view, final String status,final String reason, final String docRef){
        ShowDialog(activity);
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("status", status);

            CollectionReference collectionReference = db.collection(constants.apply);
            collectionReference.document(docRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        globalMethods.stopProgress = true;
                        ConfirmResolution(view, "Application updated successfully");
                        methods.globalMethods.loadFragments(R.id.main, new admin(), activity);
                        sendApplicationMessage(getUserId(),status,reason);
                    } else {
                        globalMethods.stopProgress = true;
                        ConfirmResolution(view, "unable to update application, please retry!");

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


    public static void getUserDetails( final String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String UserId = document.get("userid").toString();
                                if (UserId.equals(userId)) {
                                    accessKeys.setName(String.valueOf(document.get("name")));
                                    accessKeys.setSurname(String.valueOf(document.get("surname")));
                                    String NameSurname = globalMethods.InitializeFirstLetter(String.valueOf(document.get("name")))+" "+ globalMethods.InitializeFirstLetter(String.valueOf(document.get("name"))) + "\n"+
                                            document.get("userid").toString();
                                    submittedBy(NameSurname);
                                    globalMethods.stopProgress = true;
                                    break;

                                }
                            }
                            globalMethods.stopProgress = true;
                        }
                    }
                });


    }

    //get all images from firestore
    private static  void getFirestoreUsers(final Activity activity, final String userId){
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
            }});
        final FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        dbs.collection(constants.users)
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
                            getUserDetails(userId);
                        }
                    }
                });
    }

    static String Message="";
    public static void sendApplicationMessage(final String userId, final String status, final String reason){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", userId);
            user.put("subject", "New Pupil Application");
            String Additional = "";
            if (reason!="")
                Additional = "\n\nAdditional Information: \n"+reason;
            if (status.equalsIgnoreCase("rejected")) {
                user.put("message", "Dear Parent,\n\nWe regret to inform you that your application was unsuccessful. Thank you for choosing us as the preferred school of choice. "+
                                Additional+
                        "\n\nKind Regards,\nManagement");
                Message = "Dear Parent,\n\nWe regret to inform you that your application was unsuccessful. Thank you for choosing us as the preferred school of choice. "+
                        Additional+"\n\nKind Regards,\nManagement";
            }else{
                user.put("message", "Dear Parent,\n\nKindly note that you that your application was successful. We are looking forward to seeing your young one with us. Thank you for choosing us as the school of your choice. "+
                        Additional+
                        "\n\nKind Regards,\nManagement");
                Message = "Dear Parent,\n\nKindly note that you that your application was successful. We are looking forward to seeing your young one with us. Thank you for choosing us as the school of your choice. "+
                        Additional+"\n\nKind Regards,\nManagement";
            }

            user.put("status", status);
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
                                        new sendMessages().execute("New Pupil Application",Message,getMessagingToken());
                                        Message = null;
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

    public void startViewFileDirectory(final String absolutePath,final  Activity activity) {
        Intent intent = new Intent("android.intent.action.VIEW");
        //intent.setData(dataUri); // uri of directory from FileProvider, DocumentProvider or uri with file scheme, can be empty.
        intent.putExtra("org.openintents.extra.ABSOLUTE_PATH", absolutePath); // String
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    @TargetApi(26)
//Download file from url
    public static class DownloadProof extends AsyncTask<Void, Void, Void> {
        URL url;
        File Internalfile;
        File Externalfile;
        {
            try {
                ShowDialog(previewApplication.activity);
                url = new URL(previewApplication.getProof());
                Internalfile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Externalfile = Environment.getExternalStorageDirectory();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @TargetApi(26)
        @Override
        protected Void doInBackground(Void... params) {
            ShowDialog(previewApplication.activity);
            File Proof;
            try {

                if (Externalfile.exists()) {
                    File folder = new File(Externalfile + "/" + constants.AppName + "/" + constants.proof +"/");
                    if (!folder.exists()) {
                        folder.mkdirs();
                            /*CopyOption[] options = new CopyOption[]{
                                    StandardCopyOption.COPY_ATTRIBUTES,
                                    StandardCopyOption.COPY_ATTRIBUTES
                            };*/
                    }
                    File papersFile;
                    papersFile = new File(folder + "/" + previewApplication.getName()+"-ProofofPayment.pdf");
                    FileUtils.copyURLToFile(url, papersFile);
                    Proof = papersFile;
                    Runtime.getRuntime().exec("explorer.exe /select," + folder);
                    globalMethods.stopProgress = true;
                    runSuccessfulToast(previewApplication.activity, previewApplication.getName());
                    //methods.globalMethods.loadFragments(R.id.main, new previewStats(), previewReceipt.Activity);
                    //Toast.makeText(context, paperName + " file Successfully downloaded!", Toast.LENGTH_SHORT).show();
                } else {
                    File folder = new File(Internalfile + "/" + constants.AppName + "/" + constants.proof + "/");
                    if (!folder.exists()) {
                        folder.mkdirs();
                            /*CopyOption[] options = new CopyOption[]{
                                    StandardCopyOption.COPY_ATTRIBUTES,
                                    StandardCopyOption.COPY_ATTRIBUTES
                            };*/
                    }
                    File papersFile;
                    papersFile = new File(folder + "/" + previewApplication.getName()+"-ProofofPayment.pdf");
                    Proof = papersFile;
                    FileUtils.copyURLToFile(url, Proof);
                    Runtime.getRuntime().exec("explorer.exe /select," + folder);
                    globalMethods.stopProgress = true;
                    runSuccessfulToast(previewApplication.activity, previewApplication.getName());
                    //methods.globalMethods.loadFragments(R.id.main, new previewStats(), previewReceipt.Activity);
                    //Toast.makeText(context, paperName + " file Successfully downloaded!", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Loginfo("unable to generate Proof of payment, " + e.getMessage());
                globalMethods.stopProgress = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }



}



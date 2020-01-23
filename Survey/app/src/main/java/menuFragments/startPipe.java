package menuFragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.survey.MainActivity;
import io.eyec.bombo.survey.R;
import methods.globalMethods;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Thread.sleep;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.getCameraPermissions;
import static methods.globalMethods.getReadWritePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class startPipe extends android.app.Fragment implements View.OnClickListener{
    View myview;
    TextView document;
    static ImageView Image;
    TextView descriptionLabel;
    EditText description;
    CardView Submit;
    TextView Close;
    Spinner Type;
    TextView Titles;
    TextView Hinter;
    static RelativeLayout relativeLayout;
    TextView hintPress;
    static Activity activity;
    private static String documentId;
    static String selectedImage;
    private static String jobType;
    private static int Phase;
    public static LocationManager locationManager;
    public static LocationListener locationListener;
    private boolean isLocationGrabbed;

    //job Type
    public static String getJobType() {
        return jobType;
    }

    public static void setJobType(String jobType) {
        startPipe.jobType = jobType;
    }

    //Phase
    public static int getPhase() {
        return Phase;
    }

    public static void setPhase(int phase) {
        Phase = phase;
    }

    //Document Id
    public static String getDocumentId() {
        return documentId;
    }

    public static void setDocumentId(String documentId) {
        startPipe.documentId = documentId;
    }

    //set selected image on imageview
    public static void setMyImage(String myImage) {
        Glide.with(activity).load(myImage).into(Image);
        selectedImage = myImage;
    }


    public startPipe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_start_pipe, container, false);
        activity = getActivity();
        document = myview.findViewById(R.id.workid);
        Close = myview.findViewById(R.id.close);
        Image = myview.findViewById(R.id.image);
        description = myview.findViewById(R.id.body);
        Submit = myview.findViewById(R.id.MySubmit);
        Type = myview.findViewById(R.id.type);
        Titles = myview.findViewById(R.id.titles);
        descriptionLabel = myview.findViewById(R.id.descLabel);
        Hinter = myview.findViewById(R.id.hinter);
        relativeLayout = myview.findViewById(R.id.main);
        relativeLayout.setVisibility(View.GONE);
        hintPress = myview.findViewById(R.id.press);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String[] type = getResources().getStringArray(R.array.pipes);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, type);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Type.setAdapter(typeAdapter);
        if(getDocumentId()!=null){
            document.setText(getDocumentId());
            Close.setVisibility(View.VISIBLE);
            if(getJobType().equalsIgnoreCase("Pipes - above ground") && getPhase()==0){
                descriptionLabel.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                Hinter.setText("\nAdd an image of the fixed pipe");
                globalMethods.reshowView(relativeLayout,getActivity());
                //hintPress.setVisibility(View.INVISIBLE);
            }else if(getJobType().equalsIgnoreCase("Pipes - below ground") && getPhase()==2){
                descriptionLabel.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                Hinter.setText("\nAdd an image of the re-covered ground");
                globalMethods.reshowView(relativeLayout,getActivity());
                //hintPress.setVisibility(View.INVISIBLE);
            }else{
                if(getPhase()==0){
                    Hinter.setText("\nAdd an image of the broken pipe");
                    globalMethods.reshowView(relativeLayout,getActivity());
                    //hintPress.setVisibility(View.INVISIBLE);
                }else{
                    Hinter.setText("\nAdd an image of the fixed pipe");
                    globalMethods.reshowView(relativeLayout,getActivity());
                    //hintPress.setVisibility(View.INVISIBLE);

                }
                descriptionLabel.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }
            //Hide all other displays
            Titles.setVisibility(View.GONE);
            Type.setVisibility(View.GONE);
        }else{
            Hinter.setText("\nAdd an image of where the incident is.");
            globalMethods.reshowView(relativeLayout,getActivity());
            hintPress.setVisibility(View.VISIBLE);
            document.setVisibility(View.GONE);
            Close.setVisibility(View.GONE);
        }
        MonitorNotifier(hintPress,Hinter,relativeLayout,getActivity());
        Image.setOnClickListener(this);
        Submit.setOnClickListener(this);
        isLocationGrabbed = false;
        locationListener  = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                if(!isLocationGrabbed) {
                    System.out.println(location.getLatitude()+ ": "+location.getLongitude());
                    longitute = String.valueOf(location.getLongitude());
                    latitude = String.valueOf(location.getLatitude());
                    //quizInfo.answers.add(String.valueOf(location.getLongitude()));
                    //quizInfo.answers.add(String.valueOf(location.getLatitude()));
                    isLocationGrabbed = true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        isLocationGrabbed = false;
        //initiateLocation();
        return myview;
    }

    @Override
    public void onDestroy() {
            handler.removeCallbacks(myRunnable);
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacksAndMessages(myRunnable);
            handler.removeMessages(0);
        super.onDestroy();
    }
    static String longitute;
    static String latitude;
    static boolean GPS;
    public void initiateLocation(){
        locationListener  = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                if(!isLocationGrabbed) {
                    System.out.println(location.getLatitude()+ ": "+location.getLongitude());
                    longitute = String.valueOf(location.getLongitude());
                    latitude = String.valueOf(location.getLatitude());
                    //quizInfo.answers.add(String.valueOf(location.getLongitude()));
                    //quizInfo.answers.add(String.valueOf(location.getLatitude()));
                    isLocationGrabbed = true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        isLocationGrabbed = false;
    }

    private void getLocationPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.permissionfor = constants.locationPipes;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }

    public static final Handler handler = new Handler();
    final static int delay = 5000; //milliseconds
    public static Runnable myRunnable;
    public static void MonitorNotifier(final TextView hinter, final TextView Press, final RelativeLayout relativeLayout, final Activity activity){
        handler.postDelayed(myRunnable = new Runnable(){
            public void run(){
                if(relativeLayout.getVisibility() == View.VISIBLE){
                    hinter.setText("\nAdd an image of the fixed pipe");
                    //globalMethods.hideView(relativeLayout,activity);
                    relativeLayout.setVisibility(View.GONE);
                    Press.setVisibility(View.VISIBLE);
                    System.out.println("Adapter not yet Set");
                    //myRunnable = this;
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
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
                MainActivity.activity = "pipes";
                getCameraPermissions(Myactivity);
            }
        });
        dialogfromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.selectPic = true;
                MainActivity.permissionfor = constants.choosefile;
                MainActivity.activity = "pipes";
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
            getLocationPermission();
            InitiateCamera(getActivity());
        }else{
            String Description = description.getText().toString();
            if(selectedImage == null){
                Toast.makeText(getActivity(), "picture not attached!", Toast.LENGTH_LONG).show();
                Image.requestFocus();
            }else if((description.getVisibility() == View.VISIBLE && Description.isEmpty() || (description.getVisibility() == View.VISIBLE && Description.length()< 20))){
                    Toast.makeText(getActivity(), "please provide a detailed description!", Toast.LENGTH_LONG).show();
                    description.requestFocus();
            }else{
                if(getDocumentId()!=null){
                    closePipingIssue(getActivity(),Description,getDocumentId(),selectedImage);
                }else{
                    String JobType = Type.getSelectedItem().toString();
                    if(Type.getSelectedItem().toString().isEmpty()||Type.getSelectedItem().toString().equalsIgnoreCase("-- select one --")) {
                        Type.requestFocus();
                        Toast.makeText(getActivity(), "Select Job type", Toast.LENGTH_SHORT).show();
                    }else{
                        initiatePiping(getActivity(),Description,selectedImage,JobType);
                    }

                }
            }

        }

    }

    //update PicturePath
    public static void closePipingIssue(final Activity activity,final String description,final String documentRef,final String Image){
        ShowDialog(activity);
        try {
            int Phase = getPhase()+1;
            Map<String, Object> user = new HashMap<>();
            if(!getJobType().equalsIgnoreCase("Pipes - above ground")){
                user.put("GPS Longitute phase"+Phase, longitute);
                user.put("GPS Latitute phase"+Phase, latitude);
                user.put("time phase"+Phase, Time());
                user.put("date phase"+Phase, ToDate());
            }
            if(getJobType().equalsIgnoreCase("Pipes - above ground") && getPhase()==0){
                user.put("status", "closed");
                user.put("CompletionDescription", description);
                user.put("CompletionTime", Time());
                user.put("CompletionDate", ToDate());
                user.put("GPS Longitute close", longitute);
                user.put("GPS Latitute close", latitude);
            }else if(getJobType().equalsIgnoreCase("Pipes - below ground") && getPhase()==2) {
                user.put("status", "closed");
                user.put("CompletionDescription", description);
                user.put("CompletionTime", Time());
                user.put("CompletionDate", ToDate());
                user.put("GPS Longitute close", longitute);
                user.put("GPS Latitute close", latitude);
            }else{
                user.put("status", "open");
            }
            user.put("phase", Phase);
            final int phase = Phase;
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(constants.pipes);
            collectionReference.document(documentRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentRef);
                        saveDocument(activity, getJobType(),Image,documentRef,"image_phase_"+phase);
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
    public static void initiatePiping(final Activity activity,final String description, final String Image, final String JobType){
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
            user.put("phase", 0);
            user.put("GPS Longitute", longitute);
            user.put("GPS Latitute", latitude);
            // Add a new document with a generated ID
            db.collection(constants.pipes)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.pipes);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + document);
                                        saveDocument(activity, JobType,Image,document,"image_phase_0");
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
    public static String saveDocument(final Activity activity,final String project,final String picture, final String documentRef,final String picName){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(constants.pipes)
                .child(project).child("images").child(documentRef).child(picName);
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
                    updatePictureDocument(activity,downloadUri.toString(),documentRef,picName);
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
    public static void updatePictureDocument(final Activity activity, final String picture, final String documentRef,final String pictureName){
        try {
            Map<String, Object> user = new HashMap<>();
            if(getDocumentId()!=null){
                user.put(pictureName, picture);
            }else{
                user.put(pictureName, picture);
            }

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection(constants.pipes);
            collectionReference.document(documentRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentRef);
                        globalMethods.stopProgress = true;
                        setDocumentId(null);
                        selectedImage = null;
                        methods.globalMethods.loadFragmentsWithTag(R.id.main, new piping(),activity);
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

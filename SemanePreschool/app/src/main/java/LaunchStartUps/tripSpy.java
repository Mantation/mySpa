package LaunchStartUps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constants.constants;
import menuFragments.parents.home;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

public class tripSpy {

    public static List profiles = new ArrayList<String>();
    public static List grades = new ArrayList<String>();
    public static final Handler handler = new Handler();
    final static int delay = 500; //milliseconds

    //Notifications

    public static void MonitorMeetings(final Activity activity){
        handler.postDelayed(new Runnable(){
            public void run(){
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    //get child ID number
    public static void getSelfProfiles(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                int total = document.getData().size();
                                for (int i = 0; i < total; i++) {
                                    if(document.get("profile_"+i)!=null){
                                        profiles.add(document.get("profile_"+i).toString());
                                    }

                                }
                            }
                            if(profiles.size()>0){
                                getAllGrades(profiles);
                            }
                        }

                    }
                });
    }

    //get grades for all IDs
    public static void getSelfGrades(final List profiles){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.children)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("grade")!=null && document.get("id")!=null){
                                    for (int i = 0; i < profiles.size(); i++) {
                                        if(profiles.get(i).toString().equalsIgnoreCase(document.get("id").toString())){
                                            if(grades.size()==0){
                                                grades.add("grade "+document.get("grade").toString());
                                            }else{
                                                for (int j = 0; j < grades.size(); j++) {
                                                    if (grades.get(j).toString().equalsIgnoreCase(document.get("grade").toString())){
                                                        break;
                                                    }else{
                                                        if((j+1)== grades.size()){
                                                            grades.add("grade "+document.get("grade").toString());
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                            getTrips(grades);
                        }

                    }
                });
    }



    //get all grades from firestore
    private static  void getAllGrades(final List profiles){
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
                            getSelfGrades(profiles);
                        }
                    }
                });
    }

    public static List<String> topic = new ArrayList<String>();
    public static List<String> attendants = new ArrayList<String>();
    public static List<String> venue = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> Document = new ArrayList<String>();
    public static List<String> Parents = new ArrayList<String>();
    public static List<String> price = new ArrayList<String>();
    //get the closest meeting
    public static void getClosestTrip(final List Grades){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.trip)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean found= false;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("attendants") != null && document.get("venue") != null && document.get("topic") != null && document.get("time") != null && document.get("date") != null && document.get("price") != null) {
                                    int total = document.getData().size();
                                    for (int i = 0; i < total; i++) {
                                        if(document.get("parent_" + i)!=null){
                                            if(document.get("parent_" + i).toString().equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                                date.add(document.get("date").toString());
                                                time.add(document.get("time").toString());
                                                venue.add(document.get("venue").toString());
                                                topic.add(document.get("topic").toString());
                                                attendants.add(document.get("attendants").toString());
                                                Document.add(document.get("document ref").toString());
                                                price.add(document.get("price").toString());
                                                Parents.add(document.get("parent_" + i).toString());
                                                found=true;
                                                break;
                                            }else{
                                                date.add(document.get("date").toString());
                                                time.add(document.get("time").toString());
                                                venue.add(document.get("venue").toString());
                                                topic.add(document.get("topic").toString());
                                                attendants.add(document.get("attendants").toString());
                                                Document.add(document.get("document ref").toString());
                                                price.add(document.get("price").toString());
                                                Parents.add(null);
                                                break;
                                            }
                                        }else{
                                            date.add(document.get("date").toString());
                                            time.add(document.get("time").toString());
                                            venue.add(document.get("venue").toString());
                                            topic.add(document.get("topic").toString());
                                            attendants.add(document.get("attendants").toString());
                                            Document.add(document.get("document ref").toString());
                                            price.add(document.get("price").toString());
                                            Parents.add(null);
                                            break;
                                        }
                                    }
                                }


                            }
                            if(!found){
                                List<String> newDate = new ArrayList<String>();
                                List<String> counter = new ArrayList<String>();
                                for (int i = 0; i < date.size(); i++) {
                                    int MinDay = 1000000;
                                    int MaxDay = 7000000;
                                    String []today = ToDate().split("/");
                                    int Today = Integer.parseInt(today[2]+""+today[1]+""+today[0]);
                                    int forecastDay = Integer.parseInt(date.get(i).replace("/",""));
                                    if(forecastDay - Today>= MinDay && forecastDay - Today < MaxDay){
                                        for (int j = 0; j < Grades.size(); j++) {
                                            if(attendants.get(i).equalsIgnoreCase("All")|| attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                newDate.add(forecastDay + "_" + i);
                                                counter.add(String.valueOf(i));
                                                break;
                                            }
                                        }
                                    }

                                }
                                if(newDate.size()>0){
                                    Collections.sort(newDate);
                                    String []index = newDate.get(0).split("_");
                                    home.getTrip().setText(venue.get(Integer.parseInt(index[1]))+"\n"+topic.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1])));
                                    home.getTrip().setTextColor(Color.RED);
                                    home.setTripInfo(venue.get(Integer.parseInt(index[1]))+"\n"+topic.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1])));
                                    checkParents(Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])),price.get(Integer.parseInt(index[1])));
                                    //Toast.makeText(activity, "date : "+ date.get(Integer.parseInt(index[1])) + "\nvanue : "+ venue.get(Integer.parseInt(index[1])), Toast.LENGTH_SHORT).show();
                                }else{
                                    home.getTrip().setText("Not Scheduled");
                                    home.setTripInfo("Not Scheduled");
                                    //Toast.makeText(activity, "No meetings forecasted", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                List<String> newDate = new ArrayList<String>();
                                List<String> counter = new ArrayList<String>();
                                for (int i = 0; i < date.size(); i++) {
                                    int MinDay = 1000000;
                                    int MaxDay = 7000000;
                                    String []today = ToDate().split("/");
                                    int Today = Integer.parseInt(today[2]+""+today[1]+""+today[0]);
                                    int forecastDay = Integer.parseInt(date.get(i).replace("/",""));
                                    if(forecastDay - Today>= MinDay && forecastDay - Today < MaxDay){
                                        for (int j = 0; j < Grades.size(); j++) {
                                            if(attendants.get(i).equalsIgnoreCase("All")|| attendants.get(i).contains(Grades.get(j).toString()) || attendants.get(i).equalsIgnoreCase(accessKeys.getDefaultUserId())) {
                                                newDate.add(forecastDay + "_" + i);
                                                counter.add(String.valueOf(i));
                                                break;
                                            }
                                        }
                                    }

                                }
                                if(newDate.size()>0){
                                    Collections.sort(newDate);
                                    String []index = newDate.get(0).split("_");
                                    home.getTrip().setText(venue.get(Integer.parseInt(index[1]))+"\n"+topic.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1])));
                                    home.getTrip().setTextColor(Color.RED);
                                    home.setTripInfo(venue.get(Integer.parseInt(index[1]))+"\n"+topic.get(Integer.parseInt(index[1]))+"\n"+date.get(Integer.parseInt(index[1])));
                                    if(Parents.get(Integer.parseInt(index[1]))==null || !Parents.get(Integer.parseInt(index[1])).equalsIgnoreCase(accessKeys.getDefaultUserId())){
                                        checkParents(Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])),price.get(Integer.parseInt(index[1])));
                                    }
                                    //checkParents(Document.get(Integer.parseInt(index[1])),topic.get(Integer.parseInt(index[1])),venue.get(Integer.parseInt(index[1])),date.get(Integer.parseInt(index[1])),time.get(Integer.parseInt(index[1])),textView);
                                    //Toast.makeText(activity, "date : "+ date.get(Integer.parseInt(index[1])) + "\nvanue : "+ venue.get(Integer.parseInt(index[1])), Toast.LENGTH_SHORT).show();
                                }else{
                                    home.getTrip().setText("Not Scheduled");
                                    home.setTripInfo("Not Scheduled");
                                    //Toast.makeText(activity, "No meetings forecasted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }

    //get all images from firestore
    private static  void getTrips(final List Grades){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.trip).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.trip)
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
                            getClosestTrip(Grades);
                        }
                    }
                });
    }

    //getParents count

    public static void checkParents(final String documnetRef,final String Agenda,final String Venue, final String dAte, final String Time, final String Price){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.trip)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean Notfound = false;
                            int countParents = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("attendants") != null && document.get("venue") != null && document.get("topic") != null && document.get("time") != null && document.get("date") != null) {
                                    int total = document.getData().size();
                                    for (int i = 0; i < total; i++) {
                                        if (document.get("parent_" + i) != null) {
                                            countParents = i;
                                        }else{
                                            countParents = i;
                                            Notfound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(Notfound){
                                updateProfiles(documnetRef,countParents,Agenda,Venue,dAte,Time,Price);
                            }/*else{

                                textView.setText("Topic : "+Agenda +"\nVenue : "+Venue+"\nDate : "+dAte+" @ "+Time);
                                textView.setTextColor(Color.RED);
                                home.setTripInfo("Topic : "+Agenda +"\nVenue : "+Venue+ "\nDate : "+dAte+" @ "+Time);
                            }*/
                        }
                    }
                });
    }

    //update Parents count with my number adding + 1
    private static void updateProfiles(final String documnetRef, final int number,final String Agenda,final String Venue, final String dAte, final String Time, final String Price) {
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("parent_"+ number, accessKeys.getDefaultUserId());

            CollectionReference collectionReference = db.collection(constants.trip);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "profile successfully updated");
                        Loginfo("profile added successfully");
                        sendApplicationMessage(Agenda,Venue,dAte,Time,Price);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Logerror("unable to add user comments, " + e.getMessage());

                }
            });
        } catch (Exception exception) {
            exception.getMessage();
            exception.printStackTrace();

        }

    }


    public static void sendApplicationMessage(final String Agenda,final String Venue, final String dAte, final String Time, final String Price){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("subject", "School Trip");
            user.put("message", "Dear Parent,\n\nKindly be aware of this upcoming school trip:\n\nType of Trip : "+Agenda+ "\nDestination : "+Venue+"\nPrice : "+Price+"\nDate : "+dAte+"\nTime : " + Time+
                    "\n\nIt is highly recommended that all pupils affected by this trip should participate.\n\nKind Regards,\nManagement");
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
                                        Log.i(TAG, "message successfully sent");
                                        Loginfo("message sent successfully");
                                    }
                                }

                            });
                            //Client_id(activity,name,surname,email, documentReference.getId(),sex);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error sending message", e);
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }



}

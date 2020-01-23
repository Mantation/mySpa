package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.messageListAdapter;
import adapters.spaListAdapter;
import constants.constants;
import menuFragments.messages;
import methods.globalMethods;
import properties.accessKeys;

import static logHandler.Logging.Loginfo;
import static methods.globalMethods.clearList;

public class messages_ extends Application {
    public static List<String> fromSender = new ArrayList<String>();
    public static List<String> date = new ArrayList<String>();
    public static List<String> time = new ArrayList<String>();
    public static List<String> message = new ArrayList<String>();
    public static List<String> image = new ArrayList<String>();
    static messageListAdapter messagelistAdapter;

    public static void getAllDocuments(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final TextView warning) {
        clearList(fromSender);
        clearList(date);
        clearList(image);
        clearList(time);
        clearList(message);
        //gets all documents from firestore
        getFirestoreSpaMessagesDetails(activity,recyclerView,warning);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    //get all documents from firestore
    public static  void getFirestoreSpaMessagesDetails(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final TextView warning){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(messages.getSpaId()+"_"+constants.appMessages).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(messages.getSpaId()+"_"+constants.appMessages)
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
                            getSpaMessagesDetails(activity,recyclerView,warning);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }

    //get patients
    public static void getSpaMessagesDetails(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final TextView warning){
        clearList(fromSender);
        clearList(date);
        clearList(image);
        clearList(time);
        clearList(message);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(messages.getSpaId()+"_"+constants.appMessages)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.get("messageFrom")!=null  && document.get("date")!=null &&  document.get("time")!=null && document.get("message")!=null && document.get("senderRead")!=null && document.get("document ref")!=null) {
                                    String From = document.get("messageFrom").toString();
                                    String Date = document.get("date").toString();
                                    String Time = document.get("time").toString();
                                    String Message = document.get("message").toString();
                                    Boolean senderRead = Boolean.parseBoolean(document.get("senderRead").toString());
                                    if(!senderRead){
                                        String docRef = document.get("document ref").toString();
                                        updateUnreadMessage(messages.getSpaId(),docRef);
                                    }
                                    if(From.equalsIgnoreCase("sender")){
                                        image.add("none");
                                    }else{
                                        image.add(messages.getSpaImage());
                                    }
                                    fromSender.add(From);
                                    date.add(Date);
                                    time.add(Time);
                                    message.add(Message);
                                    count++;
                                }
                            }

                            final String []MyFromSender = new String [fromSender.size()];
                            final String []MyMessage = new String [message.size()];
                            final String []MyImage = new String [image.size()];
                            final String []Mydate = new String [date.size()];
                            final String []Mytime = new String [time.size()];
                            for (int i = 0; i < fromSender.size(); i++) {
                                MyFromSender[i] = fromSender.get(i);
                                MyMessage[i] = message.get(i);
                                MyImage[i] = image.get(i);
                                Mydate[i] = date.get(i);
                                Mytime[i] = time.get(i);
                            }
                            //sort ascending
                            String tempFromSender = "";
                            String tempImage = "";
                            String tempMessage= "";
                            String tempDate = "";
                            String tempTime = "";
                            for (int i = 0; i < MyMessage.length; i++) {
                                for (int j = i; j < MyMessage.length - 1; j++) {
                                    int MyDate = Integer.parseInt(Mydate[j+1].replace("/",""));
                                    int MyTime = Integer.parseInt(Mytime[j+1].replace(":",""));
                                    int MyOldDate = Integer.parseInt(Mydate[i].replace("/",""));
                                    int MyOldTime = Integer.parseInt(Mytime[i].replace(":",""));
                                    if(MyOldDate>MyDate)
                                    {

                                        tempImage = MyImage[j+1];
                                        MyImage[j+1] = MyImage[i];
                                        MyImage[i] = tempImage;

                                        tempDate = Mydate[j+1];
                                        Mydate[j+1] = Mydate[i];
                                        Mydate[i] = tempDate;

                                        tempTime = Mytime[j+1];
                                        Mytime[j+1] = Mytime[i];
                                        Mytime[i] = tempTime;

                                        tempMessage = MyMessage[j+1];
                                        MyMessage[j+1] = MyMessage[i];
                                        MyMessage[i] = tempMessage;

                                        tempFromSender = MyFromSender[j+1];
                                        MyFromSender[j+1] = MyFromSender[i];
                                        MyFromSender[i] = tempFromSender;

                                    }else{
                                        if (MyOldDate==MyDate){
                                            if(MyOldTime>MyTime)
                                            {
                                                tempImage = MyImage[j+1];
                                                MyImage[j+1] = MyImage[i];
                                                MyImage[i] = tempImage;

                                                tempDate = Mydate[j+1];
                                                Mydate[j+1] = Mydate[i];
                                                Mydate[i] = tempDate;

                                                tempTime = Mytime[j+1];
                                                Mytime[j+1] = Mytime[i];
                                                Mytime[i] = tempTime;

                                                tempMessage = MyMessage[j+1];
                                                MyMessage[j+1] = MyMessage[i];
                                                MyMessage[i] = tempMessage;

                                                tempFromSender = MyFromSender[j+1];
                                                MyFromSender[j+1] = MyFromSender[i];
                                                MyFromSender[i] = tempFromSender;

                                            }

                                        }

                                    }

                                }
                            }
                            messagelistAdapter = new messageListAdapter(activity);
                            messagelistAdapter.setMessages(MyMessage);
                            messagelistAdapter.setFromSender(MyFromSender);
                            messagelistAdapter.setImage(MyImage);
                            messagelistAdapter.setTime(Mytime);
                            messagelistAdapter.setDate(Mydate);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(messagelistAdapter);
                            recyclerView.smoothScrollToPosition(message.size());
                            messages.myAdapter = messagelistAdapter;

                            //display message if no messages are available
                            if(message.size() == 0)
                                warning.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    //update unread messages
    private static void updateUnreadMessage( final String SpaDocument, final String collectionDocument){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //update user details
        CollectionReference collectionReference = db.collection(SpaDocument+"_"+constants.appMessages);
        collectionReference.document(collectionDocument).update("senderRead", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success
                    Loginfo("senderRead successfully updated");
                }
            }

        });

    }


}

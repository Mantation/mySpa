package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.AdminMessagesAdapter;
import adapters.MessagesAdapter;
import constants.constants;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.clearList;

public class adminMessages_ extends Application {
    public static List date_ = new ArrayList<String>();
    public static List time_ = new ArrayList<String>();
    public static List Id_ = new ArrayList<String>();
    public static List subject_ = new ArrayList<String>();
    public static List body_ = new ArrayList<String>();
    public static List isread_ = new ArrayList<String>();
    public static List document_ = new ArrayList<String>();
    static AdminMessagesAdapter adminMessagesAdapter;

    public static void getAllMessages(final Activity activity, final Context context, final RecyclerView recyclerView,final TextView textview) {
        clearList(date_);
        clearList(time_);
        clearList(Id_);
        clearList(subject_);
        clearList(body_);
        clearList(isread_);
        clearList(document_);
        //gets all documents from firestore
        getFirestoreMessages(activity, context,  recyclerView, textview);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    //get all the images
    public static void getMessages(final Activity activity, final Context context, final RecyclerView recyclerView,final TextView textview){
        //gets all images from firestore
        clearList(date_);
        clearList(time_);
        clearList(Id_);
        clearList(subject_);
        clearList(body_);
        clearList(isread_);
        clearList(document_);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.message)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.get("userid")!=null && document.get("date")!=null && document.get("time")!=null && document.get("read")!=null && document.get("message")!=null && document.get("document ref")!=null && document.get("document ref")!="n/a" && document.get("subject")!=null && document.get("school Id")!=null) {
                                    String Id = document.get("school Id").toString();
                                    String date = document.get("date").toString();
                                    String time = document.get("time").toString();
                                    String subject = document.get("subject").toString();
                                    String body = document.get("message").toString();
                                    boolean isRead = Boolean.valueOf(document.get("read").toString());
                                    String documentRef = document.get("document ref").toString();
                                    Id_.add(Id);
                                    date_.add(date);
                                    time_.add(time);
                                    subject_.add(subject);
                                    body_.add(body);
                                    isread_.add(isRead);
                                    document_.add(documentRef);
                                }
                            }
                            String []Id = new String [Id_.size()];
                            String []Mydate = new String [date_.size()];
                            String []Mytime = new String [time_.size()];
                            String []Mysubject = new String [subject_.size()];
                            String []Mybody = new String [body_.size()];
                            boolean []MyIsRead = new boolean[isread_.size()];
                            String []MyDocumentRef = new String [document_.size()];

                            for (int i = 0; i < Id_.size(); i++) {
                                Id[i] = Id_.get(i).toString();
                                Mydate[i] = date_.get(i).toString();
                                Mytime[i] = time_.get(i).toString();
                                Mysubject[i] = subject_.get(i).toString();
                                Mybody[i] = body_.get(i).toString();
                                MyIsRead[i] = Boolean.valueOf(isread_.get(i).toString());
                                MyDocumentRef[i] = document_.get(i).toString();
                            }

                            //sort ascending
                            String tempId = "";
                            String tempdate = "";
                            String temptime = "";
                            String tempsubject = "";
                            String tempbody= "";
                            boolean tempRead;
                            String tempDocument="";
                            for (int i = 0; i < Id.length; i++) {
                                for (int j = i; j < Id.length - 1; j++) {
                                    int MyDate = Integer.parseInt(Mydate[j+1].replace("/",""));
                                    int MyTime = Integer.parseInt(Mytime[j+1].replace(":",""));
                                    int MyOldDate = Integer.parseInt(Mydate[i].replace("/",""));
                                    int MyOldTime = Integer.parseInt(Mytime[i].replace(":",""));
                                    if(MyOldDate<MyDate)
                                    {
                                        tempId = Id [j + 1];
                                        Id [j + 1]= Id [i];
                                        Id [i] = tempId;

                                        tempdate = Mydate [j + 1];
                                        Mydate [j + 1]= Mydate [i];
                                        Mydate [i] = tempdate;

                                        temptime = Mytime [j + 1];
                                        Mytime [j + 1]= Mytime [i];
                                        Mytime [i] = temptime;

                                        tempsubject = Mysubject [j + 1];
                                        Mysubject [j + 1]= Mysubject [i];
                                        Mysubject [i] = tempsubject;

                                        tempbody = Mybody [j + 1];
                                        Mybody [j + 1]= Mybody [i];
                                        Mybody [i] = tempbody;

                                        tempRead = MyIsRead [j + 1];
                                        MyIsRead [j + 1]= MyIsRead [i];
                                        MyIsRead [i] = tempRead;

                                        tempDocument = MyDocumentRef [j + 1];
                                        MyDocumentRef [j + 1]= MyDocumentRef [i];
                                        MyDocumentRef [i] = tempDocument;


                                    }else{
                                        if (MyOldDate==MyDate){
                                            if(MyOldTime<MyTime)
                                            {
                                                tempId = Id [j + 1];
                                                Id [j + 1]= Id [i];
                                                Id [i] = tempId;

                                                tempdate = Mydate [j + 1];
                                                Mydate [j + 1]= Mydate [i];
                                                Mydate [i] = tempdate;

                                                temptime = Mytime [j + 1];
                                                Mytime [j + 1]= Mytime [i];
                                                Mytime [i] = temptime;

                                                tempsubject = Mysubject [j + 1];
                                                Mysubject [j + 1]= Mysubject [i];
                                                Mysubject [i] = tempsubject;

                                                tempbody = Mybody [j + 1];
                                                Mybody [j + 1]= Mybody [i];
                                                Mybody [i] = tempbody;

                                                tempRead = MyIsRead [j + 1];
                                                MyIsRead [j + 1]= MyIsRead [i];
                                                MyIsRead [i] = tempRead;

                                                tempDocument = MyDocumentRef [j + 1];
                                                MyDocumentRef [j + 1]= MyDocumentRef [i];
                                                MyDocumentRef [i] = tempDocument;

                                            }

                                        }

                                    }

                                }
                            }

                            adminMessagesAdapter = new AdminMessagesAdapter(activity);
                            adminMessagesAdapter.setId(Id);
                            adminMessagesAdapter.setDate(Mydate);
                            adminMessagesAdapter.setTime(Mytime);
                            adminMessagesAdapter.setBody(Mybody);
                            adminMessagesAdapter.setSubject(Mysubject);
                            adminMessagesAdapter.setIsRead(MyIsRead);
                            adminMessagesAdapter.setDocumentRef(MyDocumentRef);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(adminMessagesAdapter);
                            if (Id.length == 0)
                                textview.setVisibility(View.VISIBLE);
                        }else{
                            Log.d(TAG, task.getException().toString());

                        }
                    }


                });
    }




    //get all images from firestore
    private static  void getFirestoreMessages(final Activity activity, final Context context, final RecyclerView recyclerView,final TextView textview){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.message).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.message)
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
                            getMessages(activity,context,recyclerView,textview);
                        }
                    }
                });
    }




}

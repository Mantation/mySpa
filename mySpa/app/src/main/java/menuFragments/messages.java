package menuFragments;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import adapters.messageListAdapter;
import constants.constants;
import helperClasses.CustomEditText;
import io.eyec.bombo.myspa.R;
import methods.globalMethods;
import properties.accessKeys;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static io.eyec.bombo.myspa.MainActivity.setFragmentTag;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static properties.accessKeys.setExitApplication;


/**
 * A simple {@link Fragment} subclass.
 */
public class messages extends android.app.Fragment implements View.OnClickListener,View.OnTouchListener{
View myview;
static String spaImage;
static String spaId;
TextView warning;
static RecyclerView recyclerView;
CustomEditText Message;
ImageButton Send;
RecyclerView.Adapter adapter;
public static messageListAdapter myAdapter;
static String fragmentTag = "messages";
int Height;

    public static String getSpaImage() {
        return spaImage;
    }

    public static void setSpaImage(String spaImage) {
        messages.spaImage = spaImage;
    }

    public static String getSpaId() {
        return spaId;
    }

    public static void setSpaId(String spaId) {
        messages.spaId = spaId;
    }

    public messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_messages, container, false);
        accessKeys.setExitApplication(false);
        warning = myview.findViewById(R.id.warning);
        recyclerView = myview.findViewById(R.id.recyclerview_messages);
        Message = myview.findViewById(R.id.initiateText);
        Send = myview.findViewById(R.id.imageButton);
        warning.setVisibility(View.GONE);
        connectionHandler.external.messages_.getAllDocuments(getActivity(), recyclerView, warning);
        trackR_TextArea();
        Message.requestFocus();
        Height = Message.getLayoutParams().height;
        Send.setOnClickListener(this);
        //Message.setOnTouchListener(this);
        recyclerView.setOnTouchListener(this);
        //scroll
        scroll = true;
        MonitorScroll();
        setFragmentTag(fragmentTag);
        setExitApplication(false);
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

    private  void trackR_TextArea(){
        Message.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                ViewGroup.LayoutParams params = Message.getLayoutParams();
                if(Message.getLineCount() > 2) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    Message.setLayoutParams(params);
                    Message.invalidate();
                    Message.setMaxLines(5);
                }else{
                    if(Message.getLineCount() <= 2) {
                        params.height = Height;
                        Message.setLayoutParams(params);
                        Message.invalidate();
                        Message.setMaxLines(5);
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                //if(imm.isActive()){
                //    scrollView.arrowScroll(ScrollView.FOCUS_DOWN);
                //}
            }
            public void afterTextChanged(Editable s) {
                ViewGroup.LayoutParams params = Message.getLayoutParams();
                if(Message.getLineCount() > 2) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    Message.setLayoutParams(params);
                    Message.invalidate();
                    Message.setMaxLines(5);
                }else{
                    if(Message.getLineCount() <= 2) {
                        params.height = Height;
                        Message.setLayoutParams(params);
                        Message.invalidate();
                        Message.setMaxLines(5);
                    }
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        String messageContent = Message.getText().toString();
        if(!messageContent.isEmpty() && messageContent.length()> 20) {
            Message.setText("");
            int count = myAdapter.getItemCount();
            if(accessKeys.getIdnumber() == null){
                showIdDialog(getActivity(),true,count,messageContent,warning);
            }else {
                updateRecycler(getActivity(),false,count,messageContent,warning);
            }
        }else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "enter enough description, more than 20 characters", Toast.LENGTH_LONG).show();
                    Message.requestFocus();
                }
            });
        }

    }
    // update recyclerview
    private static void updateRecycler(final Activity activity,final boolean updateId, final int count, final String messageContent, final TextView textView){
        //get initial values
        String dates_[] = new String[myAdapter.Dates.length];
        String times_[] = new String[myAdapter.Dates.length];
        String messages_[] = new String[myAdapter.Dates.length];
        String image_[] = new String[myAdapter.Dates.length];
        String fromSender_[] = new String[myAdapter.Dates.length];
        for (int i = 0; i < myAdapter.Dates.length; i++) {
            dates_[i] = myAdapter.Dates[i];
            times_[i] = myAdapter.Times[i];
            messages_[i] = myAdapter.Messages[i];
            image_[i] = myAdapter.Image[i];
            fromSender_[i] = myAdapter.FromSender[i];
        }
        //extend array
        myAdapter.Dates = new String[count + 1];
        myAdapter.Times = new String[count + 1];
        myAdapter.Messages = new String[count + 1];
        myAdapter.Image = new String[count + 1];
        myAdapter.FromSender = new String[count + 1];
        //reallocate variables
        for (int x = 0; x < dates_.length; x++) {
            myAdapter.Dates[x] = dates_[x];
            myAdapter.Times[x] = times_[x];
            myAdapter.Messages[x] = messages_[x];
            myAdapter.Image[x] = image_[x];
            myAdapter.FromSender[x] = fromSender_[x];
        }
        //allocate variables
        myAdapter.Dates[count] = ToDate();
        myAdapter.Times[count] = "sending...";
        myAdapter.Messages[count] = messageContent;
        myAdapter.Image[count] = "none";
        myAdapter.FromSender[count] = "sender";
        myAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(myAdapter.getItemCount()-1);
        sendUserDetails(activity,updateId,accessKeys.getIdnumber(),accessKeys.getIdType(),messageContent,getSpaId(),myAdapter.Times, count,textView);

    }
    //ID Dialog
    public static void showIdDialog(final Activity activity,final boolean updateId, final int count, final String messageContent,final TextView textView){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.idlayout);
        dialog.setCancelable(true);
        final EditText idno = (EditText) dialog.findViewById(R.id.idno);
        CardView Submit = (CardView) dialog.findViewById(R.id.MySubmit);
        final RadioButton Citizen = (RadioButton) dialog.findViewById(R.id.citizen);
        RadioButton NonCitizen = (RadioButton) dialog.findViewById(R.id.non_citizen);
        RelativeLayout Main = (RelativeLayout) dialog.findViewById(R.id.main);
        Citizen.setChecked(true);
        dialog.show();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String citizen;
                if(Citizen.isChecked()){
                    citizen = "RSA ID";
                }else{
                    citizen = "Non-RSA ID";
                }
                if(!idno.getText().toString().isEmpty() && idno.getText().toString().length() < 13 && Citizen.isChecked()) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "RSA ID containt 13 characters", Toast.LENGTH_LONG).show();
                            idno.requestFocus();
                        }
                    });
                }else if(!idno.getText().toString().isEmpty() && idno.getText().toString().length() < 4 && !Citizen.isChecked()){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "enter valid document no, 5 characters", Toast.LENGTH_LONG).show();
                            idno.requestFocus();
                        }
                    });

                }else{
                    dialog.dismiss();
                    accessKeys.setIdnumber(idno.getText().toString());
                    accessKeys.setIdType(citizen);
                    updateRecycler(activity,updateId,count,messageContent,textView);
                }
            }
        });
        Citizen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                idno.setHint("RSA Id number");
                int maxLength = 13;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                idno.setFilters(fArray);
                idno.setInputType(InputType.TYPE_CLASS_NUMBER);
                return false;
            }
        });
        NonCitizen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                idno.setHint("Foreign doc number");
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                idno.setFilters(fArray);
                idno.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });
    }

    //sending user Message
    private static void sendUserDetails(final Activity activity, final boolean updateId, final String Id, final String IdType, final String Message,final String SpaDocument,final String []TimeLayout, final int index,final TextView textView){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            final String ttime = Time();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("document ref", defaultvalue);
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("name", accessKeys.getName());
            user.put("surname", accessKeys.getSurname());
            user.put("email", accessKeys.getEmail());
            user.put("phone", accessKeys.getPhone());
            user.put("altphone", accessKeys.getAltPhone());
            user.put("gender", accessKeys.getGender());
            user.put("date", ToDate());
            user.put("time", ttime);
            user.put("message", Message);
            user.put("senderRead", true);
            user.put("receiverRead", false);
            user.put("messageFrom", "sender");

            // Add a new document with a generated ID
            db.collection(SpaDocument+"_"+ constants.appMessages)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(SpaDocument+"_"+constants.appMessages);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //set Realm user information;
                                        Loginfo("user Personal Details successfully added");
                                        if(updateId){
                                            CollectionReference collectionReference = db.collection(constants.users);
                                            collectionReference.document(accessKeys.getDefaultDocument()).update("id number", Id, "Id type",IdType).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        //set Realm user information;
                                                        Loginfo("user Personal Details successfully added");
                                                        //check existing client
                                                        getClientValidityInformation(SpaDocument,SpaDocument+"_clients",Id,document);
                                                        //getFirestoreClient(view,SpaDocument+"_clients",Id,document,dialog);
                                                        //update time on recyclerview
                                                        TimeLayout[index] = ttime;
                                                        myAdapter.notifyDataSetChanged();
                                                        recyclerView.smoothScrollToPosition(myAdapter.getItemCount());
                                                        textView.setVisibility(View.GONE);
                                                    }
                                                }

                                            });

                                        }else{
                                            //check existing client
                                            //getFirestoreClient(view,SpaDocument+"_clients",accessKeys.getIdnumber(),document,dialog);
                                            getClientValidityInformation(SpaDocument,SpaDocument+"_clients",Id,document);
                                            //update time on recyclerview
                                            TimeLayout[index] = ttime;
                                            myAdapter.notifyDataSetChanged();
                                            recyclerView.smoothScrollToPosition(myAdapter.getItemCount());
                                            textView.setVisibility(View.GONE);
                                        }
                                    }else{
                                        globalMethods.stopProgress = true;
                                        Logerror("unable to update user ID Details, ");
                                        //update time on recyclerview
                                        TimeLayout[index] = "Failed";
                                        myAdapter.notifyDataSetChanged();
                                        recyclerView.smoothScrollToPosition(myAdapter.getItemCount());
                                        textView.setVisibility(View.GONE);

                                    }
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            globalMethods.stopProgress = true;
                            Logerror("unable to add user Personal Details, " + e.getMessage());
                            //update time on recyclerview
                            TimeLayout[index] = "Failed";
                            myAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(myAdapter.getItemCount());
                            textView.setVisibility(View.GONE);
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }
    }

    public static void getClientValidityInformation( final String SpaDocument, final String SpaClients, final String ClientId, final String DocumentReference) {
        //gets all documents from firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SpaClients).whereEqualTo("IdNumber", ClientId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean isFound = false;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.get("IdNumber") != null) {
                            if (document.get("IdNumber").toString().equalsIgnoreCase(ClientId)) {
                                isFound = true;
                            }

                        }
                    }
                    updateExistingClient(SpaDocument,isFound,DocumentReference);
                }
            }
        });

    }

    private static void updateExistingClient(final String SpaDocument, final boolean Existing, final String DocumentReference){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //update user details
        CollectionReference collectionReference = db.collection(SpaDocument+"_"+constants.appMessages);
        collectionReference.document(DocumentReference).update("Existing user", Existing).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success
                    Loginfo("user Items successfully added to cart");
                }
            }

        });

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.recyclerview_messages){
            scroll = false;
        }
        return false;
    }

    public static boolean scroll;
    public static final Handler handler = new Handler();
    final static int delay = 500; //milliseconds
    public static Runnable myRunnable;
    public static void MonitorScroll(){
        handler.postDelayed(myRunnable = new Runnable(){
            public void run(){
                if(scroll) {
                    if (myAdapter != null)
                        if(myAdapter.getItemCount()!= 0)
                            recyclerView.smoothScrollToPosition(myAdapter.getItemCount()-1);
                }else{
                    handler.removeMessages(0);
                    handler.removeCallbacks(myRunnable);
                    handler.removeCallbacksAndMessages(null);
                    handler.removeCallbacksAndMessages(myRunnable);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

}

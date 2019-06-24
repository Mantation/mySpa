package authentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import accessors.userDetails;
import constants.constants;
import io.eyec.bombo.survey.R;
import io.realm.Realm;
import menuFragments.begin;
import menuFragments.quizInfo;
import menuFragments.survey;
import methods.globalMethods;
import pl.droidsonroids.gif.GifImageView;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ShowDialog;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.clearList;

public class auth {

    static String phoneVerificationId;
    static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    static PhoneAuthProvider.ForceResendingToken resendingToken;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    public static String smsMessage;
    static Realm realm;
    public static PhoneAuthCredential Crdentials;
    public static String smsCode;

    public static void PhoneRegistration(final Activity activity, final String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity, mCallbacks);
    }

    public static void callback_verification(final Activity activity) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // this method executed if phone number verified you can do your stuff here if you only want to verify phone number.
                // or you can also use this credentials for authenticate purpose.
                //SignInWithPhoneAuthCredential(activity,credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(constants.PhoneRegTag, "Invalid Credentials");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(constants.PhoneRegTag, "SMS Verification Pin needed");
                }
            }


            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendingToken = token;
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                //and then execute your method if number entered is correct.
            }
        };
    }

    public static void verifyCode(final Activity activity, final TextView textView){
        String code = textView.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,code);
        //SignInWithPhoneAuthCredential(activity,credential);

    }

    public static void SignInWithPhoneAuthCredential(final Activity activity, PhoneAuthCredential credential, final Dialog dialog, final EditText input, final Button send, final Button resend, final GifImageView counter){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isComplete()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = null;
                            if (user != null) {
                                uid = user.getUid();
                                counter.setVisibility(View.GONE);
                                dialog.dismiss();
                                ShowDialog(activity);
                                Toast.makeText(activity, "Signed In", Toast.LENGTH_SHORT).show();
                                Toast.makeText(activity, uid, Toast.LENGTH_SHORT).show();
                                if (globalMethods.isNetworkAvailable(activity)){
                                    userInformation(activity);
                                }else{
                                    SaveUserInfo(activity);
                                }
                            }else{
                                Toast.makeText(activity, "Invalid Code", Toast.LENGTH_SHORT).show();
                                counter.setVisibility(View.GONE);
                                input.setVisibility(View.VISIBLE);
                                send.setVisibility(View.VISIBLE);
                                resend.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(activity, "Invalid Code", Toast.LENGTH_SHORT).show();
                                globalMethods.stopProgress = true;
                            }
                        }
                        //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        //firebaseUser = firebaseAuth.getCurrentUser();
                        //FirebaseUser user = task.getResult().getUser();
                        //Toast.makeText(activity, "Registered successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public  static void resendCode(final Activity activity, final TextView textView){
        String phoneNumber = textView.getText().toString();
        callback_verification(activity);
        PhoneRegistration(activity,phoneNumber);
    }

    public static void signOut(View view){
        firebaseAuth.signOut();
    }

    public static void InitiateAuth(final Activity activity, final String phoneNumber){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.tokenlayout);
        dialog.setCancelable(true);
        final EditText input = (EditText) dialog.findViewById(R.id.token);
        final Button send = (Button) dialog.findViewById(R.id.send);
        final Button resend = (Button) dialog.findViewById(R.id.resend);
        final GifImageView counter = (GifImageView) dialog.findViewById(R.id.counter);
        counter.setVisibility(View.INVISIBLE);
        final String phone = "+27"+phoneNumber.substring(1,phoneNumber.length());
        RegisterPhone(activity,phone,dialog,input,send,resend,counter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input.getText().toString().isEmpty()&& input.getText().toString().length()>5){
                    final String token = input.getText().toString();
                    getFirestoreCategory(activity,dialog,input,send,resend,counter,token,phoneNumber);
                    //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,token);
                    //SignInWithPhoneAuthCredential(activity,credential,dialog,input,send,resend,counter);
                    //ShowDialog(activity);
                    /*final String SmsToken = input.getText().toString();
                    if (smsMessage==null){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,SmsToken);
                        smsMessage = credential.getSmsCode();
                    }
                    if(SmsToken.equalsIgnoreCase(smsMessage)){
                        dialog.dismiss();
                        if (globalMethods.isNetworkAvailable(activity)){
                            userInformation(activity);
                        }else{
                            SaveUserInfo(activity);
                        }
                    }*/
                    //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,token);
                    //SignInWithPhoneAuthCredential(activity,credential,dialog,input,send,resend,counter);
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,60,TimeUnit.SECONDS,activity,mCallbacks,resendingToken);
                Toast.makeText(activity, "verification code is resent", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
    }


    public static void RegisterPhone(final Activity activity,final String phoneNumber,final Dialog dialog,final EditText input,final Button send,final Button resend,final GifImageView counter){
        // The test phone number and code should be whitelisted in the console.
        //String phoneNumber = phone;//"+27820964587";
        smsCode = phoneVerificationId;


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                60L,
                TimeUnit.SECONDS,
                activity, /* activity */
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...
                        //SignInWithPhoneAuthCredential(activity,credential,dialog,input,send,resend,counter);
                        Toast.makeText(activity, "credentials : "+credential, Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, "Sms : "+smsCode, Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, "phone verification Id : "+phoneVerificationId, Toast.LENGTH_SHORT).show();
                        smsMessage = credential.getSmsCode();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.e(TAG, "Error adding document", e);
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.d(constants.PhoneRegTag, "Invalid Credentials");
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Log.d(constants.PhoneRegTag, "SMS Verification Pin needed");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        phoneVerificationId = verificationId;
                        resendingToken = token;
                        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,token.toString());
                        //smsMessage = credential.getSmsCode();
                        counter.setVisibility(View.GONE);
                        input.setVisibility(View.VISIBLE);
                        input.requestFocus();
                        send.setVisibility(View.VISIBLE);
                        resend.setVisibility(View.VISIBLE);
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        //and then execute your method if number entered is correct.
                    }


                });

    }


    public static void userInformation(final Activity activity){
        ShowDialog(activity);
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("document ref", defaultvalue);
            user.put("what is your name", quizInfo.answers.get(0));
            user.put("What is your surname", quizInfo.answers.get(1));
            user.put("How old are you?", quizInfo.answers.get(2));
            user.put("Where are you from?", quizInfo.answers.get(3));
            user.put("What is your gender?", quizInfo.answers.get(4));
            user.put("Do you work?", quizInfo.answers.get(5));
            user.put("What is your mode of transport?", quizInfo.answers.get(6));
            user.put("Do you know anything about RBED (Royal Bafokeng Economic Board)?", quizInfo.answers.get(7));
            user.put("Do you know anything about RBI (Royal Bafokeng Institute)?", quizInfo.answers.get(8));
            user.put("Do you know anything about RBH (Royal Bafokeng Holdings)?", quizInfo.answers.get(9));
            user.put("Do you know anything about Akanyang?", quizInfo.answers.get(10));
            user.put("Do you know anything about Lebone II?", quizInfo.answers.get(11));
            user.put("Do you believe the RBED information is accessible to everyone?", quizInfo.answers.get(12));
            user.put("Do you believe Lebone II is built for morafe?", quizInfo.answers.get(13));
            user.put("Are you proud of being Mofokeng?", quizInfo.answers.get(14));
            user.put("Do you believe the RBI information is accessible to everyone?", quizInfo.answers.get(15));
            user.put("Phone Number", quizInfo.answers.get(16));
            user.put("GPS Longitute", quizInfo.answers.get(17));
            user.put("GPS Latitute", quizInfo.answers.get(18));
            user.put("date", ToDate());
            user.put("time", Time());

            // Add a new document with a generated ID
            db.collection(constants.users)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.users);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(activity, "Information successfully captured", Toast.LENGTH_SHORT).show();
                                        clearList(quizInfo.answers);
                                        quizInfo.setCount(0);
                                        methods.globalMethods.loadFragmentsWithTag(R.id.main, new begin(), activity);
                                        //Toast.makeText(this, "Document created/updated", Toast.LENGTH_SHORT).show();
                                        globalMethods.stopProgress = true;
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
            exception.getMessage();
            exception.printStackTrace();
        }

    }


    public static void SaveUserInfo(final Activity activity){
        ShowDialog(activity);
        Realm.init(activity);
        new initiaterealm().onCreate();
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                userDetails user = bgRealm.createObject(userDetails.class);
                user.setUserName(quizInfo.answers.get(0));
                user.setUserSurname(quizInfo.answers.get(1));
                user.setUserAge(quizInfo.answers.get(2));
                user.setUserOrigin(quizInfo.answers.get(3));
                user.setUserGender(quizInfo.answers.get(4));
                user.setUserWork(quizInfo.answers.get(5));
                user.setUserTransport(quizInfo.answers.get(6));
                user.setUserRBEDKnowledge(quizInfo.answers.get(7));
                user.setUserRBIKnowledge(quizInfo.answers.get(8));
                user.setUserRBHKnowledge(quizInfo.answers.get(9));
                user.setUserAkanyangKnowledge(quizInfo.answers.get(10));
                user.setUserLeboneIIKnowledge(quizInfo.answers.get(11));
                user.setUserRBEDInfoAccessible(quizInfo.answers.get(12));
                user.setUserLeboneIIBuiltforMorafe(quizInfo.answers.get(13));
                user.setUserMofokengPride(quizInfo.answers.get(14));
                user.setUserRBIInfoAccessible(quizInfo.answers.get(15));
                user.setUserPhoneNumber(quizInfo.answers.get(16));
                user.setUserLongitute(quizInfo.answers.get(17));
                user.setUserLatitute(quizInfo.answers.get(18));
                user.setQuizDate(ToDate());
                user.setQuizTime(Time());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Information successfully captured", Toast.LENGTH_SHORT).show();
                clearList(quizInfo.answers);
                quizInfo.setCount(0);
                methods.globalMethods.loadFragmentsWithTag(R.id.main, new begin(), activity);
                globalMethods.stopProgress = true;

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "unable to add user Personal Details, " + error.getMessage());
                globalMethods.stopProgress = true;

            }
        });
        realm.close();
    }


    //Register phones
    public static void registerPhoneNumber( final String phoneNumber){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("document ref", defaultvalue);
            user.put("phone", phoneNumber);
            // Add a new document with a generated ID
            db.collection(constants.phones)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.phones);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        globalMethods.stopProgress = true;
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
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }

    static boolean found;
    //check all the phones
    public static void getFirestoreCategories(final Activity activity, final Dialog dialog, final EditText input, final Button send, final Button resend, final GifImageView counter,final String token,final String PhoneNumber){
        //gets all documents from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.phones)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.get("phone")!=null){
                                    String phone = document.get("phone").toString();
                                    if (phone.equals(PhoneNumber)) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (!found){
                                counter.setVisibility(View.VISIBLE);
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,token);
                                SignInWithPhoneAuthCredential(activity,credential,dialog,input,send,resend,counter);
                                registerPhoneNumber(PhoneNumber);
                            }else{
                                Toast.makeText(activity, "this phone number is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity, final Dialog dialog, final EditText input, final Button send, final Button resend, final GifImageView counter,final String token,final String PhoneNumber){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.phones).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.phones)
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
                            getFirestoreCategories(activity,dialog,input,send,resend,counter,token,PhoneNumber);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }





}

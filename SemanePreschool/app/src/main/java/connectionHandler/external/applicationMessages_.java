package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
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

import adapters.AdminApplicationsAdapter;
import adapters.ApplicationsAdapter;
import constants.constants;
import methods.globalMethods;
import properties.accessKeys;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.InitializeFirstLetter;
import static methods.globalMethods.clearList;

public class applicationMessages_ extends Application {
    public static List<String> Image = new ArrayList<String>();
    public static List<String> ProofOfPayment = new ArrayList<String>();
    public static List<String> Profile = new ArrayList<String>();
    public static List<String> Date = new ArrayList<String>();
    public static List<String> Status = new ArrayList<String>();
    public static List<String> Name = new ArrayList<String>();
    public static List<String> ID = new ArrayList<String>();
    public static List<String> Users = new ArrayList<String>();
    public static List<String> documentRef = new ArrayList<String>();
    public static List<String> Token = new ArrayList<String>();
    static AdminApplicationsAdapter adminApplicationsAdapter;


    public static void getAllDocuments(final Activity activity, final RecyclerView recyclerView, final String value, final TextView textView) {
        clearList(Profile);
        clearList(Image);
        clearList(ProofOfPayment);
        clearList(Date);
        clearList(Status);
        clearList(Name);
        clearList(ID);
        clearList(Users);
        clearList(documentRef);
        clearList(Token);
        //gets all documents from firestore
        getFirestoreCategory(activity,recyclerView,value,textView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    public static void getFirestoreCategories(final Activity activity, final RecyclerView recyclerView, final String status, final TextView textView){
        //gets all documents from firestore
        clearList(Profile);
        clearList(Image);
        clearList(ProofOfPayment);
        clearList(Date);
        clearList(Status);
        clearList(Name);
        clearList(ID);
        clearList(Users);
        clearList(documentRef);
        clearList(Token);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.apply)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.get("status")!=null){
                                    String MyStatus = document.get("status").toString();
                                    if (status.equals(MyStatus) || status.equalsIgnoreCase("all")) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if (document.get("child_name")!=null && document.get("child_surname")!=null && document.get("child_id")!=null && document.get("child_Image")!=null && document.get("proof Of Payment")!=null && document.get("status")!=null && document.get("date")!=null && document.get("time")!=null&& document.get("Guardian2_Workphone")!=null &&
                                                document.get("Guardian2_addresss1")!=null && document.get("Guardian2_addresss2")!=null && document.get("Guardian2_addresss3")!=null && document.get("Guardian2_addresssCode")!=null && document.get("Guardian2_addresssTown")!=null && document.get("Guardian2_addresssProvince")!=null && document.get("Guardian2_AddressSameAsChild")!=null &&
                                                document.get("Guardian2_employer")!=null && document.get("Guardian2_email")!=null&& document.get("Guardian2_relationship")!=null&& document.get("Guardian2_name")!=null&& document.get("Guardian2_surname")!=null && document.get("Guardian2_Homephone")!=null&& document.get("child_grade")!=null && document.get("child_gender")!=null &&
                                                document.get("child_addresss1")!=null && document.get("child_addresss2")!=null&& document.get("child_addresss3")!=null&& document.get("child_addresssCode")!=null&& document.get("child_addresssTown")!=null && document.get("child_addresssProvince")!=null&& document.get("child_MedicalConditions")!=null && document.get("child_HasAllergies")!=null &&
                                                document.get("Child_Allergies")!=null && document.get("userid")!=null){
                                            if(!document.get("child_Image").toString().equalsIgnoreCase("null") && !document.get("child_Image").toString().equalsIgnoreCase("n/a")&& !document.get("proof Of Payment").toString().equalsIgnoreCase("null") && !document.get("proof Of Payment").toString().equalsIgnoreCase("n/a")) {
                                                Image.add(document.get("child_Image").toString());
                                                ProofOfPayment.add(document.get("proof Of Payment").toString());
                                                Date.add(document.get("date").toString() + " - " +document.get("time").toString());
                                                Status.add(document.get("status").toString());
                                                Name.add(InitializeFirstLetter(document.get("child_name").toString())+" "+InitializeFirstLetter(document.get("child_surname").toString()));
                                                ID.add(document.get("child_id").toString());
                                                Users.add(document.get("userid").toString());
                                                Token.add(document.get("token").toString());
                                                documentRef.add(document.get("document ref").toString());
                                                String Address ="";
                                                String Address2 = document.get("child_addresss2").toString();
                                                String Address3 = document.get("child_addresss3").toString();
                                                if(Address2.isEmpty()){
                                                    Address+="";
                                                }else{
                                                    Address += Address2+"\n";
                                                }
                                                if(Address3.isEmpty()){
                                                    Address+="";
                                                }else{
                                                    Address+= Address3+"\n";
                                                }
                                                String workPhone="";
                                                if (!document.get("Guardian2_Workphone").toString().equalsIgnoreCase(""))
                                                    workPhone = "Work phone : "+document.get("Guardian2_Workphone").toString()+"\n";
                                                String homePhone="";
                                                if (!document.get("Guardian2_Homephone").toString().equalsIgnoreCase(""))
                                                    homePhone = "Home phone : "+document.get("Guardian2_Homephone").toString()+"\n";
                                                String GuardianAddress ="";
                                                String GuardAddress2 = document.get("Guardian2_addresss2").toString();
                                                String GuardAddress3 = document.get("Guardian2_addresss3").toString();
                                                if(Address2.isEmpty()){
                                                    GuardianAddress+="";
                                                }else{
                                                    GuardianAddress += Address2+"\n";
                                                }
                                                if(Address3.isEmpty()){
                                                    GuardianAddress+="";
                                                }else{
                                                    GuardianAddress+= Address3+"\n";
                                                }
                                                String child = InitializeFirstLetter(document.get("child_grade").toString())+" Applicant\n\n" +
                                                        globalMethods.InitializeFirstLetter(document.get("child_name").toString())+" " +globalMethods.InitializeFirstLetter(document.get("child_surname").toString())+"\n"+
                                                        document.get("child_id").toString()+"\n"+
                                                        document.get("child_gender").toString()+"\n\n"+
                                                        "Child Address\n"+
                                                        document.get("child_addresss1").toString()+"\n"+
                                                        Address+
                                                        document.get("child_addresssCode").toString()+"\n"+
                                                        document.get("child_addresssTown").toString()+"\n"+
                                                        document.get("child_addresssProvince").toString()+"\n\n"+
                                                        "Child Medical Details\n"+
                                                        "Medical Condition : "+ document.get("child_MedicalConditions").toString()+"\n"+
                                                        "Has Allergies : "+ document.get("child_HasAllergies").toString()+"\n"+
                                                        "Allergies : "+ document.get("Child_Allergies").toString()+"\n\n"+
                                                        "Guardian/Parent 2  Details\n\n"+
                                                        globalMethods.InitializeFirstLetter(document.get("Guardian2_name").toString())+" " +globalMethods.InitializeFirstLetter(document.get("Guardian2_surname").toString())+"\n"+
                                                        document.get("Guardian2_relationship").toString()+"\n"+
                                                        document.get("Guardian2_relationship").toString()+"\n"+
                                                        "Cell phone : " + document.get("Guardian2_Cellphone").toString()+"\n"+
                                                        document.get("Guardian2_email").toString()+"\n"+
                                                        workPhone+
                                                        homePhone+
                                                        "Employer Details\n\n"+
                                                        document.get("Guardian2_employer").toString()+"\n"+
                                                        "Guardian/Parent 2 Address\n\n"+
                                                        document.get("Guardian2_addresss1").toString()+"\n"+
                                                        document.get("Guardian2_addresssCode").toString()+"\n"+
                                                        document.get("Guardian2_addresssTown").toString()+"\n"+
                                                        document.get("Guardian2_addresssProvince").toString()+"\n"+
                                                        "Lives with child ? " + document.get("Guardian2_AddressSameAsChild").toString()+"\n\n";
                                                Profile.add(child);
                                            }
                                        }
                                    }
                                }
                            }

                            String []MyProfile = new String [Profile.size()];
                            String []MyStatus = new String [Status.size()];
                            String []MyImage = new String [Image.size()];
                            String []MyProofOfPayment = new String [ProofOfPayment.size()];
                            String []MyDate = new String [Date.size()];
                            String []MyName = new String [Name.size()];
                            String []MyIdNumber = new String [ID.size()];
                            String []MyUsers = new String [Users.size()];
                            String []MyDocRef = new String [documentRef.size()];
                            String []MyToken = new String [Token.size()];

                            for (int i = 0; i < Profile.size(); i++) {
                                MyProfile[i] = Profile.get(i);
                                MyStatus[i] = Status.get(i);
                                MyImage[i] = Image.get(i);
                                MyProofOfPayment[i] = ProofOfPayment.get(i);
                                MyDate[i] = Date.get(i);
                                MyName[i] = Name.get(i);
                                MyIdNumber[i] = ID.get(i);
                                MyUsers[i] = Users.get(i);
                                MyDocRef[i] = documentRef.get(i);
                                MyToken[i] = Token.get(i);
                            }
                            adminApplicationsAdapter = new AdminApplicationsAdapter(activity,activity);
                            adminApplicationsAdapter.setImage(MyImage);
                            adminApplicationsAdapter.setProofOfPayment(MyProofOfPayment);
                            adminApplicationsAdapter.setProfile(MyProfile);
                            adminApplicationsAdapter.setDate(MyDate);
                            adminApplicationsAdapter.setStatus(MyStatus);
                            adminApplicationsAdapter.setName(MyName);
                            adminApplicationsAdapter.setIDNumber(MyIdNumber);
                            adminApplicationsAdapter.setUsers(MyUsers);
                            adminApplicationsAdapter.setDocumentRef(MyDocRef);
                            adminApplicationsAdapter.setToken(MyToken);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(adminApplicationsAdapter);
                            if(Profile.size() == 0)
                                textView.setVisibility(View.VISIBLE);


                        }
                    }
                });
    }

    //get all documents from firestore
    private static  void getFirestoreCategory(final Activity activity,final RecyclerView recyclerView, final String value, final TextView textView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.apply).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.apply)
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
                            getFirestoreCategories(activity,recyclerView,value,textView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }







}

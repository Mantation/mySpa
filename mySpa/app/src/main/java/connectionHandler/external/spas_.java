package connectionHandler.external;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapters.spaListAdapter;
import constants.constants;
import io.eyec.bombo.myspa.R;
import menuFragments.main;
import menuFragments.splash;
import properties.accessKeys;
import survey.ratings;

import static methods.globalMethods.clearList;

public class spas_ extends Application {
    public static List<String> spa = new ArrayList<String>();
    public static List<String> shortLoc = new ArrayList<String>();
    public static List<String> image = new ArrayList<String>();
    public static List<String> phone = new ArrayList<String>();
    public static List<String> BusinessLat = new ArrayList<String>();
    public static List<String> BusinessLong = new ArrayList<String>();
    public static List<String> email = new ArrayList<String>();
    public static List<String> spaId = new ArrayList<String>();
    public static List<String> Promo = new ArrayList<String>();
    public static List<String> Longitude = new ArrayList<String>();
    public static List<String> Latitude = new ArrayList<String>();
    public static List<String> Town = new ArrayList<String>();
    public static List<String> Province = new ArrayList<String>();
    static spaListAdapter spalistAdapter;


    public static void getAllDocuments(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final ProgressBar progressBar, final ImageView imageView) {
        clearList(spa);
        clearList(shortLoc);
        clearList(image);
        clearList(phone);
        clearList(BusinessLat);
        clearList(BusinessLong);
        clearList(email);
        clearList(spaId);
        clearList(Promo);
        clearList(Longitude);
        clearList(Latitude);
        clearList(Town);
        clearList(Province);
        //gets all documents from firestore
        getFirestoreSpaDetails(activity,recyclerView,progressBar,imageView);
        //getCommentIssues(activity, context, view, recyclerView); - should be like this
    }

    //get patients
    public static void getSpaDetails(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final ProgressBar progressBar, final ImageView imageView){
        clearList(spa);
        clearList(shortLoc);
        clearList(image);
        clearList(phone);
        clearList(BusinessLat);
        clearList(BusinessLong);
        clearList(email);
        clearList(spaId);
        clearList(Promo);
        clearList(Longitude);
        clearList(Latitude);
        clearList(Town);
        clearList(Province);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.spa)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.get("spa")!=null  && document.get("banner")!=null &&  document.get("document")!=null && document.get("bannerSet")!=null) {
                                    boolean hasBanner = Boolean.parseBoolean(document.get("bannerSet").toString());
                                    if (hasBanner) {
                                        String Spa = document.get("spa").toString();
                                        //String Loc  = document.get("location").toString();
                                        String Img = document.get("banner").toString();
                                        //String Phone  = document.get("phone").toString();
                                        //String Addr  = document.get("address_physical").toString();
                                        //String Email  = document.get("email").toString();
                                        String Doc = document.get("document").toString();
                                        if (document.get("promo") == null) {
                                            Promo.add("none");
                                        } else {
                                            String promo = document.get("promo").toString();
                                            Promo.add(promo);
                                        }
                                        if (document.get("location") == null) {
                                            shortLoc.add("none");
                                        } else {
                                            String loc = document.get("location").toString();
                                            shortLoc.add(loc);
                                        }
                                        if (document.get("phone") == null) {
                                            phone.add("none");
                                        } else {
                                            String mobile = document.get("phone").toString();
                                            phone.add(mobile);
                                        }
                                        if (document.get("GPS Latitude") == null && document.get("GPS Longitute") == null ) {
                                            BusinessLat.add("none");
                                            BusinessLong.add("none");
                                        } else {
                                            String Latitude = document.get("GPS Latitude").toString();
                                            String Longitute = document.get("GPS Longitute").toString();
                                            BusinessLat.add(Latitude);
                                            BusinessLong.add(Longitute);
                                        }
                                        if (document.get("email") == null) {
                                            email.add("none");
                                        } else {
                                            String mail = document.get("email").toString();
                                            email.add(mail);
                                        }
                                        spa.add(Spa);
                                        image.add(Img);
                                        spaId.add(Doc);
                                        Longitude.add(accessKeys.getLongitude());
                                        Latitude.add(accessKeys.getLatitude());
                                        Town.add(document.get("town").toString());
                                        Province.add(document.get("province").toString()+"~"+count);
                                        count++;
                                    }
                                }
                            }
                            //sort data by province
                            Collections.sort(Province);
                            List<String> spas = new ArrayList<String>();
                            List<String> shortLocs = new ArrayList<String>();
                            List<String> images = new ArrayList<String>();
                            List<String> phones = new ArrayList<String>();
                            List<String> BusinessLats = new ArrayList<String>();
                            List<String> BusinessLongs = new ArrayList<String>();
                            List<String> emails = new ArrayList<String>();
                            List<String> spaIds = new ArrayList<String>();
                            List<String> Promos = new ArrayList<String>();
                            List<String> Longitudes = new ArrayList<String>();
                            List<String> Latitudes = new ArrayList<String>();
                            List<String> Towns = new ArrayList<String>();
                            List<String> Provinces = new ArrayList<String>();
                            for (int i = 0; i < Province.size(); i++) {
                                String []getIndex = Province.get(i).split("~");
                                spas.add(spa.get(Integer.parseInt(getIndex[1])));
                                shortLocs.add(shortLoc.get(Integer.parseInt(getIndex[1])));
                                images.add(image.get(Integer.parseInt(getIndex[1])));
                                phones.add(phone.get(Integer.parseInt(getIndex[1])));
                                BusinessLats.add(BusinessLat.get(Integer.parseInt(getIndex[1])));
                                BusinessLongs.add(BusinessLong.get(Integer.parseInt(getIndex[1])));
                                emails.add(email.get(Integer.parseInt(getIndex[1])));
                                spaIds.add(spaId.get(Integer.parseInt(getIndex[1])));
                                Promos.add(Promo.get(Integer.parseInt(getIndex[1])));
                                Longitudes.add(Longitude.get(Integer.parseInt(getIndex[1])));
                                Latitudes.add(Latitude.get(Integer.parseInt(getIndex[1])));
                                Towns.add(Town.get(Integer.parseInt(getIndex[1])));
                                Longitudes.add(Longitude.get(Integer.parseInt(getIndex[1])));
                                Provinces.add(getIndex[0]);
                            }
                            clearList(spa);
                            clearList(shortLoc);
                            clearList(image);
                            clearList(phone);
                            clearList(BusinessLat);
                            clearList(BusinessLong);
                            clearList(email);
                            clearList(spaId);
                            clearList(Promo);
                            clearList(Longitude);
                            clearList(Latitude);
                            clearList(Town);
                            clearList(Province);

                            final String []MySpa = new String [spas.size()];
                            final String []MyLoc = new String [shortLocs.size()];
                            final String []MyImage = new String [images.size()];
                            final String []MyPhone = new String [phones.size()];
                            final String []MyBusinessLongitude = new String [BusinessLongs.size()];
                            final String []MyBusinessLatitude = new String [BusinessLats.size()];
                            final String []MyEmail = new String [emails.size()];
                            final String []MyDoc = new String [spaIds.size()];
                            final String []MyPromo = new String [Promos.size()];
                            final String []MyLong = new String [Longitudes.size()];
                            final String []MyLat = new String [Latitudes.size()];
                            final String []MyTown = new String [Towns.size()];
                            final String []MyProvinces = new String [Provinces.size()];
                            for (int i = 0; i < spas.size(); i++) {
                                MySpa[i] = spas.get(i);
                                MyLoc[i] = shortLocs.get(i);
                                MyImage[i] = images.get(i);
                                MyPhone[i] = phones.get(i);
                                MyBusinessLongitude[i] = BusinessLongs.get(i);
                                MyBusinessLatitude[i] = BusinessLats.get(i);
                                MyEmail[i] = emails.get(i);
                                MyDoc[i] = spaIds.get(i);
                                MyPromo[i] = Promos.get(i);
                                MyLong[i] = Longitudes.get(i);
                                MyLat[i] = Latitudes.get(i);
                                MyTown[i] = Towns.get(i);
                                MyProvinces[i] = Provinces.get(i);
                            }
                            spalistAdapter = new spaListAdapter(activity);
                            spalistAdapter.setSpa(MySpa);
                            spalistAdapter.setLoc(MyLoc);
                            spalistAdapter.setImage(MyImage);
                            spalistAdapter.setPhone(MyPhone);
                            spalistAdapter.setBusinessLong(MyBusinessLongitude);
                            spalistAdapter.setBusinessLat(MyBusinessLatitude);
                            spalistAdapter.setEmail(MyEmail);
                            spalistAdapter.setDocument(MyDoc);
                            spalistAdapter.setPromo(MyPromo);
                            spalistAdapter.setLong(MyLong);
                            spalistAdapter.setLat(MyLat);
                            spalistAdapter.setTown(MyTown);
                            spalistAdapter.setProvince(MyProvinces);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(spalistAdapter);
                            //add Towns to arrayList
                            List<String> townList = new ArrayList<String>();
                            List<String> provinceList = new ArrayList<String>();
                            for (int i = 0; i < MyTown.length; i++) {
                                townList.add(MyTown[i]);
                                provinceList.add(MyProvinces[i]);
                                townList.add(MyProvinces[i]);
                            }
                            Collections.sort(townList);
                            Collections.sort(provinceList);
                            //set filtered town list
                            Set<String> Townset = new HashSet<>(townList);
                            townList.clear();
                            townList.addAll(Townset);
                            //set filtered Province list
                            Set<String> Provinceset = new HashSet<>(provinceList);
                            provinceList.clear();
                            provinceList.addAll(Provinceset);
                            //add Town to autocomplete array
                            accessKeys.TownValues = new String[townList.size()];
                            int townCounter = 0;
                            for (String str : townList) {
                                accessKeys.TownValues[townCounter] = str;
                                townCounter++;
                            }
                            //add province to properties
                            accessKeys.ProvinceValues = new String[provinceList.size()];
                            int provinceCounter = 0;
                            for (String str : provinceList) {
                                accessKeys.ProvinceValues[provinceCounter] = str;
                                provinceCounter++;
                            }
                            //add all the spas
                            accessKeys.spaValues = new String[MySpa.length];
                            int spaCounter = 0;
                            for (String str : MySpa) {
                                accessKeys.spaValues[spaCounter] = str;
                                spaCounter++;
                            }
                            //newArrayList with spas, provinces, and towns
                            List<String> allItems = new ArrayList<String>();
                            Collections.addAll(allItems,accessKeys.spaValues);
                            //add town & spa values
                            //sort Spa values
                            Set<String> SpaList = new HashSet<>(allItems);
                            //set data to autocomplete
                            List<String> spaItems = new ArrayList<String>();
                            spaItems.addAll(SpaList);
                            Collections.addAll(spaItems,accessKeys.TownValues);
                            main.adapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, spaItems);
                            main.Search.setThreshold(6);
                            main.Search.setAdapter(main.adapter);
                            //close progressBar
                            progressBar.setVisibility(View.GONE);
                            //close imageview
                            imageView.setVisibility(View.GONE);
                            //view search layout
                            main.SearchLayout.setVisibility(View.VISIBLE);
                            //view viewInformation icon
                            main.viewInfo.setVisibility(View.VISIBLE);
                            //begin ratings & survey
                            ratings.checkRatings(activity);
                        }
                    }
                });
    }

    //get all documents from firestore
    public static  void getFirestoreSpaDetails(final Activity activity, final androidx.recyclerview.widget.RecyclerView recyclerView, final ProgressBar progressBar, final ImageView imageView){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(constants.spa).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        dbs.collection(constants.spa)
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
                            getSpaDetails(activity,recyclerView,progressBar,imageView);
                            //getIssues(activity,context, view, recyclerView); //ammended
                        }
                    }
                });
    }
}

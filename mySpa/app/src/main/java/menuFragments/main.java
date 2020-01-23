package menuFragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import constants.constants;
import io.eyec.bombo.myspa.MainActivity;
import io.eyec.bombo.myspa.R;
import properties.accessKeys;

import static io.eyec.bombo.myspa.MainActivity.setFragmentTag;
import static properties.accessKeys.setExitApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class main extends android.app.Fragment implements View.OnClickListener, View.OnTouchListener{
    View myview;
    public static RecyclerView recyclerView;
    public static ProgressBar progressBar;
    public static ImageView imageView;
    public static ImageView viewInfo;
    public static ConstraintLayout MainInfo;
    public static CardView Email;
    public static CardView SearchLayout;
    public static LocationManager locationManager;
    public static  LocationListener locationListener;
    private double longitute;
    private double latitute;
    private boolean isLocationGrabbed;
    ImageView SearchButton;
    public static AutoCompleteTextView Search;
    public static ArrayAdapter<String> adapter;
    static String fragmentTag = null;
    public static final String TAG = "main";

    public main() {
        // Required empty public constructor
    }
    public static main newInstance() {
        //main fragment = new main();

        return new main();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_main, container, false);
        isLocationGrabbed = false;
        recyclerView = myview.findViewById(R.id.recycler_main);
        progressBar = myview.findViewById(R.id.progress);
        imageView = myview.findViewById(R.id.logo);
        viewInfo = myview.findViewById(R.id.display);
        MainInfo = myview.findViewById(R.id.displayMain);
        SearchLayout  = myview.findViewById(R.id.MySearch);
        Email  = myview.findViewById(R.id.Email);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        SearchButton = myview.findViewById(R.id.searchButton);
        Search = myview.findViewById(R.id.search);
        Search.setVisibility(View.GONE);
        Search.setOnClickListener(this);
        SearchButton.setOnClickListener(this);
        viewInfo.setOnClickListener(this);
        Email.setOnClickListener(this);
        recyclerView.setOnTouchListener(this);
        //set Date to autocomplete
        if(accessKeys.TownValues != null) {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, accessKeys.TownValues);
            Search.setThreshold(6);
            Search.setAdapter(adapter);
        }
        //if Location is already found
        if(accessKeys.getLongitude()!= null && accessKeys.getLatitude()!=null){
            connectionHandler.external.spas_.getAllDocuments(getActivity(), recyclerView, progressBar,imageView);
        }
        //handle selected items
        Search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
                System.out.println("Selection is " + selection);
                boolean isProvince = false;
                for (String str: accessKeys.ProvinceValues){
                    if(selection.equalsIgnoreCase(str))
                        isProvince = true;
                }
                spaSearch.setIsProvince(isProvince);
                spaSearch.setProvince(selection);
                methods.globalMethods.loadFragmentWithTag(R.id.main, new spaSearch(), getActivity(),spaSearch.fragmentTag);
                //connectionHandler.external.spasSearch_.getAllDocuments(getActivity(), recyclerView,isProvince,selection);
                Search.getText().clear();
                //methods.globalMethods.loadFragments(R.id.main, new notes(),activity);
                //System.out.println(selection);
                //System.out.println(MyDocument[position]);
            }
        });
        locationListener  = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                //if(!isLocationGrabbed) {
                boolean LocationEmpty = false;
                if(accessKeys.getLongitude()== null && accessKeys.getLatitude()==null) {
                    LocationEmpty = true;
                }
                System.out.println(location.getLatitude()+ ": "+location.getLongitude());
                accessKeys.setLongitude(String.valueOf(location.getLongitude()));
                accessKeys.setLatitude(String.valueOf(location.getLatitude()));
                if (LocationEmpty)
                    connectionHandler.external.spas_.getAllDocuments(getActivity(), recyclerView, progressBar,imageView);
                //    isLocationGrabbed = true;
                //}
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
        //isLocationGrabbed = false;
        getLocationPermission();
        //connectionHandler.external.spas_.getAllDocuments(getActivity(), recyclerView);
        setFragmentTag(fragmentTag);
        setExitApplication(true);
        return myview;
    }

    public void getLocationPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.permissionfor = constants.locationSpas;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "You need these permissions to optimally use this app.", Toast.LENGTH_LONG).show();
                }
            });
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            //connectionHandler.external.spas_.getAllDocuments(getActivity(), recyclerView);
        }

    }

    //check if permission is allowed
    public boolean getSecondaryPermissionsd(){
        boolean result;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.permissionfor = constants.locationSpas;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            result = false;
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            result = true;
            //connectionHandler.external.spas_.getAllDocuments(getActivity(), recyclerView);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.searchButton){
            hideContactInfo();
            Search.getText().clear();
            if(Search.getVisibility() == View.VISIBLE){
                Search.setVisibility(View.GONE);
                SearchButton.setImageResource(R.drawable.ic_search_black_24dp);
            }else{
                Search.setVisibility(View.VISIBLE);
                SearchButton.setImageResource(R.drawable.back);
                Search.requestFocus();
            }
        }else if(id == R.id.display){
            if(MainInfo.getVisibility() == View.VISIBLE){
                hideContactInfo();
            }else{
                viewContactInfo();
            }
            Search.setVisibility(View.GONE);
            SearchButton.setImageResource(R.drawable.ic_search_black_24dp);
        }else if(id == R.id.Email) {
            hideContactInfo();
            String emailValue = getString(R.string.email);
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailValue});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Enquiry");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear "+constants.AppName+" team, \n\n");
            getActivity().startActivity(emailIntent);

        }


        /*else{
            if(accessKeys.TownValues.length > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, accessKeys.TownValues);
                Search.setThreshold(6);
                Search.setAdapter(adapter);
                //handle selected items
                Search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String selection = (String) parent.getItemAtPosition(position);
                        System.out.println("Selection is " + selection);
                        //methods.globalMethods.loadFragments(R.id.main, new notes(),activity);
                        //System.out.println(selection);
                        //System.out.println(MyDocument[position]);
                    }
                });
            }

        }*/
    }
    //view main contact message
    private void viewContactInfo(){
        MainInfo.setVisibility(View.VISIBLE);
        viewInfo.setImageResource(R.drawable.retract);

    }
    //hide main contact message
    private void hideContactInfo(){
        MainInfo.setVisibility(View.GONE);
        viewInfo.setImageResource(R.drawable.expand);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        ((AppCompatActivity)getActivity()).setTitle(getActivity().getTitle());
        super.onDetach();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(MainInfo.getVisibility() == View.VISIBLE){
            hideContactInfo();
        }
        Search.setVisibility(View.GONE);
        SearchButton.setImageResource(R.drawable.ic_search_black_24dp);
        return false;
    }
}

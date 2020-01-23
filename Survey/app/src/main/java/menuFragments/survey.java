package menuFragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import authentication.auth;
import constants.constants;
import io.eyec.bombo.survey.MainActivity;
import io.eyec.bombo.survey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class survey extends android.app.Fragment implements View.OnClickListener {
    CardView Submit;
    EditText Answer;
    TextView Question;
    TextView questionNo;
    TextView Post;
    RadioButton Rad1;
    RadioButton Rad2;
    RadioButton Rad3;
    RadioButton Rad4;
    RadioButton Rad5;
    View myview;
    int TotalQuiz = 16;
    public static LocationManager locationManager;
    public static  LocationListener locationListener;
    private double longitute;
    private double latitute;
    private boolean isLocationGrabbed;


    public survey() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_survey, container, false);
        Submit = myview.findViewById(R.id.Submit);
        Answer = myview.findViewById(R.id.typed);
        Question = myview.findViewById(R.id.question);
        questionNo = myview.findViewById(R.id.question_no);
        Rad1 = myview.findViewById(R.id.rad0);
        Rad2 = myview.findViewById(R.id.rad1);
        Rad3 = myview.findViewById(R.id.rad2);
        Rad4 = myview.findViewById(R.id.rad3);
        Rad5 = myview.findViewById(R.id.rad4);
        Post = myview.findViewById(R.id.post);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Rad1.setChecked(true);
        Submit.setOnClickListener(this);
        if((quizInfo.getCount()) == TotalQuiz){
            Post.setText("Submit");
        }
        int resource = 0;
        switch (quizInfo.getCount()){
            case 0: resource = R.string.item1_Type;
                    break;
            case 1: resource = R.string.item2_Type;
                    break;
            case 2: resource = R.string.item3_Type;
                    break;
            case 3: resource = R.string.item4_Type;
                    break;
            case 4: resource = R.string.item5_Type;
                    break;
            case 5: resource = R.string.item6_Type;
                    break;
            case 6: resource = R.string.item7_Type;
                    break;
            case 7: resource = R.string.item8_Type;
                    break;
            case 8: resource = R.string.item9_Type;
                    break;
            case 9: resource = R.string.item10_Type;
                    break;
            case 10: resource = R.string.item11_Type;
                    break;
            case 11: resource = R.string.item12_Type;
                    break;
            case 12: resource = R.string.item13_Type;
                    break;
            case 13: resource = R.string.item14_Type;
                    break;
            case 14: resource = R.string.item15_Type;
                    break;
            case 15: resource = R.string.item16_Type;
                    break;
            case 16: resource = R.string.item17_Type;
                    break;

        }
        if(getActivity().getResources().getString(resource).equalsIgnoreCase("type")) {
            Answer.setInputType(InputType.TYPE_CLASS_TEXT);
            Answer.requestFocus();
            Rad1.setVisibility(View.GONE);
            Rad2.setVisibility(View.GONE);
            Rad3.setVisibility(View.GONE);
            Rad4.setVisibility(View.GONE);
            Rad5.setVisibility(View.GONE);
        }else if (getActivity().getResources().getString(resource).equalsIgnoreCase("typeInt")){
            Answer.setInputType(InputType.TYPE_CLASS_NUMBER);
            Answer.requestFocus();
            Rad1.setVisibility(View.GONE);
            Rad2.setVisibility(View.GONE);
            Rad3.setVisibility(View.GONE);
            Rad4.setVisibility(View.GONE);
            Rad5.setVisibility(View.GONE);
        }else if(getActivity().getResources().getString(resource).equalsIgnoreCase("genderOptions")){
                //Rad1.setVisibility(View.GONE);
                //Rad2.setVisibility(View.GONE);
            Rad3.setVisibility(View.GONE);
            Rad4.setVisibility(View.GONE);
            Rad5.setVisibility(View.GONE);
            Answer.setVisibility(View.GONE);
            Rad1.setText("Male");
            Rad2.setText("Female");
        }else if(getActivity().getResources().getString(resource).equalsIgnoreCase("YesNooptions")){
                //Rad1.setVisibility(View.GONE);
                //Rad2.setVisibility(View.GONE);
            Rad3.setVisibility(View.GONE);
            Rad4.setVisibility(View.GONE);
            Rad5.setVisibility(View.GONE);
            Answer.setVisibility(View.GONE);
            Rad1.setText("Yes");
            Rad2.setText("No");
        }else{
            Answer.setVisibility(View.GONE);
            Rad1.setText("Own Transport");
            Rad2.setText("Taxis");
            Rad3.setText("Bus");
            Rad4.setText("Walking");
            Rad5.setText("Lift Club");
        }
        if (quizInfo.getCount()==0){
            int total =  quizInfo.getCount() + 1;
            questionNo.setText(String.valueOf(total));
            Question.setText(getActivity().getResources().getString(R.string.item1));
        }else{
            int total =  quizInfo.getCount() + 1;
            switch (quizInfo.getCount()){
                case 1: questionNo.setText(String.valueOf(total));
                       Question.setText(getActivity().getResources().getString(R.string.item2));
                       break;
                case 2: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item3));
                    break;
                case 3: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item4));
                    break;
                case 4: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item5));
                    break;
                case 5: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item6));
                    break;
                case 6: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item7));
                    break;
                case 7: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item8));
                    break;
                case 8: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item9));
                    break;
                case 9: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item10));
                    break;
                case 10: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item11));
                    break;
                case 11: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item12));
                    break;
                case 12: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item13));
                    break;
                case 13: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item14));
                    break;
                case 14: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item15));
                    break;
                case 15: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item16));
                    break;
                case 16: questionNo.setText(String.valueOf(total));
                    Question.setText(getActivity().getResources().getString(R.string.item17));
                    break;
            }
        }

        locationListener  = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                if(!isLocationGrabbed) {
                    System.out.println(location.getLatitude()+ ": "+location.getLongitude());
                    quizInfo.answers.add(String.valueOf(location.getLongitude()));
                    quizInfo.answers.add(String.valueOf(location.getLatitude()));
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
        return myview;
    }

    @Override
    public void onClick(View view) {
        if (TotalQuiz == quizInfo.getCount()){
            String phoneNumber = Answer.getText().toString();
            quizInfo.answers.add(phoneNumber);
            getLocationPermission();
            auth.InitiateAuth(getActivity(), phoneNumber);
        }else {
            if (Answer.getVisibility() == View.VISIBLE) {
                if (Answer.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Kindly answer the question to proceed", Toast.LENGTH_SHORT).show();
                    Answer.requestFocus();
                } else {
                    int total = quizInfo.getCount() + 1;
                    quizInfo.setCount(total);
                    quizInfo.answers.add(Answer.getText().toString());
                    methods.globalMethods.loadFragmentsWithTag(R.id.main, new survey(), getActivity());
                }
            } else {
                String value = "";
                if (Rad1.isChecked()) {
                    value = Rad1.getText().toString();
                } else if (Rad2.isChecked()) {
                    value = Rad2.getText().toString();
                } else if (Rad3.isChecked()) {
                    value = Rad3.getText().toString();
                } else if (Rad4.isChecked()) {
                    value = Rad4.getText().toString();
                } else {
                    value = Rad5.getText().toString();
                }
                int total = quizInfo.getCount() + 1;
                quizInfo.setCount(total);
                quizInfo.answers.add(value);
                methods.globalMethods.loadFragmentsWithTag(R.id.main, new survey(), getActivity());

            }
        }


    }

    private void getLocationPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.permissionfor = constants.locationSurvey;
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
}

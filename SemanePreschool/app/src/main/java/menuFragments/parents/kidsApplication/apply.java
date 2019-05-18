package menuFragments.parents.kidsApplication;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.eyec.bombo.semanepreschool.main;

import static methods.globalMethods.getCameraPermissions;
import static methods.globalMethods.getReadWritePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class apply extends android.app.Fragment implements View.OnClickListener{
View myview;
static ImageView Child;
EditText Name;
EditText Surname;
RadioButton Male;
RadioButton Female;
EditText Idnumber;
EditText Addr1;
EditText Addr2;
EditText Addr3;
EditText Town;
EditText Code;
Spinner Province;
Spinner Grade;
RadioButton Yes;
RadioButton No;
EditText Allergy;
EditText MedicalCondition;
CardView Next;
CardView PreviousApplications;
static Activity activity;
static String getImage;
static boolean ImageSet;

    public static String getGetImage() {
        return getImage;
    }

    public static void setGetImage(String getImage) {
        apply.getImage = getImage;
    }

    public static void setImage(String image){
        Glide.with(activity).load(image).into(Child);
    }

    public static boolean isImageSet() {
        return ImageSet;
    }

    public static void setImageSet(boolean imageSet) {
        ImageSet = imageSet;
    }

    public apply() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_apply, container, false);
        activity = getActivity();
        Child = myview.findViewById(R.id.childImage);
        Name = myview.findViewById(R.id.childname);
        Surname = myview.findViewById(R.id.childsurname);
        Male = myview.findViewById(R.id.male);
        Female = myview.findViewById(R.id.female);
        Idnumber = myview.findViewById(R.id.idnum);
        Addr1 = myview.findViewById(R.id.AddressLine1);
        Addr2 = myview.findViewById(R.id.AddressLine2);
        Addr3 = myview.findViewById(R.id.AddressLine3);
        Town = myview.findViewById(R.id.City);
        Code  = myview.findViewById(R.id.Code);
        Province  = myview.findViewById(R.id.Province);
        Grade  = myview.findViewById(R.id.grades);
        Yes  = myview.findViewById(R.id.yes);
        No  = myview.findViewById(R.id.no);
        Allergy = myview.findViewById(R.id.specifyAllergy);
        MedicalCondition = myview.findViewById(R.id.specify);
        Next = myview.findViewById(R.id.Next);
        PreviousApplications = myview.findViewById(R.id.MyApplications);
        String[] province = getActivity().getResources().getStringArray(R.array.Province);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, province);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Province.setAdapter(provinceAdapter);
        String[] grades = getActivity().getResources().getStringArray(R.array.grades);
        ArrayAdapter<String> gradesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, grades);
        gradesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Grade.setAdapter(gradesAdapter);
        Child.setOnClickListener(this);
        Next.setOnClickListener(this);
        PreviousApplications.setOnClickListener(this);
        Male.setChecked(true);
        Yes.setChecked(true);
        track_Allergy();
        return myview;
    }

    private  void track_Allergy() {
        Yes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Allergy.setEnabled(true);
                return false;
            }
        });
        No.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Allergy.setEnabled(false);
                return false;
            }
        });
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
                main.capturePic = true;
                main.permissionfor = constants.camera;
                getCameraPermissions(Myactivity);
            }
        });
        dialogfromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                main.selectPic = true;
                main.permissionfor = constants.choosefile;
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
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.Next) {
            String name = Name.getText().toString();
            String surname = Surname.getText().toString();
            String gender = "";
            String IdNumber = Idnumber.getText().toString();
            String newAddress1 = Addr1.getText().toString();
            String newAddress2 = Addr2.getText().toString();
            String newAddress3 = Addr3.getText().toString();
            String newTown = Town.getText().toString();
            String newCode = Code.getText().toString();
            String newProvince = Province.getSelectedItem().toString();
            String newGrades = Grade.getSelectedItem().toString();
            String allergic = "";
            String allergyReason = Allergy.getText().toString();
            String medicalCondition = MedicalCondition.getText().toString();
            if(!isImageSet()){
                Toast.makeText(getActivity(), "Child ID photo is requred", Toast.LENGTH_SHORT).show();
                Child.requestFocus();
            }else if(name.isEmpty() || name.length()<3) {
                Name.requestFocus();
                Toast.makeText(getActivity(), "enter full name", Toast.LENGTH_SHORT).show();
            }else if(surname.isEmpty() || surname.length()<3) {
                Surname.requestFocus();
                Toast.makeText(getActivity(), "enter full surname", Toast.LENGTH_SHORT).show();
            }else if(IdNumber.isEmpty() || IdNumber.length()<13){
                Idnumber.requestFocus();
                Toast.makeText(getActivity(), "enter a valid ID number", Toast.LENGTH_SHORT).show();
            }else if(newAddress1.isEmpty()) {
                Addr1.requestFocus();
                Toast.makeText(getActivity(), "enter address here", Toast.LENGTH_SHORT).show();
            }else if(newTown.isEmpty()) {
                Town.requestFocus();
                Toast.makeText(getActivity(), "enter address town here", Toast.LENGTH_SHORT).show();
            }else if(newCode.isEmpty() || newCode.length()<4) {
                Code.requestFocus();
                Toast.makeText(getActivity(), "enter address code here", Toast.LENGTH_SHORT).show();
            }else if(newProvince.isEmpty() || newProvince.equalsIgnoreCase("-- select one --")) {
                Province.requestFocus();
                Toast.makeText(getActivity(), "please select a province", Toast.LENGTH_SHORT).show();
            }else if(newGrades.isEmpty() || newGrades.equalsIgnoreCase("-- select one --")) {
                Grade.requestFocus();
                Toast.makeText(getActivity(), "please select a grade", Toast.LENGTH_SHORT).show();
            }else if(medicalCondition.isEmpty()) {
                MedicalCondition.requestFocus();
                Toast.makeText(getActivity(), "please specify any medical conditions the child has", Toast.LENGTH_SHORT).show();
            }else{
                boolean allFine = true;
                if (Male.isChecked()){
                    gender = "Male";
                }else{
                    gender = "Female";
                }
                if(Yes.isChecked()){
                    allergic = "Yes";
                    if(allergyReason.isEmpty()){
                        Allergy.requestFocus();
                        Toast.makeText(getActivity(), "please indicate what the child is allergic to", Toast.LENGTH_SHORT).show();
                        allFine = false;
                    }
                }else{
                    allergic = "no";
                    allergyReason = "none allergic";
                    allFine = true;
                }
                if(allFine){
                    applicationInfo.setChild_Image(getGetImage());
                    applicationInfo.setChild_Name(name);
                    applicationInfo.setChild_Surname(surname);
                    applicationInfo.setChild_child_Gender(gender);
                    applicationInfo.setChild_Idnumber(IdNumber);
                    applicationInfo.setChild_Grade(newGrades);
                    applicationInfo.setChild_Addr1(newAddress1);
                    applicationInfo.setChild_Addr2(newAddress2);
                    applicationInfo.setChild_Addr3(newAddress3);
                    applicationInfo.setChild_Town(newTown);
                    applicationInfo.setChild_Code(newCode);
                    applicationInfo.setChild_Province(newProvince);
                    applicationInfo.setChild_isAllergic(allergic);
                    applicationInfo.setChild_AllergyDescription(allergyReason);
                    applicationInfo.setChild_MedicalCondition(medicalCondition);
                    methods.globalMethods.loadFragments(R.id.main, new applyFinal(), getActivity());
                }
            }

        }else if (id == R.id.childImage){
            Toast.makeText(getActivity(), "Only proper child Id photos will be accepted!", Toast.LENGTH_LONG).show();
            InitiateCamera(getActivity());
            main.className = "apply";
        }else{
            methods.globalMethods.loadFragments(R.id.main, new allApplications(), getActivity());
        }
    }

}

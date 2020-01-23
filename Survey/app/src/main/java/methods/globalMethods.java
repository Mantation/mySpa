package methods;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.eyec.bombo.survey.R;

public class globalMethods {

    //checks network availability
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Handles loading of a fragment in an activity
    public static void loadFragments(int fragment,android.app.Fragment newFragment, Context context) {
        //FragmentManager fragmentManager = getFragmentManager();
        try {
            FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragment, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }catch (Exception e){
            //Logging.Logerror(e.getMessage());
        }
    }
    //Handles loading of a fragment in an activity
    public static void loadFragmentsWithTag(int fragment,android.app.Fragment newFragment, Context context) {
        //FragmentManager fragmentManager = getFragmentManager();
        try {
            FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragment, newFragment,"main");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }catch (Exception e){
            //Logging.Logerror(e.getMessage());
        }
    }

    //empties a list
    public static void clearList(List list){
        final int i = 0;
        if (list.size() > 0 ){
            for(;list.size()>0;){
                list.remove(list.size()-1);
            }
            list.clear();
        }
    }

    //Initialize first letter
    public static String InitializeFirstLetter(String text){
        String FirstLetter = text.substring(0,1).toUpperCase();
        String OtherLetters = text.substring(1,text.length()).toLowerCase();
        return FirstLetter + OtherLetters;
    }

    //run toast on UI threat
    public static void runSuccessfulToast(final Activity activity, final String DocuName){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, DocuName + " file Successfully downloaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Toast network connectivity
    public static void networkerror(Activity activity){
        Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }


    //Date
    public static String ToDate(){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd", Locale.UK);
        Date now = new Date();
        String Today = dateformat.format(now);
        return Today;

    }
    //Time
    public static String Time(){
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        Date now = new Date();
        String Today = dateformat.format(now);
        return Today;
    }

    //Device Name
    public static String Device(){
        return Build.MODEL;
    }

    //get image from phone
    public static Intent getFileChooserIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    //launch camera
    public static Intent getCameraChooserIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*String FileName = "IMG_"+ToDate().replace("/","-")+Time().replace(":","_")+".jpg";
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + constants.AppName + "/" + constants.forumImages);
        File photo = new File(folder,  FileName);
        if (!folder.exists()) {
            folder.mkdirs();
                        /*CopyOption[] options = new CopyOption[]{
                                StandardCopyOption.COPY_ATTRIBUTES,
                                StandardCopyOption.COPY_ATTRIBUTES
                        };
        }
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,
                //Uri.fromFile(photo));
        //        Uri.fromFile(new File(folder.getPath()+"/"+FileName)));
        cameraPic = Uri.fromFile(photo);
        Dashboard.MyFile = folder.getPath()+"/"+FileName;*/
        return intent;
    }
    //chat date format

    public static String chatDate(String date){
        String result = "";
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss", Locale.ENGLISH);
            Date now = new Date();
            String Today = dateformat.format(now);
            Date myToday = dateformat.parse(Today);
            String[] Time = date.split(" ");
            String[] newDate = Time[0].split("/");
            String Date = newDate[2] + "-" + newDate[1] + "-" + newDate[0] + " " + Time[1];
            Date myBefore = dateformat.parse(Date);
            String Before = dateformat.format(myBefore);

            Date firstDate = dateformat.parse(Today);
            Date secondDate = dateformat.parse(Before);

            long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            int day = (int) diff;
            //Days before yesterday
            SimpleDateFormat Myformat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);

            switch (day) {
                case 0:
                    result = "Today".toUpperCase();
                    break;
                case 1:
                    result = "Yesterday".toUpperCase();
                    break;
                case 2:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                case 3:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                case 4:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                case 5:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                case 6:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                case 7:
                    result = CheckDayOfTheWeek(Time[0]);
                    break;
                default:
                    Date myDate = dateformat.parse(newDate[2] + "-" + newDate[1] + "-" + newDate[0] + " " + Time[1]);
                    String dd = Myformat.format(myDate);
                    result = dd.replace("-", " ");
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;


    }

    public static String CheckDayOfTheWeek(String date){
        String []myDate = date.split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.valueOf(myDate[0]), (Integer.valueOf(myDate[1]) - 1), Integer.valueOf(myDate[2]));
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static boolean stopProgress;
    public static final Handler handler = new Handler();
    final static int delay = 500; //milliseconds
    public static Runnable myRunnable;
    public static Dialog Mydialog;

    public static void ShowDialog(final Activity activity){
        try{
            if(Mydialog == null ){
                stopProgress = false;
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.custom_progress);
                dialog.setCancelable(false);
                pl.droidsonroids.gif.GifImageView gifImageView = (pl.droidsonroids.gif.GifImageView) dialog.findViewById(R.id.imageview);
                dialog.show();
                Mydialog = dialog;
                MonitorProgress();
            }

        }catch (Exception e){

        }


    }

    //Animations
    //hide view from right to left
    public static void hideView(final View view, final Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_right);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(animation);
    }

    //show view from left to right
    public static void showView(final View view, final Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(animation);
    }

    //hide view from left to right
    public static void rehideView(final View view, final Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(animation);
    }

    //show view from right to left
    public static void reshowView(final View view, final Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(animation);
    }

    public static void MonitorProgress(){
        handler.postDelayed(new Runnable(){
            public void run(){
                if(!stopProgress){
                    System.out.println("Monitor progress");
                    myRunnable = this;
                }else{
                    if(Mydialog !=null && stopProgress) {
                        Mydialog.dismiss();
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacksAndMessages(null);
                        Mydialog = null;
                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    //get Resd/write permissions from user
    public static void getCameraPermissions(Activity activity) {
        //permissions read/write external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,},
                    1);
        }
    }

    //permissions read/write external storage
    public static void getReadWritePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    //check if ArrayList contains a value
    public static boolean isAvailable(final List arrayList, final String textValue){
        boolean results = false;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).toString().equals(textValue)){
                results = true;
                break;
            }
        }
        return results;
    }

    //Displays when the user choose no option
    public static void ConfirmResolution(View view,String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }









}

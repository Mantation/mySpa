package menuFragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.internal.http.RetryableSink;

import authentication.stayLoggedIn;
import io.eyec.bombo.myspa.R;
import methods.globalMethods;

import static io.eyec.bombo.myspa.MainActivity.setFragmentTag;
import static properties.accessKeys.setExitApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class noConnection extends android.app.Fragment implements View.OnClickListener{
    View myview;
    CardView Retry;
    TextView Reconnect;
    TextView Info;
    ProgressBar progressBar;
    static String fragmentTag = null;


    public noConnection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_no_connection, container, false);
        Retry = myview.findViewById(R.id.Retry);
        Reconnect = myview.findViewById(R.id.reconnect);
        progressBar = myview.findViewById(R.id.progress);
        Info = myview.findViewById(R.id.reconnect);
        //Retry.setVisibility(View.GONE);
        Retry.setOnClickListener(this);
        runTimerProgress(getActivity());
        setFragmentTag(fragmentTag);
        setExitApplication(true);
        isAvailable=false;
        return myview;
    }

    @Override
    public void onClick(View v) {
        runTimerProgress(getActivity());
    }

    boolean isAvailable;
    private void runTimerProgress(final Activity activity){
        isAvailable = false;
        progressBar.setVisibility(View.VISIBLE);
        Info.setText("Reconnecting...");
        Retry.setVisibility(View.GONE);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (globalMethods.isNetworkAvailable(activity)) {
                    isAvailable = true;
                    onFinish();
                    this.cancel();
                }
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if(isAvailable){
                    stayLoggedIn.networkRecoveryAttempt = true;
                    stayLoggedIn.context = getActivity();
                    stayLoggedIn stayloggedIn = new stayLoggedIn();
                    stayloggedIn.onCreate();
                    getActivity().finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Info.setText("Network unavailable!");
                    Retry.setVisibility(View.VISIBLE);
                }
            }

        }.start();
    }
}

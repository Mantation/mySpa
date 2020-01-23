package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.survey.R;
import menuFragments.maintenance;
import menuFragments.start;
import menuFragments.startPipe;

import static android.support.constraint.Constraints.TAG;
import static methods.globalMethods.ConfirmResolution;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

public class PipesAdapter extends RecyclerView.Adapter <tasksViewHolder>{
    Context context;
    static Activity activity;
    private String[] status;
    private String[] Image;
    private String[] date;
    private String[] time;
    private String[] DocumentRef;
    private String[] description;
    private String[] pauses;
    private String [] phases;
    private String[] jobtype;

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String[] getImage() {
        return Image;
    }

    public void setImage(String[] image) {
        Image = image;
    }

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public String[] getDocumentRef() {
        return DocumentRef;
    }

    public void setDocumentRef(String[] documentRef) {
        DocumentRef = documentRef;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String[] getPauses() {
        return pauses;
    }

    public void setPauses(String[] pauses) {
        this.pauses = pauses;
    }

    public String[] getPhases() {
        return phases;
    }

    public void setPhases(String[] phases) {
        this.phases = phases;
    }

    public String[] getJobtype() {
        return jobtype;
    }

    public void setJobtype(String[] jobtype) {
        this.jobtype = jobtype;
    }

    public PipesAdapter(Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public tasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks, parent, false);
        return new tasksViewHolder(view);
    }

    String Date = "";
    boolean doubleClicked = false;
    @Override
    public void onBindViewHolder(final @NonNull tasksViewHolder holder, int position) {
        if (!Date.equalsIgnoreCase(date[position])) {
            Date = date[position];
            holder.date.setText(date[position]);
            holder.date.setVisibility(View.VISIBLE);
            holder.myview.setVisibility(View.VISIBLE);
        } else {
            holder.myview.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
        }
        final int maxSubject = 200;
        final int lengthMessageSubject = description[position].length();
        if(status[position].equalsIgnoreCase("open")){
            holder.MainLayout.setBackgroundResource(R.drawable.layout_edges_light);
        }else {
            holder.MainLayout.setBackgroundResource(R.drawable.layout_edges_dark);
            holder.description.setTextColor(Color.WHITE);
            holder.document.setTextColor(Color.WHITE);
        }
        if (lengthMessageSubject > maxSubject) {
            holder.description.setText(description[position].substring(0, 180) + "...");
        } else {
            holder.description.setText(description[position]);
        }

        holder.document.setText("Work id :- " + DocumentRef[position]);
        holder.time.setText(time[position]);
        Glide.with(activity).load(Image[position]).into(holder.image);
        holder.MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();

                    //start.setMyImage(Image[position]);
                    startPipe.setDocumentId(DocumentRef[position]);
                    startPipe.setJobType(jobtype[position]);
                    startPipe.setPhase(Integer.parseInt(phases[position]));
                    methods.globalMethods.loadFragments(R.id.main, new startPipe(), activity);
                //menuFragments.previewGlobalMessage.setBody(body[position]);
                //menuFragments.previewGlobalMessage.setSubject(subject[position]);
                //methods.globalMethods.loadFragments(R.id.main, new previewGlobalMessage(), context);
                //methods.globalMethods.loadFragments(R.id.main, new previewGlobalMessage(), activity);

            }
        });
        holder.MainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                String DocRef = DocumentRef[position];
                String Status = status[position];
                String Pause = pauses[position];
                updateReadResults(DocRef,v,Status,Pause);
                return false;
            }
        });
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return status.length;
    }

    static String newStatus;
    private static void updateReadResults(final String documnetRef, final View view, final String status,final String pauses) {

        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            if(status.equalsIgnoreCase("open")){
                user.put("status", "paused");
                newStatus = "paused";
                if(pauses.equalsIgnoreCase("unavailable")){
                    user.put("pause_0", "0~"+ToDate()+" "+Time());
                }else{
                    String []index = pauses.split("~");
                    int total = Integer.parseInt(index[0])+1;
                    user.put("pause_"+total,total+"~"+ToDate()+" "+Time());
                }
            }else{
                user.put("status", "open");
                newStatus = "open";
                if(pauses.equalsIgnoreCase("unavailable")){
                    user.put("reopen_0", "0~"+ToDate()+" "+Time());
                }else{
                    String []index = pauses.split("~");
                    if(Integer.parseInt(index[0])==0){
                        user.put("reopen_0","0~"+ToDate()+" "+Time());
                    }else {
                        int total = Integer.parseInt(index[0]);
                        user.put("reopen_" + total, total + "~" + ToDate() + " " + Time());
                    }
                }
            }
            CollectionReference collectionReference = db.collection(constants.maintenance);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "success adding document");
                        methods.globalMethods.loadFragmentsWithTag(R.id.main, new maintenance(), activity);
                        ConfirmResolution(view,"Work item is now "+newStatus);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);

                }
            });
        } catch (Exception exception) {
            exception.getMessage();
            exception.printStackTrace();

        }

    }



}

class tasksViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout MainLayout;
    public View myview;
    public TextView date;
    public TextView time;
    public TextView document;
    public ImageView image;
    public TextView description;

    public tasksViewHolder(View view) {
        super(view);
        MainLayout = (RelativeLayout)view.findViewById(R.id.message);
        myview = (View)view.findViewById(R.id.view);
        date = (TextView)view.findViewById(R.id.date);
        time = (TextView)view.findViewById(R.id.time);
        document = (TextView)view.findViewById(R.id.docRef);
        description = (TextView)view.findViewById(R.id.body);
        image = (ImageView) view.findViewById(R.id.image);
    }
}

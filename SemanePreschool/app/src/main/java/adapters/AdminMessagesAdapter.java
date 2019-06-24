package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.semanepreschool.R;
import io.realm.Realm;
import menuFragments.previewGlobalMessage;

import static android.support.constraint.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;

public class AdminMessagesAdapter extends RecyclerView.Adapter  <AdminMessagesViewHolder> {

    Context context;
    Activity activity;
    static Realm realm;
    private String[] subject;
    private String[] body;
    private String[] date;
    private String[] time;
    private boolean[] isRead;
    private String[] Id;
    private String[] DocumentRef;

    public String[] getSubject() {
        return subject;
    }

    public void setSubject(String[] subject) {
        this.subject = subject;
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
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

    public boolean[] getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean[] isRead) {
        this.isRead = isRead;
    }

    public String[] getId() {
        return Id;
    }

    public void setId(String[] id) {
        Id = id;
    }

    public String[] getDocumentRef() {
        return DocumentRef;
    }

    public void setDocumentRef(String[] documentRef) {
        DocumentRef = documentRef;
    }

    public AdminMessagesAdapter(Activity activity) {
        this.context = context;
        this.activity = activity;
    }



    @NonNull
    @Override
    public AdminMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox, parent, false);
        return new AdminMessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull AdminMessagesViewHolder holder, int position) {
        final int maxSubject = 65;
        final int maxBody = 80;
        final int lengthMessageSubject = subject[position].length();
        final int lengthMessageBody = body[position].length();
        holder.MainLayout.setBackgroundResource(R.drawable.layout_edges_light);
        if(lengthMessageSubject > maxSubject){
            holder.subject.setText(subject[position].substring(0,60) + "...");
        }else{
            holder.subject.setText(subject[position]);
        }
        if(lengthMessageBody > maxBody){
            holder.body.setText(body[position].substring(0,75) + "...");
        }else {
            holder.body.setText(body[position]);
        }
        holder.date.setText(date[position] + "\n" + time[position]);
        holder.subject.setTextColor(Color.BLACK);
        holder.body.setTextColor(Color.BLACK);
        holder.MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                //if (!isRead[position])
                //    updateReadResults(DocumentRef[position]);
                menuFragments.previewGlobalMessage.setBody(body[position]);
                menuFragments.previewGlobalMessage.setSubject(subject[position]);
                methods.globalMethods.loadFragments(R.id.main, new previewGlobalMessage(), context);
                methods.globalMethods.loadFragments(R.id.main, new previewGlobalMessage(), activity);

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
        return subject.length;
    }


    private static void updateReadResults(final String documnetRef) {
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("read", true);
            CollectionReference collectionReference = db.collection(constants.message);
            collectionReference.document(documnetRef).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "success adding document");
                        Loginfo("Message successfully read!");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Logerror("unable to read user message, " + e.getMessage());

                }
            });
        } catch (Exception exception) {
            exception.getMessage();
            exception.printStackTrace();

        }

    }
}
class AdminMessagesViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout MainLayout;
    public TextView subject;
    public TextView body;
    public TextView date;

    public AdminMessagesViewHolder(View view) {
        super(view);
        MainLayout = (RelativeLayout)view.findViewById(R.id.message);
        subject = (TextView)view.findViewById(R.id.heading);
        body = (TextView)view.findViewById(R.id.body);
        date = (TextView)view.findViewById(R.id.dateTime);
    }
}


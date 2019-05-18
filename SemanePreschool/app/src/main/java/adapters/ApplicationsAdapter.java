package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import io.eyec.bombo.semanepreschool.R;
import methods.globalMethods;

public class ApplicationsAdapter extends RecyclerView.Adapter<applicationsViewHolder> {


    Context context;
    Activity activity;
    private String[] Name;
    private String[] Surname;
    private String[] Profile;
    private String[] Status;
    private String[] IDNumber;

    public String[] getName() {
        return Name;
    }

    public void setName(String[] name) {
        Name = name;
    }

    public String[] getSurname() {
        return Surname;
    }

    public void setSurname(String[] surname) {
        Surname = surname;
    }

    public String[] getProfile() {
        return Profile;
    }

    public void setProfile(String[] profile) {
        Profile = profile;
    }

    public String[] getStatus() {
        return Status;
    }

    public void setStatus(String[] status) {
        Status = status;
    }

    public String[] getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String[] IDNumber) {
        this.IDNumber = IDNumber;
    }

    public ApplicationsAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public applicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicationslist, parent, false);
        return new applicationsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final applicationsViewHolder holder, int position) {
        String myName = globalMethods.InitializeFirstLetter(Name[position])+" "+globalMethods.InitializeFirstLetter(Surname[position])+"\n"+IDNumber[position];
        holder.name.setText(myName);
        Glide.with(activity).load(Profile[position]).into(holder.propic);
        if(Status[position].equalsIgnoreCase("pending")){
            holder.status.setImageResource(R.drawable.process);
        }else if (Status[position].equalsIgnoreCase("rejected")){
            holder.status.setImageResource(R.drawable.decline);
        }else{
            holder.status.setImageResource(R.drawable.accept);
        }
    }

    @Override
    public int getItemCount() {
        return Name.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
        //return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return 1;
    }

}

class applicationsViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public ImageView status;
    public CircleImageView propic;

    public applicationsViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        propic = view.findViewById(R.id.profile);
    }
}


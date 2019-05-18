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

import io.eyec.bombo.semanepreschool.R;
import methods.globalMethods;

public class KidsAdapter extends RecyclerView.Adapter<kidsViewHolder> {
    Context context;
    Activity activity;
    private String[] Name;
    private String[] Surname;
    private String[] Profile;
    private String[] Status;

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

    public KidsAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public kidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.childlist, parent, false);
        return new kidsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final kidsViewHolder holder, int position) {
        String myName = globalMethods.InitializeFirstLetter(Name[position])+" "+globalMethods.InitializeFirstLetter(Surname[position])+"\n"+Profile[position];
        holder.name.setText(myName);
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

class kidsViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public ImageView status;

    public kidsViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
    }
}

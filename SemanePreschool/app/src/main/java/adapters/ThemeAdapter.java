package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.eyec.bombo.semanepreschool.R;

public class ThemeAdapter extends RecyclerView.Adapter<themeViewHolder> {

    Context context;
    Activity activity;
    private String[] name;
    private String[] description;

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public ThemeAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public themeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.termlist, parent, false);
        return new themeViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final themeViewHolder holder, int position) {
        holder.name.setText(name[position]);
        String []newDescription = description[position].split("\\\\n");
        String myDescription = "";
        for (String mydesc:newDescription) {
            myDescription += mydesc+"\n";
        }
        holder.desc.setText(myDescription);
    }

    @Override
    public int getItemCount() {
        return name.length;
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

class themeViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView desc;

    public themeViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        desc = view.findViewById(R.id.description);
    }
}



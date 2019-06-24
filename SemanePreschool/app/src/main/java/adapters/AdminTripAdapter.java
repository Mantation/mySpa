package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.Trips.previewTrip;
import menuFragments.parents.previewReport;
import methods.globalMethods;

public class AdminTripAdapter extends RecyclerView.Adapter<tripViewHolder> {

    Context context;
    Activity activity;
    private String[] topic;
    private String[] Attendants;
    private String[] Venue;
    private String[] Price;
    private String[] Date;
    private String[] Time;
    private String[] SubmissionTime;
    private String[] SubmissionDate;

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public String[] getAttendants() {
        return Attendants;
    }

    public void setAttendants(String[] attendants) {
        Attendants = attendants;
    }

    public String[] getVenue() {
        return Venue;
    }

    public void setVenue(String[] venue) {
        Venue = venue;
    }

    public String[] getPrice() {
        return Price;
    }

    public void setPrice(String[] price) {
        Price = price;
    }

    public String[] getDate() {
        return Date;
    }

    public void setDate(String[] date) {
        Date = date;
    }

    public String[] getTime() {
        return Time;
    }

    public void setTime(String[] time) {
        Time = time;
    }

    public String[] getSubmissionTime() {
        return SubmissionTime;
    }

    public void setSubmissionTime(String[] submissionTime) {
        SubmissionTime = submissionTime;
    }

    public String[] getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(String[] submissionDate) {
        SubmissionDate = submissionDate;
    }

    public AdminTripAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public tripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.triplist, parent, false);
        return new tripViewHolder(view);
    }


    String year = "";
    @Override
    public void onBindViewHolder(@NonNull final tripViewHolder holder, int position) {
        String []Year  = Date[position].split("/");
        if (!year.equalsIgnoreCase(Year[2])){
            year=Year[2];
            holder.year.setText(Year[2]);
            holder.year.setVisibility(View.VISIBLE);
            holder.Myview.setVisibility(View.VISIBLE);
        }else{
            holder.Myview.setVisibility(View.GONE);
            holder.year.setVisibility(View.GONE);
        }
        holder.heading.setText("Trip to :"+Venue[position]);
        holder.date.setText(Date[position] + "\n" + Time[position]);
        holder.body.setText(topic[position]);
        holder.MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                previewTrip.setHeading("Trip : "+topic[position]);
                String BodyBuilder = "Dear Parent,\n\nKindly be aware of this upcoming school trip:\n\n"+
                        "Type of Trip : "+topic[position]+
                        "\nDestination : "+Venue[position]+
                        "\nPrice : " +Price[position]+
                        "\nDate : "+Date[position]+
                        "\nTime : "+Time[position]+
                        "\n\nIt is highly recommended that all pupils affected by this trip should participate."+
                        "\n\nThe folowing are affected by this trip:"+
                        "\n"+ globalMethods.InitializeFirstLetter(Attendants[position]).replace("*","\n")+
                        "\n\nThis trip was initiated on the "+SubmissionDate[position]+" @ "+SubmissionTime[position];
                previewTrip.setBody(BodyBuilder);
                methods.globalMethods.loadFragments(R.id.main, new previewTrip(), activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return topic.length;
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


class tripViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout MainLayout;
    public TextView heading;
    public TextView body;
    public ImageView status;
    public TextView date;
    public TextView year;
    public View Myview;

    public tripViewHolder(View view) {
        super(view);
        MainLayout = (RelativeLayout)view.findViewById(R.id.message);
        heading = view.findViewById(R.id.heading);
        body = view.findViewById(R.id.body);
        status = view.findViewById(R.id.status);
        date = (TextView)view.findViewById(R.id.dateTime);
        year = (TextView)view.findViewById(R.id.year);
        Myview = view.findViewById(R.id.view);

    }
}


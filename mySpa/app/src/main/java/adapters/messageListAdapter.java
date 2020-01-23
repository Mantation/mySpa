package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helperClasses.MyLeadingMarginSpan;
import io.eyec.bombo.myspa.R;

import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;

public class messageListAdapter extends RecyclerView.Adapter<messagesViewHolder> {
    Activity activity;
    public String[] FromSender;
    public String[] Messages;
    public String[] Image;
    public String[] Dates;
    public String[] Times;
    RecyclerView mRecyclerView;

    public String[] getFromSender() {
        return FromSender;
    }

    public void setFromSender(String[] fromSender) {
        FromSender = fromSender;
    }

    public String[] getMessages() {
        return Messages;
    }

    public void setMessages(String[] messages) {
        Messages = messages;
    }

    public String[] getImage() {
        return Image;
    }

    public void setImage(String[] image) {
        Image = image;
    }

    public String[] getDate() {
        return Dates;
    }

    public void setDate(String[] date) {
        Dates = date;
    }

    public String[] getTime() {
        return Times;
    }

    public void setTime(String[] time) {
        Times = time;
    }

    public messageListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public messagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagelist, parent, false);
        mRecyclerView = (RecyclerView) parent;
        return new messagesViewHolder(view);
    }

    int defaultColor;
    @Override
    public void onBindViewHolder(@NonNull final messagesViewHolder holder, int position) {
        //Convert Date
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd", Locale.UK);
        SimpleDateFormat fullDateformat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.UK);
        Date sysDate = new Date(Dates[position]);
        String Today = dateformat.format(sysDate);
        Date fullDate = new Date(Today);
        String finalDate = fullDateformat.format(fullDate);
        if(position == 0){
            holder.dateTop.setText(finalDate);
        }else{
            if(!Dates[position - 1].equalsIgnoreCase(Dates[position])){
                holder.dateTop.setText(finalDate);
                holder.DateLayout.setVisibility(View.VISIBLE);
            }else{
                holder.DateLayout.setVisibility(View.GONE);
            }
        }

        if(FromSender[position].equalsIgnoreCase("sender")){
            if (position == 0)
                defaultColor = holder.senderDescription.getCurrentTextColor();
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.senderDescription.setText(Messages[position]);
            if(Times[position].equalsIgnoreCase("sending...")||Times[position].equalsIgnoreCase("Failed")){
                holder.senderTime.setText(Times[position]);
                holder.senderTime.setTextColor(Color.RED);
            }else{
                holder.senderTime.setText(Times[position].substring(0,5));
                holder.senderTime.setTextColor(defaultColor);
            }
            //hide receiver
            holder.receiverTime.setVisibility(View.GONE);
            holder.receiverLayout.setVisibility(View.GONE);
        }else{
            if (position == 0)
                defaultColor = holder.receiverDescription.getCurrentTextColor();
            Glide.with(activity).load(Image[position]).into(holder.senderImage);
            holder.receiverLayout.setVisibility(View.VISIBLE);
            //wrapping text around Image
            ViewGroup.LayoutParams params = holder.senderImage.getLayoutParams();
            final int leftMargin = params.width +10;
            SpannableString ss = new SpannableString(Messages[position]);
            ss.setSpan(new MyLeadingMarginSpan(5, leftMargin), 0, ss.length(), 0);
            holder.receiverDescription.setText(ss);


            //holder.receiverDescription.setText(Messages[position]);
            if(Times[position].equalsIgnoreCase("sending...")||Times[position].equalsIgnoreCase("Failed")){
                holder.receiverTime.setText(Times[position]);
                holder.receiverTime.setTextColor(Color.RED);
            }else{
                holder.receiverTime.setText(Times[position].substring(0,5));
                holder.receiverTime.setTextColor(defaultColor);
            }
            //hide sender
            holder.senderTime.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.GONE);
        }
        int Header = 0;
        if(holder.DateLayout.getVisibility() == View.GONE){
            ViewGroup.LayoutParams params = holder.DateLayout.getLayoutParams();
            Header += params.height;
        }
        if(holder.DateLayout.getVisibility() == View.GONE){
            ViewGroup.LayoutParams params = holder.DateLayout.getLayoutParams();
            Header += params.height;
        }
        if(holder.receiverLayout.getVisibility() == View.GONE){
            ViewGroup.LayoutParams params = holder.receiverLayout.getLayoutParams();
            Header += params.height + 30;
        }
        if(holder.senderLayout.getVisibility() == View.GONE){
            ViewGroup.LayoutParams params = holder.senderLayout.getLayoutParams();
            Header += params.height + 30;
        }
        //set Header Layout
        ViewGroup.LayoutParams params = holder.Header.getLayoutParams();
        int defaultHeight = params.height;
        params.height = defaultHeight - Header;
        params.width = holder.Header.getLayoutParams().width;
        holder.Header.setLayoutParams(params);
        holder.Header.invalidate();

    }
    boolean repositioned;

    @Override
    public int getItemCount() {
        return Messages.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
        //return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }
    
}

    class messagesViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout DateLayout;
        public TextView dateTop;
        public RelativeLayout receiverLayout;
        public TextView receiverDescription;
        public TextView receiverTime;
        public RelativeLayout senderLayout;
        public TextView senderDescription;
        public ImageView senderImage;
        public TextView senderTime;
        public RelativeLayout Header;

        public messagesViewHolder(View view) {
            super(view);
            DateLayout = view.findViewById(R.id.dateTitle);
            dateTop = view.findViewById(R.id.dateTop);
            receiverLayout = view.findViewById(R.id.receiver);
            senderLayout = view.findViewById(R.id.sender);
            receiverDescription = view.findViewById(R.id.r_description);
            senderDescription = view.findViewById(R.id.s_description);
            senderImage = view.findViewById(R.id.userImage);
            receiverTime = view.findViewById(R.id.r_time);
            senderTime = view.findViewById(R.id.s_time);
            Header = view.findViewById(R.id.header);
        }
}

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
import menuFragments.parents.previewReport;
import methods.globalMethods;

public class ReportAdapter extends RecyclerView.Adapter<reportViewHolder>  {


    Context context;
    Activity activity;
    private String[] Subject;
    private String[] Date;
    private String[] Time;
    private String[] Term;
    private String[] Pupil;
    private String[] Total;
    private String[] DocumentRef;

    public String[] getSubject() {
        return Subject;
    }

    public void setSubject(String[] subject) {
        Subject = subject;
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

    public String[] getTerm() {
        return Term;
    }

    public void setTerm(String[] term) {
        Term = term;
    }

    public String[] getPupil() {
        return Pupil;
    }

    public void setPupil(String[] pupil) {
        Pupil = pupil;
    }

    public String[] getTotal() {
        return Total;
    }

    public void setTotal(String[] total) {
        Total = total;
    }

    public String[] getDocumentRef() {
        return DocumentRef;
    }

    public void setDocumentRef(String[] documentRef) {
        DocumentRef = documentRef;
    }

    public ReportAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public reportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportlist, parent, false);
        return new reportViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final reportViewHolder holder, int position) {
        holder.heading.setText(Term[position]);
        holder.date.setText(Date[position] + "\n" + Time[position]);
        String allsubs = Subject[position].substring(0,Subject[position].length()-1);
        String []SubjectsList = allsubs.split("\\*");
        int total = 0;
        String grade ="";
        for (int i = 0; i < SubjectsList.length; i++) {
            String[] value = SubjectsList[i].split("-");
            total+=Integer.parseInt(value[1]);
        }
        double totalPerc = Double.parseDouble(Total[position])*100;
        DecimalFormat f = new DecimalFormat("#.00");
        final double Total = (total/totalPerc)*100;

        if (Total <=45){
            grade = "Not yet achieved";
        }else if(Total >=46 && Total < 49){
            grade = "Partial achievement";
        }else if(Total >=50 && Total < 59){
            grade = "Adequate achievement";
        }else if(Total >=60 && Total < 69){
                grade = "Grade requirements achieved";
        }else if(Total >=70 && Total < 79){
            grade = "Grade requirements exceeded";
        }else if (Total >=80 && Total < 84){
            grade ="Excellent achievement";
        }else{
            grade = "Outstanding achievement";
        }
        String PassFail = "";
        if(Total>50){
            holder.status.setImageResource(R.drawable.accept);
            holder.body.setText(f.format(Total) +"% : Pass \n"+grade);
            PassFail="pass";
        }else{
            holder.status.setImageResource(R.drawable.decline);
            holder.body.setText(f.format(Total) +"% : Fail \n"+grade);
            PassFail="fail";
        }
        final String result = PassFail;
        final String []Modules = SubjectsList;
        final String Overall = String.valueOf(Total);
        holder.MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                previewReport.setTerm(Term[position]);
                previewReport.setIdentity(Pupil[position]);
                previewReport.setStatus(result);
                previewReport.setSubjects(Modules);
                previewReport.setOverall(Overall);
                methods.globalMethods.loadFragments(R.id.main, new previewReport(), activity);



            }
        });

    }

    @Override
    public int getItemCount() {
        return Subject.length;
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

class reportViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout MainLayout;
    public TextView heading;
    public TextView body;
    public ImageView status;
    public TextView date;

    public reportViewHolder(View view) {
        super(view);
        MainLayout = (RelativeLayout)view.findViewById(R.id.message);
        heading = view.findViewById(R.id.heading);
        body = view.findViewById(R.id.body);
        status = view.findViewById(R.id.status);
        date = (TextView)view.findViewById(R.id.dateTime);
    }
}

package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import io.eyec.bombo.semanepreschool.R;
import menuFragments.Administration.Application.previewApplication;
import menuFragments.previewGlobalMessage;
import methods.globalMethods;

public class AdminApplicationsAdapter extends RecyclerView.Adapter <adminApplicationsHolder>{

    Context context;
    Activity activity;
    private String[] Image;
    private String[] ProofOfPayment;
    private String[] Profile;
    private String[] Date;
    private String[] Status;
    private String[] Name;
    private String[] IDNumber;
    private String[] Users;
    private String[] DocumentRef;
    private String[] Token;

    public String[] getImage() {
        return Image;
    }

    public void setImage(String[] image) {
        Image = image;
    }

    public String[] getProofOfPayment() {
        return ProofOfPayment;
    }

    public void setProofOfPayment(String[] proofOfPayment) {
        ProofOfPayment = proofOfPayment;
    }

    public String[] getDate() {
        return Date;
    }

    public void setDate(String[] date) {
        Date = date;
    }

    public String[] getUsers() {
        return Users;
    }

    public void setUsers(String[] users) {
        Users = users;
    }

    public String[] getName() {
        return Name;
    }

    public void setName(String[] name) {
        Name = name;
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

    public String[] getDocumentRef() {
        return DocumentRef;
    }

    public void setDocumentRef(String[] documentRef) {
        DocumentRef = documentRef;
    }

    public String[] getToken() {
        return Token;
    }

    public void setToken(String[] token) {
        Token = token;
    }

    public AdminApplicationsAdapter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public adminApplicationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicationslist, parent, false);
        return new adminApplicationsHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final adminApplicationsHolder holder, int position) {
        //String myName = globalMethods.InitializeFirstLetter(Name[position])+" "+globalMethods.InitializeFirstLetter(Surname[position])+"\n"+IDNumber[position];
        holder.name.setText(Name[position]+"\n"+IDNumber[position]);
        Glide.with(activity).load(Image[position]).into(holder.propic);
        if(Status[position].equalsIgnoreCase("pending")){
            holder.status.setImageResource(R.drawable.process);
        }else if (Status[position].equalsIgnoreCase("rejected")){
            holder.status.setImageResource(R.drawable.decline);
        }else{
            holder.status.setImageResource(R.drawable.accept);
        }

        holder.MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                previewApplication.setUserId((Users[position]));
                previewApplication.setProfile((Profile[position]));
                previewApplication.setProof((ProofOfPayment[position]));
                previewApplication.setImage((Image[position]));
                previewApplication.setStatus(Status[position]);
                previewApplication.setName(Name[position]);
                previewApplication.setDocumentRefference(DocumentRef[position]);
                previewApplication.setMessagingToken(Token[position]);
                methods.globalMethods.loadFragments(R.id.main, new previewApplication(), activity);

            }
        });


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

class adminApplicationsHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public ImageView status;
    public CircleImageView propic;
    public ConstraintLayout MainLayout;

    public adminApplicationsHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        propic = view.findViewById(R.id.profile);
        MainLayout = view.findViewById(R.id.main);
    }
}


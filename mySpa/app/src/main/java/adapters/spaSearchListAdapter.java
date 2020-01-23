package adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import constants.constants;
import io.eyec.bombo.myspa.MainActivity;
import io.eyec.bombo.myspa.R;
import menuFragments.messages;
import methods.globalMethods;
import properties.accessKeys;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static logHandler.Logging.Logerror;
import static logHandler.Logging.Loginfo;
import static methods.globalMethods.InitializeFirstLetter;
import static methods.globalMethods.Time;
import static methods.globalMethods.ToDate;
import static methods.globalMethods.loadFragmentWithTag;
import static methods.globalMethods.showFragmentTemp;

public class spaSearchListAdapter extends RecyclerView.Adapter<mainSearchViewHolder> {
    Activity activity;
    private String[] Spa;
    private String[] Loc;
    private String[] Image;
    private String[] BusinessLong;
    private String[] BusinessLat;
    private String[] Phone;
    private String[] Email;
    private String[] Document;
    private String[] Promo;
    private String[] Long;
    private String[] Lat;
    private String[] Town;
    private String[] Province;
    private int MinHeight = 500;
    private int MaxHeight = 800;
    RecyclerView mRecyclerView;
    final DecimalFormat f = new DecimalFormat("#.00");

    public String[] getSpa() {
        return Spa;
    }

    public void setSpa(String[] spa) {
        Spa = spa;
    }

    public String[] getLoc() {
        return Loc;
    }

    public void setLoc(String[] loc) {
        Loc = loc;
    }

    public String[] getImage() {
        return Image;
    }

    public void setImage(String[] image) {
        Image = image;
    }

    public String[] getBusinessLong() {
        return BusinessLong;
    }

    public void setBusinessLong(String[] businessLong) {
        BusinessLong = businessLong;
    }

    public String[] getBusinessLat() {
        return BusinessLat;
    }

    public void setBusinessLat(String[] businessLat) {
        BusinessLat = businessLat;
    }

    public String[] getPhone() {
        return Phone;
    }

    public void setPhone(String[] phone) {
        Phone = phone;
    }

    public String[] getEmail() {
        return Email;
    }

    public void setEmail(String[] email) {
        Email = email;
    }

    public String[] getDocument() {
        return Document;
    }

    public void setDocument(String[] document) {
        Document = document;
    }

    public String[] getPromo() {
        return Promo;
    }

    public void setPromo(String[] promo) {
        Promo = promo;
    }

    public String[] getLong() {
        return Long;
    }

    public void setLong(String[] aLong) {
        Long = aLong;
    }

    public String[] getLat() {
        return Lat;
    }

    public void setLat(String[] lat) {
        Lat = lat;
    }

    public String[] getTown() {
        return Town;
    }

    public void setTown(String[] town) {
        Town = town;
    }

    public String[] getProvince() {
        return Province;
    }

    public void setProvince(String[] province) {
        Province = province;
    }

    public spaSearchListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public mainSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainlist, parent, false);
        mRecyclerView = (RecyclerView) parent;
        return new mainSearchViewHolder(view);
    }
    int lastOpen;
    boolean isOpened;
    mainSearchViewHolder mainviewHolder;
    int MainLayerHeight;
    int MainLayerWidht;
    @Override
    public void onBindViewHolder(@NonNull final mainSearchViewHolder holder, int position) {
        //get MainLayer default Height & Length
        if(MainLayerHeight < 1 && MainLayerWidht < 1){
            //set main Layout
            ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
            int defaultHeight = params.height;
            params.height = holder.MainrelativeLayout.getLayoutParams().height;
            params.width = holder.MainrelativeLayout.getLayoutParams().width;
            MainLayerHeight = params.height;
            MainLayerWidht = params.width;
        }
        //Main Layer height & width
        holder.Notification.setVisibility(View.GONE);
        Glide.with(activity).load(Image[position]).into(holder.image);
        holder.spa.setText(Spa[position]);
        holder.loca.setText(Loc[position]);
        holder.town.setText(Town[position]);
        holder.Main.setVisibility(View.GONE);
        //set provinces
        int Header = 0;
        if(position == 0){
            holder.province.setText(Province[position]);
            // Promo Layer Params
            ViewGroup.LayoutParams param = holder.Main.getLayoutParams();
            Header += param.height - 10;
            //set main Layout
            ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
            params.height = MainLayerHeight - Header;
            params.width = MainLayerWidht;
            holder.MainrelativeLayout.setLayoutParams(params);
            holder.MainrelativeLayout.invalidate();
            holder.Display.setImageResource(R.drawable.view);
        }else{
            if(!Province[position - 1].equalsIgnoreCase(Province[position])){
                holder.province.setText(Province[position]);
                holder.ProvinceLayer.setVisibility(View.VISIBLE);
                // Promo Layer Params
                ViewGroup.LayoutParams Promoparams = holder.Main.getLayoutParams();
                Header += Promoparams.height - 10;
            }else{
                holder.ProvinceLayer.setVisibility(View.GONE);
                // Promo Layer Params
                ViewGroup.LayoutParams params = holder.Main.getLayoutParams();
                Header += params.height ;
                //Province Layer Params
                ViewGroup.LayoutParams param = holder.ProvinceLayer.getLayoutParams();
                Header += param.height;
            }
            //set main Layout
            ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
            params.height = MainLayerHeight - Header;
            params.width = MainLayerWidht;
            holder.MainrelativeLayout.setLayoutParams(params);
            holder.MainrelativeLayout.invalidate();
            holder.Display.setImageResource(R.drawable.view);
        }
        if(Promo[position].equalsIgnoreCase("none")){
            holder.Display.setVisibility(View.GONE);
            MinLayout(holder,MinHeight);
        }else{
            holder.Display.setVisibility(View.VISIBLE);
            Glide.with(activity).load(Promo[position]).into(holder.Promo);
            MinLayout(holder,MinHeight);
        }
        if(!BusinessLong[position].equalsIgnoreCase("none") || !BusinessLat[position].equalsIgnoreCase("none")){
            Location locationA = new Location("point A");
            locationA.setLatitude(Double.parseDouble(BusinessLat[position]));
            locationA.setLongitude(Double.parseDouble(BusinessLong[position]));

            Location locationB = new Location("point B");

            locationB.setLatitude(Double.parseDouble(Lat[position]));
            locationB.setLongitude(Double.parseDouble(Long[position]));

            float distance = locationA.distanceTo(locationB);
            float Killometers = distance/1000;
            if(Killometers < 1){
                holder.distance.setText("0"+f.format(Killometers) + " km");
            }else{
                holder.distance.setText(f.format(Killometers) + " km");
            }

        }
        //Promo
        holder.Promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                promoDialog(activity,Promo[position]);
            }
        });

        //location
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(BusinessLong[position].equalsIgnoreCase("none") || BusinessLat[position].equalsIgnoreCase("none")){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "location information unavailable", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=&daddr="+BusinessLat[position]+","+BusinessLong[position]));
                    activity.startActivity(intent);
                }
            }
        });
        //phone
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(Phone[position].equalsIgnoreCase("none") || Phone[position].equalsIgnoreCase("")){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Phone information unavailable", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    //notify use of service
                    phoneEmailAlert("phone service",Spa[position],Document[position]);
                    /* Create the Intent */
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+Phone[position].trim()));
                    activity.startActivity(intent);
                }
            }
        });
        //Email
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(Email[position].equalsIgnoreCase("none") || Email[position].equalsIgnoreCase("")){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Email information unavailable", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    //notify use of service
                    phoneEmailAlert("phone service",Spa[position],Document[position]);
                    /* Create the Intent */
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    /* Fill it with Data */
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Email[position].trim()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Enquiry");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear "+Spa[position]+"\n\n");
                    activity.startActivity(emailIntent);

                    /* Send it off to the Activity-Chooser */
                    //activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    //Intent intent = new Intent(Intent.ACTION_DIAL);
                    //intent.setData(Uri.parse("tel:"+Phone[position]));
                    //activity.startActivity(intent);
                }
            }
        });
        //Contact information
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                messages.setSpaImage(Image[position]);
                messages.setSpaId(Document[position]);
                if(globalMethods.isNetworkAvailable(activity)) {
                    loadFragmentWithTag(R.id.main, new messages(), activity,"messages");
                }else{
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        //Display
        holder.Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //Getting View heights
                int ProvinceHeader = 0;
                int MainHeader = 0;
                if(holder.ProvinceLayer.getVisibility() == View.GONE){
                    //Province Layer Params
                    ViewGroup.LayoutParams params = holder.ProvinceLayer.getLayoutParams();
                    ProvinceHeader += params.height;
                    // Promo Layer Params
                    ViewGroup.LayoutParams Promoparams = holder.Main.getLayoutParams();
                    MainHeader += Promoparams.height;
                }else{
                    // Promo Layer Params
                    ViewGroup.LayoutParams params = holder.Main.getLayoutParams();
                    MainHeader += params.height - 10;

                }
                //Handle open & close
                if(holder.Main.getVisibility() == View.VISIBLE){
                    holder.Display.setImageResource(R.drawable.view);
                    holder.Main.setVisibility(View.GONE);
                    //Main Layer
                    ViewGroup.LayoutParams param = holder.MainrelativeLayout.getLayoutParams();
                    param.height = MainLayerHeight - ProvinceHeader - MainHeader;
                    holder.MainrelativeLayout.setLayoutParams(param);
                    holder.MainrelativeLayout.invalidate();
                }else{
                    holder.Display.setImageResource(R.drawable.hide);
                    holder.Main.setVisibility(View.VISIBLE);
                    //Main Layer
                    ViewGroup.LayoutParams param = holder.MainrelativeLayout.getLayoutParams();
                    param.height = MainLayerHeight - ProvinceHeader;// + MainHeader;
                    holder.MainrelativeLayout.setLayoutParams(param);
                    holder.MainrelativeLayout.invalidate();
                }
                mRecyclerView.smoothScrollToPosition(position);
                //set main Layout
                /*ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
                int defaultHeight = params.height;
                params.height = defaultHeight - Header;
                //String img = "android.resource://" + activity.getApplicationContext().getPackageName() + "/" + R.drawable.hide;
                //final Bitmap ExistingImage = ((BitmapDrawable)holder.Display.getDrawable()).getBitmap();
                //Drawable myDrawable = activity.getResources().getDrawable(R.drawable.hide);
                //final Bitmap hideImage = ((BitmapDrawable) myDrawable).getBitmap();
                if(lastOpen != position){
                    if(isOpened){
                        ConstraintLayout Main = mRecyclerView.getChildAt(lastOpen).findViewById(R.id.main);
                        ImageView imageView = mRecyclerView.getChildAt(lastOpen).findViewById(R.id.display);
                        imageView.setImageResource(R.drawable.view);
                        RelativeLayout Mainlayout = mRecyclerView.getChildAt(lastOpen).findViewById(R.id.mainLayout);
                        CardView ProvinceLayout = mRecyclerView.getChildAt(lastOpen).findViewById(R.id.Province);
                        //calculate main view total size
                        ViewGroup.LayoutParams MainViewparams = Main.getLayoutParams();
                        int defaultheight = MainViewparams.height;

                        //calculate length of previous views
                        int header = 0;
                        if(ProvinceLayout.getVisibility() == View.GONE){
                            //Province Layer Params
                            ViewGroup.LayoutParams param = ProvinceLayout.getLayoutParams();
                            header += param.height;
                            // Promo Layer Params
                            ViewGroup.LayoutParams Mainparams = Mainlayout.getLayoutParams();
                            header += Mainparams.height;
                        }else{
                            // Promo Layer Params
                            ViewGroup.LayoutParams param = Mainlayout.getLayoutParams();
                            header += param.height - 10;

                        }
                        MinLayout(mainviewHolder,defaultheight - header);
                        Mainlayout.setVisibility(View.GONE);
                        isOpened = false;
                    }
                }
                if(isOpened){
                    holder.Display.setImageResource(R.drawable.view);
                    MinLayout(holder,defaultHeight - Header);
                    holder.Main.setVisibility(View.GONE);
                    isOpened = false;
                }else {
                    //Uri imgUri = Uri.parse("android.resource://" + activity.getApplicationContext().getPackageName() + "/" + R.drawable.hide);
                    holder.Display.setImageResource(R.drawable.hide);
                    //Glide.with(activity).load(imgUri).into(holder.Display);
                    holder.Main.setVisibility(View.VISIBLE);
                    MaxLayout(holder, defaultHeight + Header);
                    isOpened = true;
                }*/
                lastOpen = position;
                mainviewHolder = holder;
            }
        });
        //send message
        //Display
        holder.Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (globalMethods.isNetworkAvailable(activity)) {
                    sendMessageDialog(activity, v, Document[position], Spa[position]);
                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        getAllNotifications(activity,Document[position],holder.Notification);
        /*if(!Category[position].equals("zerrrrO")){
            holder.Type.setText(InitializeFirstLetter(Category[position]));
            //holder.Type.getRootView().setVisibility(View.VISIBLE);
            //holder.TypeLine.getRootView().setVisibility(View.VISIBLE);
            holder.Type.setVisibility(View.VISIBLE);
            holder.TypeLine.setVisibility(View.VISIBLE);
        }else{
            //holder.Type.getRootView().setVisibility(View.GONE);
            //holder.TypeLine.getRootView().setVisibility(View.GONE);
            holder.Type.setVisibility(View.INVISIBLE);
            holder.TypeLine.setVisibility(View.INVISIBLE);
        }
        Glide.with(activity).load(Image[position]).into(holder.Image);
        holder.Product.setText(Item[position]);
        holder.Amount.setText("R"+Price[position]);
        holder.Quantity.setText("");
        holder.Quantity.setText("1");
        holder.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                EditText quatity = holder.Quantity;
                if(!quatity.getText().toString().isEmpty()){
                    try{
                        int amount = Integer.parseInt(quatity.getText().toString());
                        if (Integer.parseInt(quatity.getText().toString()) > 0) {
                            setItemsToCart(activity, v, Image[position], Price[position], Item[position], String.valueOf(amount));
                        }

                    }catch (Exception e){
                        quatity.requestFocus();
                    }
                    //Toast.makeText(activity, String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    public static void sendMessageDialog(final Activity activity,final View view,final String SpaId, final String Spa){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.sendlayout);
        dialog.setCancelable(true);
        final EditText idno = (EditText) dialog.findViewById(R.id.idno);
        final EditText message = (EditText) dialog.findViewById(R.id.body);
        CardView Submit = (CardView) dialog.findViewById(R.id.MySubmit);
        final RadioButton Citizen = (RadioButton) dialog.findViewById(R.id.citizen);
        RadioButton NonCitizen = (RadioButton) dialog.findViewById(R.id.non_citizen);
        RelativeLayout Main = (RelativeLayout) dialog.findViewById(R.id.main);
        ConstraintLayout MainLayout = (ConstraintLayout) dialog.findViewById(R.id.main_layout);
        message.setHint("to: "+ Spa);
        if(accessKeys.getIdnumber() != null){
            Main.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = MainLayout.getLayoutParams();
            params.height = 400;
            params.width = MainLayout.getLayoutParams().width;
            MainLayout.setLayoutParams(params);
            MainLayout.invalidate();
        }else{
            Citizen.setChecked(true);

        }
        dialog.show();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalMethods.ShowDialog(activity);
                String citizen;
                if(!message.getText().toString().isEmpty() && message.getText().toString().length()> 20){
                    boolean updateId = false;
                    if(accessKeys.getIdnumber() == null)
                        updateId = true;
                    if(Citizen.isChecked()){
                        citizen = "RSA ID";
                    }else{
                        citizen = "Non-RSA ID";
                    }
                    sendUserDetails(activity,view,updateId,idno.getText().toString(),citizen,message.getText().toString().replace("\"","°"),dialog,SpaId);

                }else{
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "enter enough description, more than 20 characters", Toast.LENGTH_LONG).show();
                            message.requestFocus();
                        }
                    });
                }
            }
        });
        Citizen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                idno.setHint("RSA Id number");
                int maxLength = 13;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                idno.setFilters(fArray);
                idno.setInputType(InputType.TYPE_CLASS_NUMBER);
                return false;
            }
        });
        NonCitizen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                idno.setHint("Foreign doc number");
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                idno.setFilters(fArray);
                idno.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });
    }

    public static void promoDialog(final Activity activity, final String image){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.previewpromo);
        dialog.setCancelable(true);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.promo);
        Glide.with(activity).load(image).into(imageView);
        dialog.show();
    }

    private void MinLayout(mainSearchViewHolder holder, int height){
        ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
        params.height = height;
        params.width = holder.MainrelativeLayout.getLayoutParams().width;
        holder.MainrelativeLayout.setLayoutParams(params);
        holder.MainrelativeLayout.invalidate();
    }

    private void MaxLayout(mainSearchViewHolder holder, int height){
        ViewGroup.LayoutParams params = holder.MainrelativeLayout.getLayoutParams();
        params.height = height;
        params.width = holder.MainrelativeLayout.getLayoutParams().width;
        holder.MainrelativeLayout.setLayoutParams(params);
        holder.MainrelativeLayout.invalidate();
    }

    //sending user Message
    private static void sendUserDetails(final Activity activity,final View view,final boolean updateId, final String Id, final String IdType, final String Message, final Dialog dialog, final String SpaDocument){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("document ref", defaultvalue);
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("name", accessKeys.getName());
            user.put("surname", accessKeys.getSurname());
            user.put("email", accessKeys.getEmail());
            user.put("phone", accessKeys.getPhone());
            user.put("altphone", accessKeys.getAltPhone());
            user.put("gender", accessKeys.getGender());
            user.put("date", ToDate());
            user.put("time", Time());
            user.put("message", Message);
            user.put("senderRead", true);
            user.put("receiverRead", false);
            user.put("messageFrom", "sender");

            // Add a new document with a generated ID
            db.collection(SpaDocument+"_"+ constants.appMessages)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(SpaDocument+"_"+constants.appMessages);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //set Realm user information;
                                        Loginfo("user Personal Details successfully added");
                                        if(updateId){
                                            CollectionReference collectionReference = db.collection(constants.users);
                                            collectionReference.document(accessKeys.getDefaultDocument()).update("id number", Id, "Id type",IdType).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        //set Realm user information;
                                                        Loginfo("user Personal Details successfully added");
                                                        //check existing client
                                                        getClientValidityInformation(view,SpaDocument,SpaDocument+"_clients",Id,document,dialog);
                                                        //getFirestoreClient(view,SpaDocument+"_clients",Id,document,dialog);
                                                        accessKeys.setIdnumber(Id);
                                                    }
                                                }

                                            });

                                        }else{
                                            //check existing client
                                            //getFirestoreClient(view,SpaDocument+"_clients",accessKeys.getIdnumber(),document,dialog);
                                            getClientValidityInformation(view,SpaDocument,SpaDocument+"_clients",Id,document,dialog);
                                        }
                                    }else{
                                        globalMethods.stopProgress = true;
                                        Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                                        Logerror("unable to update user ID Details, ");
                                        dialog.dismiss();

                                    }
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            globalMethods.stopProgress = true;
                            Toast.makeText(activity, "Error adding sending details to server", Toast.LENGTH_SHORT).show();
                            Logerror("unable to add user Personal Details, " + e.getMessage());
                            dialog.dismiss();
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }
    }
    //Determining existing user

    public static void getClientValidityInformation(final View view, final String SpaDocument, final String SpaClients, final String ClientId, final String DocumentReference, final Dialog dialog) {
        //gets all documents from firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SpaClients).whereEqualTo("IdNumber", ClientId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean isFound = false;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.get("IdNumber") != null) {
                            if (document.get("IdNumber").toString().equalsIgnoreCase(ClientId)) {
                                isFound = true;
                            }

                        }
                    }
                    updateExistingClient(view,SpaDocument,isFound,DocumentReference,dialog);
                }
            }
        });

    }

    private static void updateExistingClient(final View view, final String SpaDocument, final boolean Existing, final String DocumentReference, final Dialog dialog){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //update user details
        CollectionReference collectionReference = db.collection(SpaDocument+"_"+constants.appMessages);
        collectionReference.document(DocumentReference).update("Existing user", Existing).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //success
                    Loginfo("user Items successfully added to cart");
                    globalMethods.stopProgress = true;
                    globalMethods.ConfirmResolution(view," Message sent!");
                    dialog.dismiss();
                }
            }

        });

    }

    //check notifications
    public static void getAllNotifications(final Activity activity, final String SpaDocument, final CardView cardView) {
        //gets all documents from firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SpaDocument+"_"+constants.appMessages).whereEqualTo("senderRead", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean results = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.get("senderRead")!=null){
                            if (!Boolean.valueOf(document.get("senderRead").toString())){
                                results = true;
                            }
                        }
                    }
                    if(results){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                cardView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }
        });

    }
    //send phone/email click notification
    private static void phoneEmailAlert(final String phoneEmail,final String Spa,final String SpaId){
        try {
            String defaultvalue = "n/a";
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("document ref", defaultvalue);
            user.put("userid", accessKeys.getDefaultUserId());
            user.put("service", phoneEmail);
            user.put("targetSpa", Spa);
            user.put("targetSpaId", SpaId);
            user.put("name", InitializeFirstLetter(accessKeys.getName())+" "+InitializeFirstLetter(accessKeys.getSurname()));
            user.put("date", ToDate());
            user.put("time", Time());

            // Add a new document with a generated ID
            db.collection(constants.sourceNotification)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            final  String document = documentReference.getId();
                            CollectionReference collectionReference = db.collection(constants.sourceNotification);
                            collectionReference.document(documentReference.getId()).update("document ref", document).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //set Realm user information;
                                        Loginfo("user contact service updated");

                                    }else{
                                        Logerror("unable to update user contact service updated, ");
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Logerror("unable to add user contact service, " + e.getMessage());
                        }
                    });
        }catch (Exception exception){
            exception.getMessage();
            exception.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return Spa.length;
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

class mainSearchViewHolder extends RecyclerView.ViewHolder {
    public TextView spa;
    public ImageView image;
    public TextView loca;
    public RelativeLayout Main;
    public ImageView Promo;
    public ImageView Display;
    public ConstraintLayout MainrelativeLayout;
    public TextView distance;
    public ImageView location;
    public ImageView phone;
    public ImageView email;
    public ImageView info;
    public TextView province;
    public TextView town;
    public CardView ProvinceLayer;
    public FloatingActionButton Send;
    public CardView Notification;


    public mainSearchViewHolder(View view) {
        super(view);
        spa = view.findViewById(R.id.spa);
        image = view.findViewById(R.id.prodImage);
        loca = view.findViewById(R.id.loc);
        Main = view.findViewById(R.id.mainLayout);
        Promo = view.findViewById(R.id.promo);
        Display = view.findViewById(R.id.display);
        MainrelativeLayout = view.findViewById(R.id.main);
        distance = view.findViewById(R.id.distance);
        location = view.findViewById(R.id.location);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        info = view.findViewById(R.id.info);
        province = view.findViewById(R.id.province);
        town = view.findViewById(R.id.town);
        ProvinceLayer = view.findViewById(R.id.Province);
        Send = view.findViewById(R.id.send);
        Notification = view.findViewById(R.id.notification);
    }
}

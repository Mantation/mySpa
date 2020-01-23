package menuFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import io.eyec.bombo.myspa.R;
import properties.accessKeys;

import static io.eyec.bombo.myspa.MainActivity.setFragmentTag;
import static properties.accessKeys.setExitApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class spaSearch extends android.app.Fragment implements View.OnClickListener{
    public static RecyclerView recyclerView;
    View myview;
    ImageView SearchButton;
    private static boolean isProvince;
    private static String Province;
    public static AutoCompleteTextView Search;
    public static ArrayAdapter<String> adapter;
    static String fragmentTag = "mainSearch";

    public static boolean isIsProvince() {
        return isProvince;
    }

    public static void setIsProvince(boolean isProvince) {
        spaSearch.isProvince = isProvince;
    }

    public static String getProvince() {
        return Province;
    }

    public static void setProvince(String province) {
        Province = province;
    }

    public spaSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_spa_search, container, false);
        recyclerView = myview.findViewById(R.id.recycler_main);
        SearchButton = myview.findViewById(R.id.searchButton);
        Search = myview.findViewById(R.id.search);
        Search.setVisibility(View.GONE);
        Search.setOnClickListener(this);
        SearchButton.setOnClickListener(this);
        connectionHandler.external.spasSearch_.getAllDocuments(getActivity(), recyclerView,isIsProvince(),getProvince());
        //set Date to autocomplete
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, accessKeys.TownValues);
        Search.setThreshold(6);
        Search.setAdapter(adapter);
        //handle selected items
        Search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
                System.out.println("Selection is " + selection);
                boolean isProvince = false;
                for (String str: accessKeys.ProvinceValues){
                    if(selection.equalsIgnoreCase(str))
                        isProvince = true;
                }
                spaSearch.setIsProvince(isProvince);
                spaSearch.setProvince(selection);
                methods.globalMethods.loadFragmentWithTag(R.id.main, new spaSearch(), getActivity(),fragmentTag);
                //connectionHandler.external.spasSearch_.getAllDocuments(getActivity(), recyclerView,isProvince,selection);
                Search.getText().clear();
                //methods.globalMethods.loadFragments(R.id.main, new notes(),activity);
                //System.out.println(selection);
                //System.out.println(MyDocument[position]);
            }
        });
        setFragmentTag(fragmentTag);
        setExitApplication(true);
        return myview;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.searchButton){
            Search.getText().clear();
            if(Search.getVisibility() == View.VISIBLE){
                Search.setVisibility(View.GONE);
                SearchButton.setImageResource(R.drawable.ic_search_black_24dp);
            }else{
                Search.setVisibility(View.VISIBLE);
                SearchButton.setImageResource(R.drawable.back);
                Search.requestFocus();
            }
        }

    }
}

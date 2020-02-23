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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import connectionHandler.external.spasSearch_;
import io.eyec.bombo.myspa.R;
import methods.globalMethods;
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
    private static String Category;
    private static String option;
    public static AutoCompleteTextView Search;
    public static ArrayAdapter<String> adapter;
    static String fragmentTag = "mainSearch";

    public static String getCategory() {
        return Category;
    }

    public static void setCategory(String category) {
        Category = category;
    }

    public static void setOption(String option) {
        spaSearch.option = option;
    }

    public static String getOption() {
        return option;
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
        spasSearch_.getAllDocuments(getActivity(), recyclerView,getCategory(),getOption());
        //newArrayList with spas, provinces, and towns
        List<String> allItems = new ArrayList<String>();
        Collections.addAll(allItems,accessKeys.spaValues);
        //add town & spa values
        //sort Spa values
        Set<String> SpaList = new HashSet<>(allItems);
        //set data to autocomplete
        List<String> spaItems = new ArrayList<String>();
        spaItems.addAll(SpaList);
        Collections.addAll(spaItems,accessKeys.TownValues);
        //set Data to autocomplete
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, spaItems);
        Search.setThreshold(6);
        Search.setAdapter(adapter);
        //handle selected items
        Search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
                System.out.println("Selection is " + selection);
                String category = "";
                for (String str: accessKeys.ProvinceValues){
                    if(selection.equalsIgnoreCase(str))
                        category = "province";
                }
                if (category.equalsIgnoreCase("")) {
                    for (String str : accessKeys.TownValues) {
                        if (selection.equalsIgnoreCase(str))
                            category = "town";
                    }
                }
                if (category.equalsIgnoreCase("")) {
                    category = "spa";
                }

                spaSearch.setCategory(category);
                spaSearch.setOption(selection);
                globalMethods.loadFragmentWithTag(R.id.main, new spaSearch(), getActivity(),fragmentTag);
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

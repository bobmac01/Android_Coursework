package com.robertmcateer.com.navigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class Search extends Fragment implements View.OnClickListener {
    public static ArrayList<NewEvent> returnedEvents;

    Button searchBtn;
    EditText searchTerm;

    DatabaseHandler db;



    public Search(){} // Empty Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        new DatabaseHandler(getActivity().getApplicationContext());

        searchBtn = (Button)v.findViewById(R.id.searchButton);
        searchTerm = (EditText)v.findViewById(R.id.search_text);

        searchBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v)
    {
        Log.i("SEARCH", searchTerm.getText().toString());
        db.searchResults(searchTerm.getText().toString());
    }
}

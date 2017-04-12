package com.robertmcateer.com.navigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Search extends Fragment implements View.OnClickListener {
    public static ArrayList<NewEvent> returnedEvents;

    Button searchBtn;
    EditText searchTerm;

    DatabaseHandler db;

    List<NewEvent> results;


    public Search(){} // Empty Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        db = new DatabaseHandler(getActivity().getApplicationContext());

        searchBtn = (Button)v.findViewById(R.id.searchButton);
        searchTerm = (EditText)v.findViewById(R.id.search_text);

        searchBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v)
    {
        String search = searchTerm.getText().toString();
        results = new ArrayList<NewEvent>();

        if(search.equals(""))
        {
            Toast toast = Toast.makeText(getContext(), "You need to enter a search term above :-)", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            if(db.searchResults(search).size() == 0)
            {
                Log.i("DATABASE SEARCH","it's zero!");
                Toast toast = Toast.makeText(getContext(), "There's nothing matching that string!", Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                for(NewEvent n : db.searchResults(search))
                {
                    results.add(new NewEvent(n.getTitle(), n.getURI(), n.getVenue(), n.getDate(), n.getEventType()));
                }
                db.close();
                // Updates the recycler view
                update();
            }
        }


    }

    public void update()
    {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.card_list);
        recyclerView.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new MyRecyclerAdapter(results);
        recyclerView.setAdapter(adapter);
    }
}

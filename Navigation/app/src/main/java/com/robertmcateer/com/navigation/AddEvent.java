package com.robertmcateer.com.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddEvent extends Fragment
{

    public EditText title;
    public EditText date;
    public EditText venue;
    public EditText url;
    public EditText type;
    public Button dateBtn;
    public Button addBtn;

    public AddEvent()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_add_event, container, false);

        title = (EditText)v.findViewById(R.id.AddEventTitle);
        date = (EditText)v.findViewById(R.id.AddDateText);
        venue = (EditText)v.findViewById(R.id.AddVenueEvent);
        type = (EditText)v.findViewById(R.id.AddTypeEvent);
        url = (EditText)v.findViewById(R.id.AddURLEvent);

        dateBtn = (Button)v.findViewById(R.id.AddDateEvent);
        addBtn = (Button)v.findViewById(R.id.AddEventButton);

        dateBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Log.i("LOG", "ADD DATE WORKING");
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Log.i("LOG", "ADDING AN EVENT :-)");
            }
        });



        return v;
    }

    private void addNewEvent(String title, String date, String venue, String type, String url)
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.addEvent(new NewEvent(title, url, venue, date, type));
    }
}

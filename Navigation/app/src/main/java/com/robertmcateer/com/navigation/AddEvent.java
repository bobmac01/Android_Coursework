package com.robertmcateer.com.navigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddEvent extends Fragment
{

    public EditText title;
    public EditText date;
    public EditText venue;
    public EditText url;
    public EditText type;

    public AddEvent()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        title = (EditText)container.findViewById(R.id.AddEventTitle);
        date = (EditText)container.findViewById(R.id.AddDateText);
        venue = (EditText)container.findViewById(R.id.AddVenueEvent);
        type = (EditText)container.findViewById(R.id.AddTypeEvent);
        url = (EditText)container.findViewById(R.id.AddURLEvent);

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    private void addNewEvent(String title, String date, String venue, String type, String url)
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.addEvent(new NewEvent(title, url, venue, date, type));
    }

}

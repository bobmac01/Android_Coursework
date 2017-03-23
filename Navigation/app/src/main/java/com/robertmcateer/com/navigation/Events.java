package com.robertmcateer.com.navigation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Events extends Fragment
{
    ArrayList<NewEvent> events;

    public Events()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        GetEventData event = new GetEventData();
        event.execute();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        events = new ArrayList<NewEvent>();

        for(NewEvent n : db.getAllEvents())
        {
            events.add(new NewEvent(n.getTitle(), "", n.getVenue(), n.getDate(), n.getEventType()));
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.card_list);
        recyclerView.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new MyRecyclerAdapter(events);
        recyclerView.setAdapter(adapter);

    }


    public void DatabaseTest()
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        Log.d("INSERT: ", "INSERTING...");
        db.addEvent(new NewEvent("AC/DC", "http://ACDC.COM", "AECC", "1/2/2020", "13:00"));

        Log.d("READING: ", "READING...");

        List<NewEvent> events = db.getAllEvents();

        for(NewEvent e : events)
        {
            String log =
                            "Title: " + e.getTitle() +
                            ", URI: " + e.getURI() +
                            ", Venue: " + e.getVenue() +
                            ", Date:" + e.getDate() +
                            ", Time: " + e.getEventType();

            Log.d("EVENT", log);
        }
    }

    private class GetEventData extends AsyncTask<Object, String, Integer>
    {
        private final String QUERY_STRING =
                "http://api.songkick.com/api/3.0/metro_areas/24596/calendar.xml?apikey=LGlk9gNZ5vlumW7u";

        @Override
        protected Integer doInBackground(Object... params)
        {
            XmlPullParser gettingData = tryDownloadingData();
            int recordsFound = tryParsingXMLData(gettingData);
            return recordsFound;
        }

        private XmlPullParser tryDownloadingData()
        {
            try
            {
                Log.i("EVENTDATA", "Now downloading");
                URL xmlURL = new URL(QUERY_STRING);
                XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
                receivedData.setInput(xmlURL.openStream(), null);
                return receivedData;
            } catch (XmlPullParserException e)
            {
                Log.i("EVENT_XML_exception", e.toString());
            } catch (IOException e)
            {
                Log.i("EVENT_IO_exception", e.toString());
            }
            return null;
        }

        private int tryParsingXMLData(XmlPullParser gettingData)
        {
            if (gettingData != null)
            {
                try
                {
                    return processData(gettingData);
                } catch (XmlPullParserException e)
                {
                    Log.i("EVENT_XML_exception", e.toString());

                } catch (IOException e)
                {
                    Log.i("EVENT_IO_exception", e.toString());
                }
            }
            return 0;
        }

        private int processData(XmlPullParser gettingData) throws XmlPullParserException, IOException
        {
            int recordsFound = 0;

            String title = "";
            String location = "";
            String dateOfEvent = "";
            String typeOfEvent = "";
            String eventURL = "";

            int eventType = -1;
            while (eventType != XmlResourceParser.END_DOCUMENT)
            {
                String tagName = gettingData.getName();

                switch (eventType)
                {
                    case XmlResourceParser.START_TAG:

                        if(tagName.equals("event"))
                        {
                            // type of event
                            typeOfEvent = gettingData.getAttributeValue(null, "type");
                            title = gettingData.getAttributeValue(null, "displayName");
                            eventURL = gettingData.getAttributeValue(null, "uri");
                        }
                        if(tagName.equals("start"))
                        {
                            dateOfEvent = gettingData.getAttributeValue(null, "date");
                        }
                        if(tagName.equals("venue"))
                        {
                            location = gettingData.getAttributeValue(null, "displayName");
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equals("event"))
                        {
                            recordsFound++;
                            publishProgress(title, location, dateOfEvent, typeOfEvent, eventURL);
                        }
                        break;
                }
                eventType = gettingData.next();
            }
            if (recordsFound == 0)
            {
                publishProgress();
            }
            return recordsFound;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            if (values.length == 0)
            {
                Log.i("Events", "There is no data");
            }

            if(values.length > 0)
            {
                    //Log.i("NEW EVENT", "----------------- Database insert -----------------------");
/*
                    Log.i("Title", values[0]);
                    Log.i("Location", values[1]);
                    Log.i("Date", values[2]);
                    Log.i("Type of event", values[3]);
                    Log.i("URL", values[4]);
*/
                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

                Object n = db.getEvent(values[0]);

                if(n != null)
                {
                    Log.i("DATABASE_STUFF","NEW EVENT NOT NULL");
                }
                else
                {
                    Log.i("DATABASE_STUFF","NEW EVENT NOT NULL");

                }

                //db.addEvent(new NewEvent(values[0], values[4], values[1], values[2], values[3]));
                //db.addEvent(new NewEvent("","","","",""));
                //Log.i("database all", db.getAllEvents().toString());
            }

            super.onProgressUpdate();
        }
    }
}

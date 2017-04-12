package com.robertmcateer.com.navigation;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Events extends Fragment implements View.OnClickListener
{
    public static ArrayList<NewEvent> events;
    FloatingActionButton addEventBtn;
    FragmentTransaction ft;

    public Events(){} // Empty constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        this.addEventBtn = (FloatingActionButton) v.findViewById(R.id.addEventFloatingButton);

        addEventBtn.setOnClickListener(this);

        GetEventData g = new GetEventData();
        g.execute();

        // Inflate the layout for this fragment

        return v;
    }

    @Override
    public void onClick(View v)
    {
        Search frag = new Search();
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, frag).commit();
        TextView title = (TextView)getActivity().findViewById(R.id.toolbarTextView);
        title.setText("Search");
    }

    public void refresh()
    {
        ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void update()
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        events = new ArrayList<NewEvent>();

        for(NewEvent n : db.getAllEvents())
        {
            events.add(new NewEvent(n.getTitle(), n.getURI(), n.getVenue(), n.getDate(), n.getEventType()));
        }

        if(events.size() == 0)
        {
            Toast toast = Toast.makeText(getContext(), "There's no events!", Toast.LENGTH_LONG);
            toast.show();
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.card_list);
        recyclerView.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new MyRecyclerAdapter(events);
        recyclerView.setAdapter(adapter);
        db.close();
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
        db.close();
    }

    private class GetEventData extends AsyncTask<Object, String, Integer>
    {

        /*
        I can't query the Songklick API much more as if i use the device location city it
        returns data from Aberdeen in America. It's just down to the way they arvhice their data also.
        Each city is a "metro_area" in the URL so you need to find the code for your given area.

        This example: Aberdeen City = 24596
         */
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
            try {
                Log.i("EVENTDATA", "Now downloading");
                URL xmlURL = new URL(QUERY_STRING);
                XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
                receivedData.setInput(xmlURL.openStream(), null);
                return receivedData;
            } catch (XmlPullParserException e) {
                Log.i("EVENT_XML_exception", e.toString());
            } catch (IOException e) {
                Log.i("EVENT_IO_exception", e.toString());
            }
            return null;
        }

        private int tryParsingXMLData(XmlPullParser gettingData)
        {
            if (gettingData != null) {
                try {
                    return processData(gettingData);
                } catch (XmlPullParserException e) {
                    Log.i("EVENT_XML_exception", e.toString());

                } catch (IOException e) {
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
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                String tagName = gettingData.getName();

                switch (eventType) {
                    case XmlResourceParser.START_TAG:

                        if (tagName.equals("event")) {
                            // type of event
                            typeOfEvent = gettingData.getAttributeValue(null, "type");
                            title = gettingData.getAttributeValue(null, "displayName");
                            eventURL = gettingData.getAttributeValue(null, "uri");
                        }
                        if (tagName.equals("start")) {
                            dateOfEvent = gettingData.getAttributeValue(null, "date");
                        }
                        if (tagName.equals("venue")) {
                            location = gettingData.getAttributeValue(null, "displayName");
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equals("event")) {
                            recordsFound++;
                            publishProgress(title, location, dateOfEvent, typeOfEvent, eventURL);
                        }
                        break;
                }
                eventType = gettingData.next();
            }
            if (recordsFound == 0) {
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
                refresh();

            }

            if (values.length > 0)
            {
                /*
                Log.i("Title", values[0]);
                Log.i("Location", values[1]);
                Log.i("Date", values[2]);
                Log.i("Type of event", values[3]);
                Log.i("URL", values[4]);
                */

                DatabaseHandler db = new DatabaseHandler(getContext());

                // Method to check for each database entry
                Boolean exists = db.checkDatabase(values[0]);

                if(exists)
                {
                    // Record is found based on title. Moves onto next...
                    Log.i("CHECK", "RECORD EXSISTS. MOVING...");

                }
                else
                {
                    // adds to the database
                    db.addEvent(new NewEvent(values[0], values[4], values[1], values[2], values[3]));

                }
                db.close();
            }
            super.onProgressUpdate();

            update();
        }
    }
}

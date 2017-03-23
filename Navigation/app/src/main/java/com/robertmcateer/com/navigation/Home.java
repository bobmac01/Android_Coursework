package com.robertmcateer.com.navigation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment
{
    public TextView LOCATION, TEMP, HUMID, TYPE, WIND, CHOICETEXT;
    public String IMAGEURL;
    public String weatherType;
    public ImageView IMAGEVIEW_URL;

    public Home()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);

        Context c = getActivity().getApplicationContext();

        weatherType = "";

        LOCATION = (TextView) view.findViewById(R.id.location_cardview);
        WIND = (TextView) view.findViewById(R.id.wind_cardview);
        TEMP = (TextView) view.findViewById(R.id.temp_cardview);
        HUMID = (TextView) view.findViewById(R.id.humid_cardview);
        TYPE  = (TextView) view.findViewById(R.id.type_cardview);
        IMAGEURL = "";
        IMAGEVIEW_URL = (ImageView) view.findViewById(R.id.image_cardview);
        CHOICETEXT = (TextView)view.findViewById(R.id.suggestion);


        WeatherWidget widget = new WeatherWidget();
        widget.execute();

        return view;
    }


    private class WeatherWidget extends AsyncTask<Object, String, Integer>
    {
        public String[] data = new String[5];
        private final String QUERY_STRING =
                "http://api.apixu.com/v1/current.xml?key=78e10938f1d742bfa9e133038171602&q=aberdeen";

        @Override
        protected Integer doInBackground(Object... params) {
            XmlPullParser gettingData = tryDownloadingData();
            int recordsFound = tryParsingXMLData(gettingData);
            return recordsFound;
        }

        private XmlPullParser tryDownloadingData()
        {
            try
            {
                Log.i("WeatherWidget", "Now downloading");
                URL xmlURL = new URL(QUERY_STRING);
                XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
                receivedData.setInput(xmlURL.openStream(), null);
                return receivedData;
            }
            catch(XmlPullParserException e)
            {
                Log.i("Weather_XML_exception", e.toString());
            }
            catch(IOException e)
            {
                Log.i("Weather_IO_exception", e.toString());
            }
            return null;
        }

        private int tryParsingXMLData(XmlPullParser gettingData)
        {
            if(gettingData != null)
            {
                try {
                    return processData(gettingData);
                }
                catch(XmlPullParserException e)
                {
                    Log.i("Weather_XML_exception", e.toString());

                }
                catch(IOException e)
                {
                    Log.i("Weather_IO_exception", e.toString());
                }
            }
            return 0;
        }

        private int processData(XmlPullParser gettingData) throws XmlPullParserException, IOException
        {
            int recordsFound = 0;

            String location = "";
            String windspeed = "";
            String temp = "";
            String humidity = "";
            String overcast = "";
            String weatherImage = "";

            int eventType = -1;
            while(eventType != XmlResourceParser.END_DOCUMENT)
            {
                String tagName = gettingData.getName();

                switch(eventType)
                {
                    case XmlResourceParser.START_TAG:
                        if(tagName.equals("name"))
                        {
                            location = gettingData.nextText();
                        }
                        if(tagName.equals("temp_c"))
                        {
                            temp = gettingData.nextText();
                        }

                        if(tagName.equals("text"))
                        {
                            overcast = gettingData.nextText();
                        }
                        if(tagName.equals("icon"))
                        {
                            weatherImage = gettingData.nextText();
                        }

                        if(tagName.equals("humidity"))
                        {
                            humidity = gettingData.nextText();
                        }

                        if(tagName.equals("wind_mph"))
                        {
                            windspeed = gettingData.nextText();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(tagName.equals("root"))
                        {
                            recordsFound++;
                            publishProgress(location, windspeed, temp, humidity, overcast, weatherImage);
                        }
                        break;
                }
                eventType = gettingData.next();
            }
            if(recordsFound == 0)
            {
                publishProgress();
            }
            return recordsFound;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            if(values.length == 0)
            {
                Log.i("WeatherWidget", "There is no data");
            }
            if(values.length == 6)
            {
                Log.i("WeatherWidget", "Location: " + values[0]);
                Log.i("WeatherWidget", "Temp: " + values[2]  + "c");
                Log.i("WeatherWidget", "Type: " + values[4]);
                Log.i("WeatherWidget", "ImageURL: " + values[5]);
                Log.i("WeatherWidget", "Wind: " + values[1] + "MPH");
                Log.i("WeatherWidget", "humidity: " + values[3] + "%");


                LOCATION.setText(values[0]);
                WIND.setText("Wind: "+values[1] + "MPH");
                TEMP.setText("Temp: "+values[2]+"c");
                HUMID.setText("Humidity: "+values[3]+"%");
                TYPE.setText(values[4]);
                weatherType = values[4];
                IMAGEURL = "http:"+values[5];
                Log.i("Meh", IMAGEURL);

                // This sets the suggestion text
                CHOICETEXT.setText(setMainText(weatherType));

                // This sets the imageview using Picasso dependency
                Picasso
                        .with(getActivity().getApplicationContext())
                        .load(IMAGEURL)
                        .into(IMAGEVIEW_URL);

            }
            super.onProgressUpdate();
        }

        private String setMainText(String weatherType)
        {
            if(weatherType.equals("Sunny") || weatherType.equals("Partly cloudy"))
            {
                return "It's sunny! Go party outside or head straight to tbe beer garden."
                + "This is not a suggestion it is an order!";
            }

            if(weatherType.equals("Torrential rain shower") || weatherType.equals("Heavy rain"))
            {
                return "Just don't even go out pal. It's nae worth it";
            }

            if(weatherType.equals("Cloudy"))
            {
                return "It's cloudy! Maybe try something indoors?";
            }

            if(weatherType.equals("Overcast"))
            {
                return "Meh. If Overcast was a day it'd be called Monday";
            }

            if(weatherType.equals("Light snow"))
            {
                return "YAY IT'S SNOWING!!!! Time to build a snow angel!";
            }

            if(weatherType.equals("Light drizzle") || weatherType.equals("Light rain"))
            {
                return "Ah it's sort of raining. How about take an umbrella with you?";
            }

            if(weatherType.equals("Clear"))
            {
                return "It's a nice night. Maybe go take some photos of the stars?";
            }
            return "I dinna know what to do pal! Maybe go to work?";
        }
    }
}

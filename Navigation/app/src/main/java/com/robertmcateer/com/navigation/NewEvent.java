package com.robertmcateer.com.navigation;

/**
 * Public class to house a new event taken from the XML data
 * This is for adding into the SQLite database
 */

public class NewEvent
{
    // Private variables
    String _title, _uri, _venue, _eventtype, _date;

    public NewEvent()
    {}


    public NewEvent(String title, String uri, String venue, String date, String _eventtype)
    {
        this._title = title;
        this._uri = uri;
        this._venue = venue;
        this._date = date;
        this._eventtype = _eventtype;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle(String title)
    {
        this._title = title;
    }

    public String getURI()
    {
        return _uri;
    }

    public void setURI(String uri)
    {
        this._uri = uri;
    }

    public String getVenue()
    {
        return _venue;
    }

    public void setVenue(String venue)
    {
        this._venue = venue;
    }

    public String getEventType()
    {
        return _eventtype;
    }

    public void setEventType(String type)
    {
        this._eventtype = type;
    }

    public String getDate()
    {
        return _date;
    }

    public void setDate(String date)
    {
        this._date = date;
    }
}

package com.robertmcateer.com.navigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "events";

    private static final String TABLE_EVENTS = "eventTable";

    // Table columns
    private static final String KEY_TITLE = "title";
    private static final String KEY_URI = "uri";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "eventType";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_EVENTS + "(" +
                        KEY_TITLE + " TEXT, " +
                        KEY_URI + " TEXT, " +
                        KEY_VENUE + " TEXT, " +
                        KEY_DATE + " TEXT, " +
                        KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(NewEvent event)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.d("BEFORE: ", "");
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_URI, event.getURI());
        values.put(KEY_VENUE, event.getVenue());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TYPE, event.getEventType());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public NewEvent getEvent(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS, new String[] {
                        KEY_TITLE, KEY_URI, KEY_VENUE, KEY_DATE, KEY_TYPE},
                KEY_TITLE + "=?",
                new String[] { String.valueOf(title)}, null, null, null, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        NewEvent event = new NewEvent(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
                );
        return event;
    }

    public List<NewEvent> getAllEvents()
    {
        List<NewEvent> eventlist = new ArrayList<NewEvent>();

        String query = "SELECT * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Loop time
        if(cursor.moveToFirst())
        {
            do
            {
                NewEvent event = new NewEvent();
                event.setTitle(cursor.getString(0));
                event.setURI(cursor.getString(1));
                event.setVenue(cursor.getString(2));
                event.setDate(cursor.getString(3));
                event.setEventType(cursor.getString(4));

                eventlist.add(event);
            } while (cursor.moveToNext());
        }
        return eventlist;
    }

    public int getEventsCount()
    {
        String countquery = "SELECT * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countquery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateEvent(NewEvent event)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_URI, event.getURI());
        values.put(KEY_VENUE, event.getVenue());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TYPE, event.getEventType());

        // Update row
        return db.update(TABLE_EVENTS, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(event.getTitle()) });
    }

    public void deleteEvent(NewEvent event)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_TITLE + " = ?",
                new String[] { String.valueOf(event.getTitle()) });
        db.close();
    }

}

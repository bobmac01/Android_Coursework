package com.robertmcateer.com.navigation;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditEvent extends Fragment implements DatePickerDialog.OnDateSetListener,  View.OnClickListener
{
    public static String titleCheck;
    EditText VENUE_TEXT, URL_TEXT, TYPE_TEXT;
    TextView TITLE_TEXT, DATE_TEXT;
    Button changeDateBtn, updateBtn;

    // This is for retrieving the data to edit
    NewEvent ne;

    // For the date picker to show the current date
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    public EditEvent(){} // Empty Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_edit_event, container, false);

        DatabaseHandler db = new DatabaseHandler(getContext());
        ne = db.getEvent(titleCheck);

        TITLE_TEXT = (TextView) v.findViewById(R.id.EditEventTitle);
        TITLE_TEXT.setText(ne.getTitle());

        URL_TEXT = (EditText)v.findViewById(R.id.EditURLEvent);
        URL_TEXT.setText(ne.getURI());

        VENUE_TEXT = (EditText)v.findViewById(R.id.EditVenueEvent);
        VENUE_TEXT.setText(ne.getVenue());

        DATE_TEXT = (TextView) v.findViewById(R.id.EditDateText);
        DATE_TEXT.setText(ne.getDate());

        TYPE_TEXT = (EditText)v.findViewById(R.id.EditTypeEvent);
        TYPE_TEXT.setText(ne.getEventType());

        changeDateBtn = (Button)v.findViewById(R.id.EditDateEvent);
        updateBtn = (Button)v.findViewById(R.id.EditEventButton);

        updateBtn.setOnClickListener(this);
        changeDateBtn.setOnClickListener(this);

        return v;
    }

    private boolean validateFields(EditText[] fields)
    {
        /*
        * This iterates through an array of EditText's
        * Just to check for empty fields.
        *
        * This might be better as a For Each loop?
        */

        for(int i = 0; i < fields.length; i++)
        {
            EditText currentText = fields[i];
            if(currentText.getText().toString().length() == 0)
            {
                return false;
            }
        }
        return true;
    }

    private void updateEvent(String title, String url, String venue, String date, String type)
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());


        if(TITLE_TEXT.getText().toString().equals(titleCheck))
        {
            boolean cleanedFields = validateFields(new EditText[]{URL_TEXT, VENUE_TEXT, TYPE_TEXT});

            if(cleanedFields)
            {
                if(DATE_TEXT.length() != 0)
                {
                    // Add a new event once all fields are populated
                    //db.addEvent(new NewEvent(title, url, venue, date, type));
                    db.updateEvent(new NewEvent(title, url, venue, date, type));
                    Log.i("url",url);
                    Log.i("venue",venue);
                    Log.i("DATE",date);
                    Log.i("type",type);

                    // Prints to the screen to show completion of adding
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Your event is updated!",
                            Toast.LENGTH_LONG
                    );
                    toast.show();
                    db.close();
                }
                else
                {
                    // When the date field is empty
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Ahh! You are missing a date. Please select one",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else
            {
                // When something other than the date field is missing!
                Toast.makeText(getActivity().getApplicationContext(),
                        "Oh No! You're missing a field. Please check them all!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
        else
        {
            Log.i("UPDATE_ERROR","Title name is changed ");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        DATE_TEXT.setText(year+"-"+month+"-"+dayOfMonth);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.EditDateEvent:
                DatePickerDialog dialog = new DatePickerDialog(getContext(), this, mYear, mMonth, mDay);
                dialog.show();
                break;

            case R.id.EditEventButton:
                 updateEvent(
                        TITLE_TEXT.getText().toString(),
                        URL_TEXT.getText().toString(),
                        VENUE_TEXT.getText().toString(),
                        DATE_TEXT.getText().toString(),
                        TYPE_TEXT.getText().toString());
                break;

            default:
                break;
        }
    }
}

package com.robertmcateer.com.navigation;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    EditText TITLE_TEXT, VENUE_TEXT, URL_TEXT, TYPE_TEXT;
    TextView DATE_TEXT;
    Button dateBtn, addBtn;

    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    public AddEvent() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_add_event, container, false);

        TITLE_TEXT = (EditText)v.findViewById(R.id.AddEventTitle);
        DATE_TEXT = (TextView) v.findViewById(R.id.AddDateText);
        VENUE_TEXT = (EditText)v.findViewById(R.id.AddVenueEvent);
        TYPE_TEXT = (EditText)v.findViewById(R.id.AddTypeEvent);
        URL_TEXT = (EditText)v.findViewById(R.id.AddURLEvent);

        dateBtn = (Button)v.findViewById(R.id.AddDateEvent);
        addBtn = (Button)v.findViewById(R.id.AddEventButton);



        addBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);

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

    private void addNewEvent(String title, String date, String venue, String type, String url)
    {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        boolean cleanedFields = validateFields(new EditText[]{TITLE_TEXT, URL_TEXT, VENUE_TEXT, TYPE_TEXT});

        if(cleanedFields)
        {
            if(DATE_TEXT.length() != 0)
            {
                // Add a new event once all fields are populated
                db.addEvent(new NewEvent(title, url, venue, date, type));

                // Prints to the screen to show completion of adding
                Toast toast = Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Your event is added!",
                        Toast.LENGTH_LONG
                );
                toast.show();
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
            case R.id.AddDateEvent:
                DatePickerDialog dialog = new DatePickerDialog(getContext(), this, mYear, mMonth, mDay);
                dialog.show();
                break;

            case R.id.AddEventButton:
                addNewEvent(
                        TITLE_TEXT.toString(),
                        URL_TEXT.toString(),
                        VENUE_TEXT.toString(),
                        TYPE_TEXT.toString(),
                        URL_TEXT.toString());
                break;

            default:
                break;
        }
    }
}

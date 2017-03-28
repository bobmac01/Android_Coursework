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

    EditText title, venue, url, type;
    TextView date;
    Button dateBtn, addBtn;

    public AddEvent() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_add_event, container, false);

        title = (EditText)v.findViewById(R.id.AddEventTitle);
        date = (TextView) v.findViewById(R.id.AddDateText);
        venue = (EditText)v.findViewById(R.id.AddVenueEvent);
        type = (EditText)v.findViewById(R.id.AddTypeEvent);
        url = (EditText)v.findViewById(R.id.AddURLEvent);

        dateBtn = (Button)v.findViewById(R.id.AddDateEvent);
        addBtn = (Button)v.findViewById(R.id.AddEventButton);
        int i = date.length();

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            // I know this is not a fantastic way to do it but it works really well :)
            public void onClick(View v)
            {
                boolean cleanedFields = validateFields(new EditText[]{title, url, venue, type});

                if(cleanedFields)
                {
                    if(date.length() != 0)
                    {
                        // Add a new event once all fields are populated
                        addNewEvent(
                                title.getText().toString(),
                                date.getText().toString(),
                                venue.getText().toString(),
                                type.getText().toString(),
                                url.getText().toString());
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
        });

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
        db.addEvent(new NewEvent(title, url, venue, date, type));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Log.i("CALENDAR", "DATE SET");
        date.setText(year+"/"+month+"/"+dayOfMonth);
    }

    @Override
    public void onClick(View v)
    {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), this, 2017, 3, 26);
        dialog.show();
    }
}

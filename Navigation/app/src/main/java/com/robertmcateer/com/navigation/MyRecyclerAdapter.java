package com.robertmcateer.com.navigation;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>
{
    private List<NewEvent> allEventsList;

    MyRecyclerAdapter(List<NewEvent>allEventsList)
    {
        this.allEventsList = allEventsList;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyRecyclerAdapter.ViewHolder holder, final int i)
    {
        holder.TITLE.setText(allEventsList.get(i).getTitle());
        holder.LOCATION.setText(allEventsList.get(i).getVenue());
        holder.DATE_EVENT.setText(allEventsList.get(i).getDate());
        holder.TYPE_EVENT.setText(allEventsList.get(i).getEventType());

        holder.VIEW_BUTTON.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Store url in String var.
                // Assign to web browser intent below!
                String url = allEventsList.get(i).getURI();
                Log.i("EVENT", allEventsList.get(i).getURI());

                // Code to open browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });

        holder.EDIT_BUTTON.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                /// Log event that has been clicked
                Log.i("EVENT", allEventsList.get(i).getTitle());

                // Call for new fragment to view
                EditEvent newFragment = new EditEvent();
                FragmentTransaction transaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, newFragment);

                // This is so the EditFragment knows what to search for to populate text fields
                // Static variable
                newFragment.titleCheck = allEventsList.get(i).getTitle();

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allEventsList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView TITLE;
        private TextView LOCATION;
        private TextView DATE_EVENT;
        private TextView TYPE_EVENT;
        private Button VIEW_BUTTON;
        private Button EDIT_BUTTON;

        ViewHolder(View view)
        {
            super(view);
            TITLE = (TextView)view.findViewById(R.id.card_title);
            LOCATION = (TextView)view.findViewById(R.id.card_location);
            DATE_EVENT = (TextView)view.findViewById(R.id.card_date);
            TYPE_EVENT = (TextView)view.findViewById(R.id.card_event_type);
            VIEW_BUTTON = (Button)view.findViewById(R.id.viewBtn);
            EDIT_BUTTON = (Button)view.findViewById(R.id.editBtn);
        }
    }
}

package com.robertmcateer.com.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>
{

    private List<NewEvent> allEventsList;


    public MyRecyclerAdapter(List<NewEvent>allEventsList)
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
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int i)
    {
        holder.title.setText(allEventsList.get(i).getTitle());
        holder.location.setText(allEventsList.get(i).getVenue());
        holder.date_of_event.setText(allEventsList.get(i).getDate());
        holder.type_of_event.setText(allEventsList.get(i).getEventType());
    }

    @Override
    public int getItemCount() {
        return allEventsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView location;
        private TextView date_of_event;
        private TextView type_of_event;


        ViewHolder(View view)
        {
            super(view);
            title = (TextView)view.findViewById(R.id.card_title);
            location = (TextView)view.findViewById(R.id.card_location);
            date_of_event = (TextView)view.findViewById(R.id.card_date);
            type_of_event = (TextView)view.findViewById(R.id.card_event_type);
        }
    }
}

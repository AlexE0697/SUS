package com.example.sus.Activities.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sus.Activities.Core.FirebaseHandler;
import com.example.sus.Activities.Models.Event_Model;
import com.example.sus.R;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Event_Model> event_model_list;

    public EventAdapter(ArrayList<Event_Model> model_list, Context context) {
        this.event_model_list = model_list;
        this.context = context;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Event_Model event_model = event_model_list.get(position);

        holder.event_title.setText(event_model.getevent_title());
        holder.event_description.setText(event_model.getevent_description());
        holder.event_by.setText(event_model.getevent_by());
        holder.event_timestamp.setText(event_model.getevent_timestamp());
        holder.event_price.setText(event_model.getevent_price());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseHandler.removeEvent(context, holder.event_timestamp.getText().toString());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return event_model_list.size();
    }

    @Override
    public void onClick(View v) {
        final TextView timestamp = v.findViewById(R.id.event_timestamp_tv);

        //Toast.makeText(context, "You clicked the " + ((TextView) v.findViewById(R.id.title_tv)).getText().toString(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("timestamp", timestamp.getText().toString());

        //You need to create an intent to a new Full article activity, pass in the timestamp

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView event_title, event_description, event_by, event_timestamp, event_price;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(EventAdapter.this);
            event_title = itemView.findViewById(R.id.event_title_tv);
            event_description = itemView.findViewById(R.id.event_description_tv);
            event_by = itemView.findViewById(R.id.event_by_tv);
            event_timestamp = itemView.findViewById(R.id.event_timestamp_tv);
            event_price = itemView.findViewById(R.id.event_price_tv);
        }//end viewholder
    }//end ViewHolder class
}//end class

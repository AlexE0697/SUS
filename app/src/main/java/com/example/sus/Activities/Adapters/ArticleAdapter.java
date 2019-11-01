package com.example.sus.Activities.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sus.Activities.Models.Article_Model;
import com.example.sus.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Article_Model> article_model_list;
    private Article_Model article_model;

    public ArticleAdapter(ArrayList<Article_Model> model_list, Context context) {
        this.article_model_list = model_list;
        this.context = context;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);
        return new ArticleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        article_model = article_model_list.get(position);

        holder.title.setText(article_model.gettitle());
        holder.description.setText(article_model.getdescription());
        holder.article_by.setText(article_model.getarticle_by());
        holder.timestamp.setText(article_model.gettimestamp());
    }

    @Override
    public int getItemCount() {
        return article_model_list.size();
    }

    @Override
    public void onClick(View v) {
        final TextView timestamp = v.findViewById(R.id.timestamp_tv);

        //Toast.makeText(context, "You clicked the " + ((TextView) v.findViewById(R.id.title_tv)).getText().toString(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("timestamp", timestamp.getText().toString());

        //You need to create an intent to a new Full article activity, pass in the timestamp

    }





    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, article_by, timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(ArticleAdapter.this);
            title = itemView.findViewById(R.id.title_tv);
            description = itemView.findViewById(R.id.description_tv);
            article_by = itemView.findViewById(R.id.article_by_tv);
            timestamp = itemView.findViewById(R.id.timestamp_tv);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.setVisibility(View.GONE); //HACK!!

                    //TODO: Show a dialog here confimring that the article will be deleted then move the below code into the onclick for the yes in the dialog

                    FirebaseDatabase.getInstance().getReference().child("subjects").child("articles").child(((TextView) view.findViewById(R.id.timestamp_tv)).getText().toString().trim()).removeValue();

                    return false;
                }
            });

        }//end viewholder
    }//end ViewHolder class
}//end class

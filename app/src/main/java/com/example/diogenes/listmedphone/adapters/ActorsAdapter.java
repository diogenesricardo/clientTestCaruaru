package com.example.diogenes.listmedphone.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diogenes.listmedphone.R;
import com.example.diogenes.listmedphone.interfaces.ClickListener;
import com.example.diogenes.listmedphone.model.Actor;
import com.example.diogenes.listmedphone.util.DownloadImageTask;

import java.util.ArrayList;
import java.util.List;

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.MyViewHolder> {

    public List<Actor> actorsList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ActorsAdapter(List<Actor> actorsList) {
        this.actorsList = actorsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ActorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder vh = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_layout, parent, false));
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Actor actor = actorsList.get(position);
        holder.mTextView.setText(actor.name);
        if (actor.favorite) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(holder.imageView);
            downloadImageTask.execute(actor.avatar);
        } else {
            holder.imageView.setImageResource(R.mipmap.ic_avatar_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return actorsList != null ? actorsList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tw_name_actor);
            imageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }

    }

    public Actor getItem(int position) {
        return actorsList.get(position);
    }

}

package com.example.cacadordebugs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cacadordebugs.databinding.ItemBugBinding;

import java.util.ArrayList;

public class BugAdapter extends RecyclerView.Adapter<BugViewHolder> {

    public interface OnBugClickListener {
        void onBugClick(Bug bug);
    }

    public interface OnBugLongClickListener {
        void onBugLongClick(Bug bug);
    }

    private final ArrayList<Bug> listaBugs;
    private final OnBugClickListener clickListener;
    private final OnBugLongClickListener longClickListener;

    public BugAdapter(
            ArrayList<Bug> listaBugs,
            OnBugClickListener clickListener,
            OnBugLongClickListener longClickListener
    ) {
        this.listaBugs = listaBugs;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public BugViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        ItemBugBinding binding = ItemBugBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new BugViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BugViewHolder holder,
            int position
    ) {

        Bug bug = listaBugs.get(position);

        holder.bind(bug);

        holder.itemView.setOnClickListener(v -> {
            clickListener.onBugClick(bug);
        });

        holder.itemView.setOnLongClickListener(v -> {

            longClickListener.onBugLongClick(bug);

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaBugs.size();
    }
}
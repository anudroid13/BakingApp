package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.models.Step;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView
        .Adapter<RecipeDetailAdapter.RecyclerViewHolder> {

    private List<Step> bakingStepList;
    final private ListItemClickListener mOnClickListener;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.recipe_detail_step_cardrview_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(bakingStepList.get(position).getId() + ".  "
                + bakingStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return bakingStepList != null ? bakingStepList.size() : 0;
    }



    public RecipeDetailAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }


    public void setMasterRecipeData(List<Step> steps) {
        //set the list and notify the adapter
        this.bakingStepList = steps;
        notifyDataSetChanged();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;
        RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.shortDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }
}

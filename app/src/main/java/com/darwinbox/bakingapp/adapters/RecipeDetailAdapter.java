package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.models.Recipe;
import com.darwinbox.bakingapp.models.Step;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView
        .Adapter<RecipeDetailAdapter.RecyclerViewHolder> {

    List<Step> lSteps;
    private String recipeName;

    final private ListItemClickListener lOnClickListener;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.recipe_detail_cardview_items;

        Log.d("anudroid", "onCreateviewholder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Log.d("anudroid", "onBindViewHolder");
        holder.textRecyclerView.setText(lSteps.get(position).getId() + ". "
                + lSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return lSteps != null ? lSteps.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    public RecipeDetailAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }


    public void setMasterRecipeData(List<Recipe> recipesIn, Context context) {
        //lSteps = recipesIn;
        lSteps = recipesIn.get(0).getSteps();
        recipeName = recipesIn.get(0).getName();
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
            lOnClickListener.onListItemClick(lSteps, clickedPosition, recipeName);
        }
    }
}

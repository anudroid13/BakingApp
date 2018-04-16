package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.models.Ingredient;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView
        .Adapter<RecipeIngredientAdapter.RecyclerViewHolder> {

    private List<Ingredient> bakingIngredientList;

    public void setMasterRecipeData(List<Ingredient> ingredients) {
        //set the list and notify the adapter
        this.bakingIngredientList = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.recipe_detail_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecipeIngredientAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Ingredient ingredient = bakingIngredientList.get(position);

        holder.txtSerialNumber.setText(String.format("%d", position + 1));
        holder.txtIngredient.setText(ingredient.getIngredient());
        holder.txtQuantity.setText(String.valueOf(ingredient.getQuantity()) + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return bakingIngredientList != null ? bakingIngredientList.size() : 0;
    }

    public RecipeIngredientAdapter() {
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtSerialNumber;
        TextView txtIngredient;
        TextView txtQuantity;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            txtSerialNumber = itemView.findViewById(R.id.serial_number);
            txtIngredient = itemView.findViewById(R.id.ingredient);
            txtQuantity = itemView.findViewById(R.id.quantity_measure);
        }
    }
}

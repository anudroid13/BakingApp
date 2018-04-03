package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.models.IngredientsModel;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private ArrayList<IngredientsModel> arrayList;
    private Context mContext;

    public void setArrayList(ArrayList<IngredientsModel> arrayList) {
        if (arrayList != null) {
            this.arrayList = arrayList;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_ingredient, parent, false);
        return new IngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.ViewHolder holder,
                                 int position) {
        IngredientsModel ingredient = arrayList.get(position);

        holder.txtIngredient.setText(ingredient.getIngredient());
        holder.txtQuantity.setText("(" + ingredient.getQuantity() +
                " " + ingredient.getMeasure() + ")");
    }

    @Override
    public int getItemCount() {
        if (arrayList != null && !arrayList.isEmpty()) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView txtIngredient;
        final TextView txtQuantity;

        ViewHolder(View convertView) {
            super(convertView);
            txtIngredient = convertView.findViewById(R.id.txt_ingredient);
            txtQuantity = convertView.findViewById(R.id.txt_quantity);
        }
    }
}

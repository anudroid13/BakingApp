package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.interfaces.OnItemClickListener;
import com.darwinbox.bakingapp.models.RecipeModel;
import com.darwinbox.bakingapp.models.StepsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<RecipeModel> arrayList;
    private Context mContext;
    private String recipeName;
    final private ListItemClickListener lOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(ArrayList<RecipeModel> stepsOut,
                             int clickedItemIndex, String recipeName);
    }

    public RecipeAdapter(ListItemClickListener lOnClickListener) {
        this.lOnClickListener = lOnClickListener;
    }

    public void setArrayList(ArrayList<RecipeModel> arrayList) {
        if (arrayList != null) {
            this.arrayList = arrayList;
            recipeName = arrayList.get(0).getName();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RecipeModel recipe = arrayList.get(position);

        holder.txtRecipe.setText(recipe.getName());

        try {
            Picasso.with(mContext)
                    .load(String.valueOf(recipe.getImage()))
                    .error(R.drawable.recipe)
                    .into(holder.imgRecipe);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imgRecipe.setImageDrawable(mContext.getDrawable(R.drawable.recipe));
        }
    }

    @Override
    public int getItemCount() {
        if (arrayList != null && !arrayList.isEmpty()) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView txtRecipe;
        final ImageView imgRecipe;

        ViewHolder(View convertView) {
            super(convertView);
            txtRecipe = convertView.findViewById(R.id.txt_recipe);
            imgRecipe = convertView.findViewById(R.id.img_recipe);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(arrayList,clickedPosition,recipeName);
        }
    }
}

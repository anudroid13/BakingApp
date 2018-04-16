package com.darwinbox.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    private ArrayList<Recipe> lRecipes;
    private Context mContext;
    final private ListItemClickListener lOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }


    public void setRecipeData(ArrayList<Recipe> recipesIn) {
        lRecipes = recipesIn;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textRecipeName.setText(lRecipes.get(position).getName());
        holder.textServings.setText("Servings : " + String.valueOf(lRecipes.get(position).getServings()));
        String imageUrl = lRecipes.get(position).getImage();

        Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
        Picasso.with(mContext)
                .load(builtUri)
                .error(R.drawable.recipe)
                .into(holder.imageRecyclerView);
    }

    @Override
    public int getItemCount() {
        return lRecipes != null ? lRecipes.size() : 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView textRecipeName;
        TextView textServings;
        ImageView imageRecyclerView;


        RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecipeName = itemView.findViewById(R.id.title);
            imageRecyclerView = itemView.findViewById(R.id.recipeImage);
            textServings = itemView.findViewById(R.id.servings);

            itemView.setOnClickListener(this);
        }

        public String getRecipeName(){
            return textRecipeName.getText().toString();
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

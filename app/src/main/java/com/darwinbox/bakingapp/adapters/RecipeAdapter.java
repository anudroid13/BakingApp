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
import java.util.Objects;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    ArrayList<Recipe> lRecipes;
    Context mContext;
    final private ListItemClickListener lOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItemIndex);
    }

    public RecipeAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }


    public void setRecipeData(ArrayList<Recipe> recipesIn, Context context) {
        lRecipes = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(lRecipes.get(position).getName());
        String imageUrl = lRecipes.get(position).getImage();

        if (!Objects.equals(imageUrl, "")) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.imageRecyclerView);
        }
    }

    @Override
    public int getItemCount() {
        return lRecipes != null ? lRecipes.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView textRecyclerView;
        ImageView imageRecyclerView;


        RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.title);
            imageRecyclerView = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(lRecipes.get(clickedPosition));
        }

    }
}

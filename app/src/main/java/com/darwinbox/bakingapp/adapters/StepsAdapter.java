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
import com.darwinbox.bakingapp.interfaces.OnItemClickListener;
import com.darwinbox.bakingapp.models.StepsModel;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<StepsModel> arrayList;
    private Context mContext;
    private final OnItemClickListener listener;

    public StepsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setArrayList(ArrayList<StepsModel> arrayList) {
        if (arrayList != null) {
            this.arrayList = arrayList;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_steps, parent, false);
        return new StepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {

        StepsModel steps = arrayList.get(position);
        holder.txtShortDescription.setText(steps.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null && !arrayList.isEmpty()) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView txtShortDescription;

        ViewHolder(View convertView) {
            super(convertView);
            txtShortDescription = convertView.findViewById(R.id.txt_short_description);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemSelected(getAdapterPosition());
        }
    }
}

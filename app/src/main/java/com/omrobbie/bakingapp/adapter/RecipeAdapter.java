package com.omrobbie.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.database.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> data = new ArrayList<>();
    private RecipeClickFromWidgetListener clickFromWidgetListener;

    public RecipeAdapter() {
    }

    public interface RecipeClickFromWidgetListener {
        void onClickFromWidget(Recipe recipe);
    }

    public void setClickFromWidgetListener(RecipeClickFromWidgetListener clickFromWidgetListener) {
        this.clickFromWidgetListener = clickFromWidgetListener;
    }

    public void replaceAll(List<Recipe> data) {
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.row_recipe, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if (clickFromWidgetListener != null) {
            holder.bind(data.get(position), clickFromWidgetListener);
        } else {
            holder.bind(data.get(position), null);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

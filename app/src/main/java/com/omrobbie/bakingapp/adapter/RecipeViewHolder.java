package com.omrobbie.bakingapp.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.database.pojo.Recipe;
import com.omrobbie.bakingapp.features.recipedetail.RecipeDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_recipe)
    ImageView img;
    @BindView(R.id.tv_recipe_name)
    TextView name;
    @BindView(R.id.tv_recipe_servings)
    TextView servings;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Recipe data, @Nullable final RecipeAdapter.RecipeClickFromWidgetListener clickFromWidget) {
        if (data.getImage().equals("")) {
            Glide.with(itemView.getContext())
                    .load(getImage(data.getId()))
                    .into(img);
        } else {
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(img);
        }

        name.setText(data.getName());
        servings.setText(data.getResolvedServings());

        if (clickFromWidget == null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(itemView.getContext(), RecipeDetailActivity.class);
                    detail.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(data));
                    itemView.getContext().startActivity(detail);
                }
            });
        } else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFromWidget.onClickFromWidget(data);
                }
            });
        }

    }

    public static int getImage(int id) {
        switch (id) {
            case 1:
                return R.drawable.nutella_pie;

            case 2:
                return R.drawable.brownies;

            case 3:
                return R.drawable.yellow_cake;

            case 4:
                return R.drawable.cheesecake;

            default:
                return R.drawable.placeholder;
        }
    }
}

package com.omrobbie.bakingapp.features.recipedetailstep;

import android.content.Context;
import android.widget.ImageView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;

import com.omrobbie.bakingapp.basemvp.BaseView;
import com.omrobbie.bakingapp.database.pojo.Step;

public interface RecipeDetailStepView extends BaseView {
    void bindData(Step step);

    Context getContextFromFragment();

    void onPlayerSet(SimpleExoPlayer player, MediaSource mediaSource);

    void onImageSet();

    void onNoMediaAvailable();

    ImageView getImageView();
}

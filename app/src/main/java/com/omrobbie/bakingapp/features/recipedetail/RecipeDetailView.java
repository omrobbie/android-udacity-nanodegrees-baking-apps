package com.omrobbie.bakingapp.features.recipedetail;

import android.support.v4.app.FragmentManager;

import com.omrobbie.bakingapp.basemvp.BaseView;
import com.omrobbie.bakingapp.database.pojo.Recipe;

public interface RecipeDetailView extends BaseView {
    void bindData(Recipe recipe);

    FragmentManager getFragmentManagerFromActivity();
}

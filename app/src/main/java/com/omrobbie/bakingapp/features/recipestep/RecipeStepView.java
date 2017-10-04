package com.omrobbie.bakingapp.features.recipestep;

import android.support.v4.app.FragmentManager;

import com.omrobbie.bakingapp.basemvp.BaseView;
import com.omrobbie.bakingapp.database.pojo.Recipe;

public interface RecipeStepView extends BaseView {
    void bindData(Recipe recipe);

    FragmentManager getFragmentManagerFromFragment();
}

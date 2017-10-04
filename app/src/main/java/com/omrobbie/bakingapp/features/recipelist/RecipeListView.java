package com.omrobbie.bakingapp.features.recipelist;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.omrobbie.bakingapp.basemvp.BaseView;
import com.omrobbie.bakingapp.database.pojo.Recipe;

import java.util.List;

public interface RecipeListView extends BaseView {
    void onDataReceived(List<Recipe> data);

    void onWarningMessageReceived(String message);

    void onDataLoading();

    ContentResolver getResolver();

    Context getContext();

    LoaderManager getLoaderManagerFromActivity();
}

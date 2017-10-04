package com.omrobbie.bakingapp.features.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;

import com.omrobbie.bakingapp.basemvp.BaseView;
import com.omrobbie.bakingapp.database.pojo.Recipe;

import java.util.List;

public interface ConfigWidgetView extends BaseView {
    void onBind(List<Recipe> data);

    void onComplete();

    Context getContextFromAct();

    ContentResolver getContentResolverFromAct();

    LoaderManager getLoaderFromAct();

    SharedPreferences getPrefs();
}

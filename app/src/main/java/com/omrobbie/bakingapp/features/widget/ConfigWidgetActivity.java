package com.omrobbie.bakingapp.features.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.adapter.RecipeAdapter;
import com.omrobbie.bakingapp.database.pojo.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigWidgetActivity extends AppCompatActivity implements ConfigWidgetView, RecipeAdapter.RecipeClickFromWidgetListener {

    @BindView(R.id.rv_config_widget)
    RecyclerView rv;

    private RecipeAdapter adapter;
    private ConfigWisgetPresenter presenter;

    private int widgetID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);
        ButterKnife.bind(this);

        widgetID = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        adapter = new RecipeAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        onDetachView();
    }

    @Override
    public void onClickFromWidget(Recipe recipe) {
        presenter.saveData(widgetID, recipe);
    }

    @Override
    public void onAttachView() {
        presenter = new ConfigWisgetPresenter();
        presenter.onAttach(this);

    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onBind(List<Recipe> data) {
        adapter.replaceAll(data);
        adapter.setClickFromWidgetListener(ConfigWidgetActivity.this);
    }

    @Override
    public void onComplete() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public Context getContextFromAct() {
        return ConfigWidgetActivity.this;
    }

    @Override
    public ContentResolver getContentResolverFromAct() {
        return getContentResolver();
    }

    @Override
    public LoaderManager getLoaderFromAct() {
        return getSupportLoaderManager();
    }

    @Override
    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }
}

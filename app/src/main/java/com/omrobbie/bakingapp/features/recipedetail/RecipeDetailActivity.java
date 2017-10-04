package com.omrobbie.bakingapp.features.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.database.pojo.Recipe;
import com.omrobbie.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import com.omrobbie.bakingapp.features.recipestep.RecipeStepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailView,
        RecipeStepFragment.OnStepSelected,
        RecipeDetailStepFragment.StepNavigationClickListener {

    private static final String JSON_STRING = "json_string";

    private RecipeDetailPresenter presenter;
    private String jsonStr;
    private boolean isTwoPane = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.container)
    FrameLayout container;

    @Nullable
    @BindView(R.id.container_left)
    FrameLayout containerLeft;
    @Nullable
    @BindView(R.id.container_right)
    FrameLayout containerRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            jsonStr = savedInstanceState.getString(JSON_STRING);
        } else {
            jsonStr = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        }

        onAttachView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(JSON_STRING, jsonStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeDetailPresenter();
        presenter.onAttach(this);

        if (container == null) {
            isTwoPane = true;
        }

        if (jsonStr != null) {
            presenter.getRecipeModel(jsonStr);
            if (isTwoPane) {
                presenter.addFragments(
                        presenter.getStepFragment(jsonStr, isTwoPane),
                        presenter.getStepDetailFragment(null, 0, 0, 0, 0)
                );
            } else {
                presenter.addFragment(presenter.getStepFragment(jsonStr, isTwoPane));
            }
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void bindData(Recipe recipe) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }
    }

    @Override
    public FragmentManager getFragmentManagerFromActivity() {
        return getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        if (isTwoPane) {
            RecipeDetailActivity.this.finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                RecipeDetailActivity.this.finish();
            }
        }
    }

    @Override
    public void onstepselected(String stepJson) {
        presenter.changeFragmentRight(presenter.getStepDetailFragment(stepJson, 0, 0, 0, 0));
    }

    @Override
    public void onNavigateStep(int targetPosition, int totalPosition) {
        presenter.replaceFragment(
                presenter.getStepDetailFragment(
                        presenter.getStepJsonByIndex(jsonStr, targetPosition),
                        targetPosition,
                        totalPosition,
                        presenter.getPreviousStepIDByTargetID(jsonStr, targetPosition),
                        presenter.getNextStepIDByTargetID(jsonStr, targetPosition)
                )
        );
    }
}

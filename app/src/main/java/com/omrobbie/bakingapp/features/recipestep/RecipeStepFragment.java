package com.omrobbie.bakingapp.features.recipestep;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.adapter.StepAdapter;
import com.omrobbie.bakingapp.database.pojo.Recipe;
import com.omrobbie.bakingapp.database.pojo.Step;
import com.omrobbie.bakingapp.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment implements RecipeStepView, StepAdapter.OnStepClick {

    private static final String JSON_STRING = "json_string";

    private RecipeStepPresenter presenter;
    private Unbinder unbinder;
    private StepAdapter stepAdapter;
    private OnStepSelected onStepSelected;
    private String json;

    @BindView(R.id.tv_ingredients)
    TextView ingredients;
    @BindView(R.id.rv_steps)
    RecyclerView rvStep;

    public RecipeStepFragment() {
    }

    public interface OnStepSelected {
        void onstepselected(String stepJson);
    }

    public static RecipeStepFragment newInstance(String json, boolean isTwoPane) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.KEY_IS_TWO_PANE, isTwoPane);
        bundle.putString(Constant.KEY_RECIPE, json);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepSelected = (OnStepSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            json = savedInstanceState.getString(JSON_STRING);
        } else {
            json = getArguments().getString(Constant.KEY_RECIPE, null);
        }

        onAttachView();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(JSON_STRING, json);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            json = savedInstanceState.getString(JSON_STRING);
        } else {
            json = getArguments().getString(Constant.KEY_RECIPE, null);
        }
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeStepPresenter();
        presenter.onAttach(this);

        if (json != null) {
            setupRV();
            presenter.getRecipeModel(json);
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        onDetachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void bindData(Recipe recipe) {
        ingredients.setText(presenter.getEachIngredient(recipe.getIngredients()));
        stepAdapter.replaceAll(recipe.getSteps());
        stepAdapter.addClickListener(this);
    }

    @Override
    public FragmentManager getFragmentManagerFromFragment() {
        return getFragmentManager();
    }

    private void setupRV() {
        stepAdapter = new StepAdapter();
        rvStep.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStep.setAdapter(stepAdapter);
        rvStep.addItemDecoration(new DividerItemDecoration(rvStep.getContext(),
                LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onStepClicked(Step step,
                              int stepNumber,
                              int totalSteps,
                              int previousStepID,
                              int nextStepID) {
        if (getArguments().getBoolean(Constant.KEY_IS_TWO_PANE, false)) {
            onStepSelected.onstepselected(presenter.getJsonStep(step));
        } else {
            presenter.addFragment(
                    presenter.getDetailStepFragment(
                            presenter.getJsonStep(step),
                            stepNumber,
                            totalSteps,
                            previousStepID,
                            nextStepID
                    )
            );
        }
    }
}

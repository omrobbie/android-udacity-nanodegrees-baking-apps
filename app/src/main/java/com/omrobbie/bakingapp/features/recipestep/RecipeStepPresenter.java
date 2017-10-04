package com.omrobbie.bakingapp.features.recipestep;

import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.basemvp.BasePresenter;
import com.omrobbie.bakingapp.database.pojo.IngredientItem;
import com.omrobbie.bakingapp.database.pojo.Recipe;
import com.omrobbie.bakingapp.database.pojo.Step;
import com.omrobbie.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;

import java.util.List;

public class RecipeStepPresenter implements BasePresenter<RecipeStepView> {

    private RecipeStepView view;
    private Gson gson = new Gson();

    @Override
    public void onAttach(RecipeStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        gson = null;
        view = null;
    }

    void getRecipeModel(String json) {
        view.bindData(gson.fromJson(json, Recipe.class));
    }

    String getJsonStep(Step step) {
        return gson.toJson(step);
    }

    public String getEachIngredient(List<IngredientItem> data) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getIngredient();
            String measure = data.get(i).getMeasure();
            String qty = String.valueOf(data.get(i).getQuantity());

            String strToAppend = "√ " + name + "(" + qty + " " + measure + ")";

            if (i == data.size() - 1) {
                stringBuilder.append(strToAppend);
            } else {
                stringBuilder.append(strToAppend).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    void addFragment(Fragment fragment) {
        view.getFragmentManagerFromFragment().beginTransaction()
                .replace(R.id.container, fragment, RecipeDetailStepFragment.class.getSimpleName())
                .addToBackStack(RecipeDetailStepFragment.class.getSimpleName())
                .commit();
        view.getFragmentManagerFromFragment().executePendingTransactions();
    }

    Fragment getDetailStepFragment(String json,
                                   int currentStep,
                                   int totalStep,
                                   int previousStep,
                                   int nextStep) {
        return RecipeDetailStepFragment.newInstance(json, currentStep, totalStep, previousStep, nextStep);
    }
}

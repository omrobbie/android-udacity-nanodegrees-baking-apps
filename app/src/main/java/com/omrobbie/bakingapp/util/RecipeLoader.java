package com.omrobbie.bakingapp.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.omrobbie.bakingapp.database.pojo.IngredientItem;
import com.omrobbie.bakingapp.database.pojo.Recipe;
import com.omrobbie.bakingapp.database.pojo.Step;

import java.util.ArrayList;
import java.util.List;

import static com.omrobbie.bakingapp.database.contract.IngredientContract.IngredientEntry;
import static com.omrobbie.bakingapp.database.contract.RecipeContract.RecipeEntry;
import static com.omrobbie.bakingapp.database.contract.StepContract.StepEntry;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String TAG = RecipeLoader.class.getSimpleName();
    private ContentResolver resolver;

    public RecipeLoader(Context context, ContentResolver resolver) {
        super(context);
        this.resolver = resolver;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes = new ArrayList<>();

        Cursor recipeCursor = resolver.query(
                RecipeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (recipeCursor != null) {
            if (recipeCursor.getCount() > 0) {
                if (recipeCursor.moveToFirst()) {
                    do {
                        Recipe recipe = new Recipe();
                        recipe.setId(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_ID)
                        ));

                        recipe.setImage(recipeCursor.getString(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_IMAGE)
                        ));

                        recipe.setName(recipeCursor.getString(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_NAME)
                        ));

                        recipe.setServings(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_SERVINGS)
                        ));

                        recipe.setIngredients(getIngredients(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_ID)
                        )));

                        recipe.setSteps(getSteps(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_ID)
                        )));

                        recipes.add(recipe);
                    } while (recipeCursor.moveToNext());
                }
            }

            recipeCursor.close();
        }

        return recipes;
    }

    private List<IngredientItem> getIngredients(int recipeID) {
        List<IngredientItem> result = new ArrayList<>();

        Cursor cursor = resolver.query(
                URIWithID(IngredientEntry.CONTENT_URI, recipeID),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        IngredientItem ingredient = new IngredientItem();
                        ingredient.setQuantity(cursor.getDouble(
                                cursor.getColumnIndex(IngredientEntry.INGREDIENT_QUANTITY)
                        ));

                        ingredient.setMeasure(cursor.getString(
                                cursor.getColumnIndex(IngredientEntry.INGREDIENT_MEASURE)
                        ));

                        ingredient.setIngredient(cursor.getString(
                                cursor.getColumnIndex(IngredientEntry.INGREDIENT)
                        ));

                        result.add(ingredient);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }

        return result;
    }

    private List<Step> getSteps(int recipeID) {
        List<Step> result = new ArrayList<>();

        Cursor cursor = resolver.query(
                URIWithID(StepEntry.CONTENT_URI, recipeID),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        Step step = new Step();
                        step.setId(cursor.getInt(
                                cursor.getColumnIndex(StepEntry.STEP_ID)
                        ));

                        step.setShortDescription(cursor.getString(
                                cursor.getColumnIndex(StepEntry.STEP_SHORT_DESC)
                        ));

                        step.setDescription(cursor.getString(
                                cursor.getColumnIndex(StepEntry.STEP_DESCRIPTION)
                        ));

                        step.setVideoURL(cursor.getString(
                                cursor.getColumnIndex(StepEntry.STEP_VIDEO_URL)
                        ));

                        step.setThumbnailURL(cursor.getString(
                                cursor.getColumnIndex(StepEntry.STEP_THUMBNAIL_URL)
                        ));

                        result.add(step);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }

        return result;
    }

    private Uri URIWithID(Uri uri, long id) {
        return ContentUris.withAppendedId(uri, id);
    }
}

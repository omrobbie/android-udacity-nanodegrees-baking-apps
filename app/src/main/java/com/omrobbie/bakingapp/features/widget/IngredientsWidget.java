package com.omrobbie.bakingapp.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.omrobbie.bakingapp.R;
import com.omrobbie.bakingapp.database.contract.IngredientContract;
import com.omrobbie.bakingapp.database.pojo.IngredientItem;
import com.omrobbie.bakingapp.features.recipedetail.RecipeDetailActivity;
import com.omrobbie.bakingapp.util.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.omrobbie.bakingapp.database.contract.IngredientContract.IngredientEntry.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

//    private static final String TAG = IngredientsWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int recipeID, String recipeName, String jsonRecipe, int appWidgetId) {

        String ingredient = getEachIngredient(getIngredients(context, recipeID));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
        views.setTextViewText(R.id.tv_widget_ingredients, ingredient);

        Intent recipeDetail = new Intent(context, RecipeDetailActivity.class);
        recipeDetail.putExtra(Intent.EXTRA_TEXT, jsonRecipe);
        recipeDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeDetail, PendingIntent.FLAG_CANCEL_CURRENT);
        if (jsonRecipe != null || !jsonRecipe.equals("") || jsonRecipe.length() != 0) {
            views.setOnClickPendingIntent(R.id.rootWidget, pendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int recipeID = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(Constant.WIDGET_SELECTED_RECIPE_ID, 1);

        String recipeName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constant.WIDGET_SELECTED_RECIPE_NAME, "");

        String jsonRecipe = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constant.KEY_RECIPE, "");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeID, recipeName, jsonRecipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static String getEachIngredient(List<IngredientItem> data) {
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

    private static List<IngredientItem> getIngredients(Context context, int id) {
        List<IngredientItem> result = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(CONTENT_URI, id),
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
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT_QUANTITY)
                        ));
                        ingredient.setMeasure(cursor.getString(
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT_MEASURE)
                        ));
                        ingredient.setIngredient(cursor.getString(
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT)
                        ));

                        result.add(ingredient);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }

        return result;
    }
}

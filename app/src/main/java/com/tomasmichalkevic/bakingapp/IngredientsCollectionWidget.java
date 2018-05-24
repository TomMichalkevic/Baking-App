/*
 * PROJECT LICENSE
 *
 * This project was submitted by Tomas Michalkevic as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2018 Tomas Michalkevic
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tomasmichalkevic.bakingapp;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.tomasmichalkevic.bakingapp.data.Ingredient;
import com.tomasmichalkevic.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

@SuppressWarnings("ALL")
public class IngredientsCollectionWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (appWidgetIds != null) {
            for (int appWidgetId : appWidgetIds) {
                Intent intent = new Intent(context, UpdateWidgetService.class);

                intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setAction("FROM WIDGET PROVIDER");
                context.startService(intent);
            }
        }
    }

    @SuppressLint("Registered")
    public static class UpdateWidgetService extends IntentService {

        String data = "";

        public UpdateWidgetService() {
            super("UpdateWidgetService");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(UpdateWidgetService.this);

            int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);

            data = intent.getStringExtra("data");

            if (incomingAppWidgetId != INVALID_APPWIDGET_ID) {
                try {
                    updateAppWidget(appWidgetManager, incomingAppWidgetId,
                            intent);
                } catch (NullPointerException ignored) {
                }

            }
        }

        void updateAppWidget(AppWidgetManager appWidgetManager,
                             int appWidgetId, Intent intent) {

            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);

            setRemoteAdapter(this, views, data);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views, String data) {
            Intent intent = new Intent(context, IngredientsWidgetService.class);
            intent.putExtra("data", data);
            views.setRemoteAdapter(R.id.widget_list, intent);
        }
    }

    public static class IngredientsWidgetService extends RemoteViewsService {
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new IngredientsWidgetDataProvider(this, intent);
        }
    }

    public static class IngredientsWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

        Context context;
        ArrayList<String> ingredientsList = new ArrayList<>();
        Intent intent;

        public IngredientsWidgetDataProvider(Context context, Intent intent){
            this.context = context;
            this.intent = intent;
        }

        public void init(){
            Recipe recipe = new Gson().fromJson(intent.getStringExtra("data"), Recipe.class);
            List<Ingredient> ingredientArrayList = recipe.getIngredients();
            ingredientsList.clear();
            for(Ingredient ingredient: ingredientArrayList){
                ingredientsList.add(ingredient.getIngredient() + " " + ingredient.getQuantity() + " " + ingredient.getMeasure());
            }
        }

        @Override
        public void onCreate() {
            if(intent.hasExtra("data")){
                init();
            }
        }

        @Override
        public void onDataSetChanged() {
            if(intent.hasExtra("data")){
                init();
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews view = new RemoteViews(context.getPackageName(),
                    android.R.layout.simple_list_item_1);
            view.setTextViewText(android.R.id.text1, ingredientsList.get(position));
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}

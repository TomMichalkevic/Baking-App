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

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tomasmichalkevic.bakingapp.data.Ingredient;
import com.tomasmichalkevic.bakingapp.data.Recipe;
import com.tomasmichalkevic.bakingapp.data.Step;
import com.tomasmichalkevic.bakingapp.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String recipeListUrl = BuildConfig.RECIPE_LIST_LINK;

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private RecipeCardAdapter recipeCardAdapter;

    private RecyclerView.LayoutManager mLayoutManagerRecipes;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        recipeCardAdapter = new RecipeCardAdapter(recipes, new RecipeCardAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Recipe view) {
                Toast.makeText(MainActivity.this, "Clicked on card! " + view.getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(view).toString());
                startActivity(intent);
            }
        });

        mLayoutManagerRecipes = new LinearLayoutManager(this);

//        mLayoutManagerRecipes = new GridLayoutManager(this, 2);

        recipesRecyclerView.setLayoutManager(mLayoutManagerRecipes);

        recipesRecyclerView.setAdapter(recipeCardAdapter);

        recipes.clear();

        if (isNetworkAvailable(this)) {
            recipes.addAll(getRecipes());
            //recipes.add(getMock());
        } else {
            Toast.makeText(this, "Cannot refresh due to no network!",
                    Toast.LENGTH_LONG).show();
        }

        recipeCardAdapter.notifyDataSetChanged();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static ArrayList<Recipe> getRecipes() {
        JsonUtil jsonUtil = new JsonUtil();
        ArrayList<Recipe> result = new ArrayList<>();
        String json = "";

        try {
            json = jsonUtil.execute(recipeListUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(LOG_TAG, "getRecipes: ", e);
        } finally {

            if (json.equals("")) {
                return result;
            }
            Recipe[] recipes = new Gson().fromJson(json, Recipe[].class);
            result.addAll(Arrays.asList(recipes));
            return result;
        }
    }

    public Recipe getMock() {
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step(1, "Buy stuff to cook", "Pretty self explanatory step, buy stuff", "Youtube", ""));
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient(1.1, "oz", "Sugar"));
        return new Recipe(1,
                "Nutella",
                ingredients,
                steps,
                1,
                "");
    }
}

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
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tomasmichalkevic.bakingapp.RecipeDetailsFragment.OnListItemClickListener;
import com.tomasmichalkevic.bakingapp.data.Ingredient;
import com.tomasmichalkevic.bakingapp.data.Recipe;
import com.tomasmichalkevic.bakingapp.data.Step;

/**
 * Created by tomasmichalkevic on 29/04/2018.
 */

public class DetailsActivity extends Activity implements OnListItemClickListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.i("TESTING", "onCreate: " + getIntent().hasExtra("data"));
        Recipe recipe = new Gson().fromJson(getIntent().getStringExtra("data"),
                Recipe.class);

        if (findViewById(R.id.tablet_recipe_details_layout) != null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mTwoPane = true;

            FragmentManager fragmentManager = getFragmentManager();

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setData(recipe.getIngredients().toArray(new Ingredient[]{}));

            fragmentManager.beginTransaction()
                    .add(R.id.step_details_fragment, ingredientsFragment).commit();
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onListItemSelected(String json) {
        if(json.equals("ingredients")){
            if(getIntent().hasExtra("data")){
                Recipe recipe = new Gson().fromJson(getIntent().getStringExtra("data"),
                        Recipe.class);
                if(mTwoPane){
                    Log.i("TESTING ", "onListItemSelected:setting stuff up for ingredient two pane");
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    ingredientsFragment.setData(recipe.getIngredients().toArray(new Ingredient[]{}));
                    getFragmentManager().beginTransaction()
                            .replace(R.id.step_details_fragment, ingredientsFragment)
                            .commit();
                }else{
                    Bundle bundle = new Bundle();
                    Intent intent;
                    bundle.putString("data", new Gson().toJson(recipe.getIngredients()));
                    intent = new Intent(this, IngredientsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }else{
            if(mTwoPane){
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setData(new Gson().fromJson(json, Step.class));
                getFragmentManager().beginTransaction()
                        .replace(R.id.step_details_fragment, stepDetailsFragment)
                        .commit();

            }else{
                Bundle bundle = new Bundle();
                Intent intent;
                bundle.putString("data", json);
                intent = new Intent(this, RecipeStepActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}

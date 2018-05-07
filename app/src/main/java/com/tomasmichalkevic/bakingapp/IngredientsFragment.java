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

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tomasmichalkevic.bakingapp.data.Ingredient;
import com.tomasmichalkevic.bakingapp.data.Recipe;
import com.tomasmichalkevic.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tomasmichalkevic on 03/05/2018.
 */

public class IngredientsFragment extends Fragment {

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private IngredientCardAdapter ingredientCardAdapter;

    private RecyclerView.LayoutManager mLayoutManagerRecipes;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView ingredientRecyclerView;

    private Ingredient[] ingredients = {};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ButterKnife.bind(this, rootView);
        String data = getActivity().getIntent().getExtras().getString("data");
        Ingredient[] ingredientsArray = new Gson().fromJson(data, Ingredient[].class);

        ingredientCardAdapter = new IngredientCardAdapter(ingredientList);

        mLayoutManagerRecipes = new LinearLayoutManager(getContext());

        ingredientRecyclerView.setLayoutManager(mLayoutManagerRecipes);

        ingredientRecyclerView.setAdapter(ingredientCardAdapter);

        ingredientList.clear();

        Collections.addAll(ingredientList, ingredientsArray);

        ingredientCardAdapter.notifyDataSetChanged();
        return rootView;
    }

    public void setData(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }
}

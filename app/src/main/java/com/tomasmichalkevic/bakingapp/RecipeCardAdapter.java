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

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tomasmichalkevic.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tomasmichalkevic on 25/04/2018.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private ItemClickListener listener;

    public RecipeCardAdapter(List<Recipe> recipeList, ItemClickListener listener){
        this.recipeList = recipeList;
        this.listener = listener;
    }

    public interface ItemClickListener {
        public void onItemClick(Recipe view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_recipe) TextView recipeTitle;
        @BindView(R.id.tv_ingredient_count) TextView ingredientsCount;
        @BindView(R.id.tv_step_count) TextView stepsCount;
        @BindView(R.id.tv_serving_count) TextView servingsCount;
        @BindView(R.id.recipe_card_view) CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe, final ItemClickListener listener){
            recipeTitle.setText(recipe.getName());
            ingredientsCount.setText(String.valueOf(recipe.getIngredients().size()));
            stepsCount.setText(String.valueOf(recipe.getSteps().size()));
            servingsCount.setText(String.valueOf(recipe.getServings()));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(recipe);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipeList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}

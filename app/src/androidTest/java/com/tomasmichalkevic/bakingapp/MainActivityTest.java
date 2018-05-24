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

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void useAppContext(){
        Context appContext = getTargetContext();
        assertEquals("com.tomasmichalkevic.bakingapp", appContext.getPackageName());
    }

    @Test
    public void instantiateMainActivity(){
        MainActivity mainActivity = mActivityTestRule.getActivity();
        assertNotNull(mainActivity);
    }

    @Test
    public void checkLoadedRecipes(){
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.
                <RecipeCardAdapter.ViewHolder>scrollTo(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.
                <RecipeCardAdapter.ViewHolder>scrollTo(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.
                <RecipeCardAdapter.ViewHolder>scrollTo(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.
                <RecipeCardAdapter.ViewHolder>scrollTo(hasDescendant(withText("Cheesecake"))));
    }

    @Test
    public void opensDetailsActivity(){
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.
                <RecipeCardAdapter.ViewHolder>actionOnItemAtPosition(1, click()));
        intended(hasComponent(DetailsActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource!=null){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }

    }
}

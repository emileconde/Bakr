package com.example.android.bakr.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakr.R;
import com.example.android.bakr.Widget.WidgetUpdateService;
import com.example.android.bakr.model.Ingredient;
import com.example.android.bakr.model.Step;

import java.util.ArrayList;
import java.util.List;

/**Second Activity. Comprised of two fragments that are both recyclerView. One displays the
 * ingredients and the other displays the steps*/

public class DishActivity extends AppCompatActivity implements StepsFragments.DishClick,
        IngredientFragment.IngredientsReady
{
    public static final String DESCRIPTION_INTENT_KEY = "mDescription";
    public static final String VIDEO_URL_INTENT_KEY = "mVideoUrl";
    public static final String STEPS_INTENT_KEY = "steps";
    public static final String POSITION_INTENT_KEY = "position";
    public static final String STEPS_INDEX_KEY = "step_list";
    public static final String INGREDIENT_INTENT_SERVICE_KEY = "ingredients";
    public static final String INGREDIENT_BUNDLE = "ingredient_bundle";
    final int FIRST_STEP = 0;
    String mDescription;
    String mVideoUrl = "";
    public static boolean mTwoPane;
    private List<Step> mSteps = new ArrayList<>();
    private int mPosition = 0;
    Bundle mSavedInstances;
    //StepsFragment is declared here because it needs to be accessed in onResume to get access to
    // the List of steps which is needed to automatically play the first video on tablets. /
    StepsFragments stepsFragments = new StepsFragments();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate: ONCREATE");
        mSavedInstances = savedInstanceState;
        setContentView(R.layout.activity_dish);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.ingredient_step_layout) != null) {
            mTwoPane = true;
            Intent intent = getIntent();
                //The name of the dish that was clicked on in MainActivity
            String name = intent.getStringExtra("dish");
            if (savedInstanceState == null) {
                IngredientFragment ingredientFragment = new IngredientFragment();
                ingredientFragment.setDishName(name);
                fragmentManager.beginTransaction().
                        add(R.id.ingredient_container, ingredientFragment)
                        .commit();

                stepsFragments.setDishName(name);
                mSteps = stepsFragments.getSteps();
                fragmentManager.beginTransaction()
                        .add(R.id.steps_container, stepsFragments)
                        .commit();

            }
        }else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                Intent intent = getIntent();
                //The name of the dish that was clicked on in MainActivity
                String name = intent.getStringExtra("dish");
                IngredientFragment ingredientFragment = new IngredientFragment();
                ingredientFragment.setDishName(name);
                fragmentManager.beginTransaction().
                        add(R.id.ingredient_container, ingredientFragment)
                        .commit();

                StepsFragments stepsFragments = new StepsFragments();
                stepsFragments.setDishName(name);
                fragmentManager.beginTransaction()
                        .add(R.id.steps_container, stepsFragments)
                        .commit();
            }
        }
    }
            //Back button
          @Override
          public boolean onOptionsItemSelected(MenuItem item) {
              if(item.getItemId()== android.R.id.home) {
                  finish();
              }
              return super.onOptionsItemSelected(item);
          }

          //*Communication interface with StepsFragment. This is both the entire list
          // and the particular item that was clicked on. The entire list of steps is
          // sent to the to StepDetailActivity to make it possible to from one step to
          // another by clicking the next or previous button*/
          @Override
          public void onDishClick(int position, List<Step> steps) {
              mDescription = steps.get(position).getDescription();
              mVideoUrl = steps.get(position).getVideoUrl();
              mPosition = position;
              mSteps = steps;
              FragmentManager fragmentManager = getSupportFragmentManager();
              StepDetailFragment stepDetailFragment = new StepDetailFragment();
              if(mTwoPane){
                  stepDetailFragment.setDescription(mDescription);
                  stepDetailFragment.setMediaUri(Uri.parse(mVideoUrl));
                  stepDetailFragment.toggle(mSteps, mPosition);
                  fragmentManager.beginTransaction()
                              .replace(R.id.step_detail_container, stepDetailFragment)
                              .commit();
              }else{
              Intent intent = new Intent(DishActivity.this, StepDetailActivity.class);
              intent.putExtra(DESCRIPTION_INTENT_KEY, mDescription);
              intent.putExtra(VIDEO_URL_INTENT_KEY, mVideoUrl);
              intent.putParcelableArrayListExtra(STEPS_INTENT_KEY, (ArrayList<? extends Parcelable>)
                      steps);
              intent.putExtra(POSITION_INTENT_KEY, position);
              startActivity(intent);
              }
          }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSteps = savedInstanceState.getParcelableArrayList(STEPS_INDEX_KEY);
        Log.d("TAG", "onRestoredInstanceState: Restored"+mSteps.get(0).getVideoUrl());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_INDEX_KEY, (ArrayList<? extends Parcelable>) mSteps);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Walk around nullpointerException. 'stepsFragments.getSteps()' would be null in OnCreate
        // so stepsFragments.getSteps() is only called when the fragment has been created and every
        // variable in it has been assigned./
        if(mTwoPane) {
            mSteps = stepsFragments.getSteps();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setDescription(mSteps.get(FIRST_STEP).getDescription());
            stepDetailFragment.setMediaUri(Uri.parse(mSteps.get(FIRST_STEP).getVideoUrl()));
            stepDetailFragment.toggle(mSteps, mPosition);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onIngredientsReady(List<Ingredient> ingredients) {
        Ingredient[] ingredientArray = new Ingredient[ingredients.size()];
        ingredientArray = ingredients.toArray(ingredientArray) ;
        Intent intent = new Intent(this, WidgetUpdateService.class);
       Bundle bundle = new Bundle();
       bundle.putParcelableArray(INGREDIENT_INTENT_SERVICE_KEY, ingredientArray);
       intent.putExtra(INGREDIENT_BUNDLE, bundle);
       intent.setAction(WidgetUpdateService.ACTION_UPDATE);
       startService(intent);
    }
}

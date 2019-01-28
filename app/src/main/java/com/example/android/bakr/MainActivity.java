package com.example.android.bakr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakr.model.Dish;
import com.example.android.bakr.ui.DishActivity;
import com.example.android.bakr.ui.MasterListFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MasterListFragment.MasterListFragmentClicked
{

    public static final String DISH_INFO_TAG = "dish";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //*Item in the list that was clicked on. This helps to know what to display in the following
    // Activity*/
    @Override
    public void onclickCallback(int position, List<Dish> dishes) {
        Intent intent = new Intent(MainActivity.this, DishActivity.class);
        intent.putExtra(DISH_INFO_TAG, dishes.get(position).getName());
        startActivity(intent);
    }

}

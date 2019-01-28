package com.example.android.bakr.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Step;

import java.util.List;

/**Third activity. Displays the video and description of a particular step.*/

public class StepDetailActivity extends AppCompatActivity{
    String mVideoUrl;
    private static final int DEFAULT_VALUE = 0;
    public static final String VIDEO_URL_INDEX = "url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

            //Intent from DishActivity
            Intent intent = getIntent();
            String description = intent.getStringExtra("mDescription");
            mVideoUrl = intent.getStringExtra("mVideoUrl");
            int position = intent.getIntExtra("position", DEFAULT_VALUE);
            List<Step> steps = intent.getParcelableArrayListExtra("steps");
        Toast.makeText(this, "Position "+position, Toast.LENGTH_SHORT).show();

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setDescription(description);
            stepDetailFragment.setPosition(position);
            stepDetailFragment.setMediaUri(Uri.parse(mVideoUrl));
            stepDetailFragment.toggle(steps, position);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId()== android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        }
}

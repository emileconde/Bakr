package com.example.android.bakr.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.android.bakr.R;
import com.example.android.bakr.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conde on 5/15/2018.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    String mDescription;
    private static final String DESCRIPTION_INDEX = "description";
    private static final String POSITION_INDEX = "position";
    private static final String NUMBER_OF_STEPS_INDEX = "steps";
    private int mNumberOfSteps;
    private SimpleExoPlayer mExoPlayer;
    private Uri mMediaUri;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    public static final String TAG = StepDetailFragment.class.getSimpleName() ;
    private List<Step> mSteps = new ArrayList<>();
    private int mPosition;
    private FloatingActionButton mFabNext;
    private FloatingActionButton mFabPrevious;
    private SimpleExoPlayerView mPlayerView;
    private TextView mPositionTextView;
    TextView mDescriptionTextView;
    final int EXO_PLAYER_MARGIN_START = 0;
    final int EXO_PLAYER_MARGIN_END = 0;
    final int EXO_PLAYER_HEIGHT = 800;
    public StepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            mDescription = savedInstanceState.getString(DESCRIPTION_INDEX);
            mPosition = savedInstanceState.getInt(POSITION_INDEX);
            mNumberOfSteps = savedInstanceState.getInt(NUMBER_OF_STEPS_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_step_detail,container, false);
        mPlayerView = rootView.findViewById(R.id.exo_playerView);
        mDescriptionTextView = rootView.findViewById(R.id.description);
        mPositionTextView = rootView.findViewById(R.id.tv_position);
        mFabNext = rootView.findViewById(R.id.fab_next);
        mFabPrevious = rootView.findViewById(R.id.fab_previous);
        mNumberOfSteps = mSteps.size() ;
        mPositionTextView.setText(String.format("%s/%s", String.valueOf(mPosition + 1),String.
                valueOf(mNumberOfSteps)));
        setVisibility();
        mFabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPosition != (mSteps.size() -1 )) {
                    mPosition++;
                    mFabPrevious.setVisibility(View.VISIBLE);
                    mDescriptionTextView.setText(mSteps.get(mPosition).getDescription());
                    mPositionTextView.setText(String.format("%s/%s", String.valueOf(mPosition + 1),
                            String.valueOf(mSteps.size())));
                    releaseVideo();
                    setMediaUri(Uri.parse(mSteps.get(mPosition).getVideoUrl()));
                    playVideo(mMediaUri);
                }
                setVisibility();
            }
        });

        mFabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPosition != 0) {
                    mPosition--;
                    mFabNext.setVisibility(View.VISIBLE);
                    mDescriptionTextView.setText(mSteps.get(mPosition).getDescription());
                    mPositionTextView.setText(String.format("%s/%s", String.valueOf(mPosition + 1),
                            String.valueOf(mSteps.size())));
                    releaseVideo();
                    setMediaUri(Uri.parse(mSteps.get(mPosition).getVideoUrl()));
                    playVideo(mMediaUri);
                }
                setVisibility();
            }
        });

        mDescriptionTextView.setText(mDescription);

        playVideo(mMediaUri);
        setupMediaSession();

        return rootView;
    }


    public void setDescription(String description){
        this.mDescription = description;
    }

    //Makes it possible to skip from one step to another resetting
    // fields like shortDescription.
    //Will be used in conjunction with the arrayList of Steps*/
    public void toggle(List<Step> stepList, int position){
       this.mSteps = stepList;
       this.mPosition = position;
    }
   
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseVideo();
    }

    public void releaseVideo(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    public void setupMediaSession(){
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }



    public void setMediaUri(Uri uri){
        mMediaUri = uri;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {
    }




    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    //Makes button visible and invisible at the beginning and the end of the list
    public void setVisibility(){
        if (mPosition == (mSteps.size() - 1)){
            mFabNext.setVisibility(View.INVISIBLE);
        }
        if(mPosition == 0) {
            mFabPrevious.setVisibility(View.INVISIBLE);
        }
    }

    public void playVideo(Uri uri){
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_INDEX, mPosition);
        outState.putString(DESCRIPTION_INDEX, mDescription);
        outState.putInt(NUMBER_OF_STEPS_INDEX, mNumberOfSteps);
    }



    //Takes care of the orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        // Only take fullscreen when app is running on phones
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !DishActivity.mTwoPane) {
            //Hide UI elements to make video player take up the whole screen
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }

            mPositionTextView.setVisibility(View.GONE);
            mFabNext.setVisibility(View.GONE);
            mFabPrevious.setVisibility(View.GONE);
            mDescriptionTextView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Make UI elements appear again.
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }

            mPositionTextView.setVisibility(View.VISIBLE);
            mFabNext.setVisibility(View.VISIBLE);
            mFabPrevious.setVisibility(View.VISIBLE);
            mDescriptionTextView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.setMarginEnd(EXO_PLAYER_MARGIN_END);
            params.setMarginStart(EXO_PLAYER_MARGIN_START);
            params.height= EXO_PLAYER_HEIGHT;
            mPlayerView.setLayoutParams(params);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}

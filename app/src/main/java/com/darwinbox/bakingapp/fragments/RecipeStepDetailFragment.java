package com.darwinbox.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.activities.RecipeDetailActivity;
import com.darwinbox.bakingapp.models.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {

    private static final String RESUME_WINDOW_KEY = "resume_window_key";
    private static final String RESUME_POSITION_KEY = "resume_position_key";
    private String STEPS_POSITION_KEY = "steps_position_key";
    private String STEPS_LIST_KEY = "steps_list_key";
    private String PLAY_STATE = "playstate";

    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ArrayList<Step> steps = new ArrayList<>();
    private int position;
    private Button mPrevStep;
    private Button mNextstep;
    private ImageView thumbImage;
    private TextView textView;
    private int resumeWindow;
    private long resumePosition;
    private boolean isLandscape;
    private TrackSelector trackSelector;
    private ProgressBar progressBar;
    private boolean isPlayWhenReady = true;

    public RecipeStepDetailFragment() {

    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.recipe_step_detail_fragment_body_part, container,
                false);

        textView = rootView.findViewById(R.id.recipe_step_detail_text);
        thumbImage = rootView.findViewById(R.id.thumbImage);

        simpleExoPlayerView = rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        progressBar = rootView.findViewById(R.id.progress_bar);

        mPrevStep = rootView.findViewById(R.id.previousStep);
        mNextstep = rootView.findViewById(R.id.nextStep);

        isLandscape = textView == null;

        if(mPrevStep != null){
            mPrevStep.setOnClickListener(this);
        }

        if(mNextstep != null){
            mNextstep.setOnClickListener(this);
        }

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            String recipeName = ((RecipeDetailActivity) getActivity()).recipeName;
            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        progressBar.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            position = savedInstanceState.getInt(STEPS_POSITION_KEY);
            resumeWindow = savedInstanceState.getInt(RESUME_WINDOW_KEY ,
                    C.INDEX_UNSET);
            resumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY ,
                    C.TIME_UNSET);
            isPlayWhenReady = savedInstanceState.getBoolean(PLAY_STATE, false);
        }
        if (isLandscape) {
            //hide the actionbar if the layout is in landscape.
            ActionBar actionBar =  ((AppCompatActivity)getContext()).getSupportActionBar();
            if(null != actionBar){
                actionBar.hide();
            }
        }
        setData();
    }

    private void setData(){
        if (steps != null && !steps.isEmpty()) {
            Step bakingStep = steps.get(position);
            if (!isLandscape) {
                textView.setText(bakingStep.getDescription());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
        playVideo(steps.get(position));
    }

    private void initializePlayer() {
        if (player == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);
            player.addListener(mPlayerListener);
        }
    }

    private void playVideo(Step step){
        if(step.getVideoUrl() == null || step.getVideoUrl().isEmpty()){
            thumbImage.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);

            Uri builtUri = Uri.parse(step.getThumbnailUrl()).buildUpon().build();
            Picasso.with(getContext())
                    .load(builtUri)
                    .error(R.drawable.recipe)
                    .into(thumbImage);
        } else {
            thumbImage.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
        }

        String userAgent = Util.getUserAgent(getContext(), "Baking App");
        Uri mediaUri = Uri.parse(step.getVideoUrl());
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
        boolean isPlayerResumed =  resumeWindow != C.INDEX_UNSET;
        if(isPlayerResumed){
            player.seekTo(resumePosition);
        }
        player.prepare(mediaSource , !isPlayerResumed , false);
//        player.setPlayWhenReady(true);
        player.setPlayWhenReady(isPlayWhenReady);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);

        if (steps != null) {
            currentState.putParcelableArrayList(STEPS_LIST_KEY, steps);
            currentState.putInt(STEPS_POSITION_KEY, position);

            if(resumeWindow != C.INDEX_UNSET){
                currentState.putInt(RESUME_WINDOW_KEY , resumeWindow);
            }
            if(resumePosition != C.TIME_UNSET){
                currentState.putLong(RESUME_POSITION_KEY , resumePosition);
            }

            currentState.putBoolean(PLAY_STATE, isPlayWhenReady);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player = null;
    }

    @Override
    public void onClick(View view) {
        if(view == mPrevStep){
            if(position != 0){
                position--;
                resumeWindow = 0;
                resumePosition = 0;
            }

        }
        if(view == mNextstep){
            if(position != steps.size()-1){
                position++;
                resumeWindow = 0;
                resumePosition = 0;
            }
        }
        playVideo(steps.get(position));
        textView.setText(steps.get(position).getDescription());
    }

    @Override
    public void onPause() {
        super.onPause();

        updateResumePosition();
        stopAndReleasePlayer();
    }

    private void updateResumePosition() {
        if(player !=  null) {
            resumeWindow = player.getCurrentWindowIndex();
            resumePosition = Math.max(0, player.getContentPosition());
        }
    }

    private void stopAndReleasePlayer(){
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private final Player.EventListener mPlayerListener = new Player.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            if(playbackState == Player.STATE_BUFFERING){
                //show the loading Indicator
                progressBar.setVisibility(View.VISIBLE);
            }
            if(playbackState == Player.STATE_READY){
                //hide the loading indicator
                isPlayWhenReady = player.getPlayWhenReady();
                progressBar.setVisibility(View.INVISIBLE);
            }
            if(playbackState == Player.STATE_ENDED){
                //hide the loading indicator
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };
}

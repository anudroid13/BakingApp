package com.darwinbox.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darwinbox.bakingapp.models.StepsModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaActivity extends AppCompatActivity {

    @BindView(R.id.playerview)
    PlayerView playerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.txt_desc)
    TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        ButterKnife.bind(MediaActivity.this);
        Intent intent = getIntent();
        StepsModel model = intent.getParcelableExtra("model");

        txtDescription.setText(model.getDescription());

        if (model.getVideoUrl() != null && !model.getVideoUrl().equals("")) {

            // 1. Create a default TrackSelector
            Handler mainHandler = new Handler();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            SimpleExoPlayer player =
                    ExoPlayerFactory
                            .newSimpleInstance(MediaActivity.this, trackSelector);

            // Bind the player to the view.
            playerView.setPlayer(player);


// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new
                    DefaultDataSourceFactory(MediaActivity.this,
                    Util.getUserAgent(MediaActivity.this, "BakingApp"));

// This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(model.getVideoUrl()));
// Prepare the player with the source.
            player.prepare(videoSource);
            player.setPlayWhenReady(true);

            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);

                    if (playbackState == Player.STATE_BUFFERING) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        progressBar.setVisibility(View.GONE);
                    } else if (playbackState == Player.STATE_ENDED) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

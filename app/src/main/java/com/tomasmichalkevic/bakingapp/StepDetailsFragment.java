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
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tomasmichalkevic on 30/04/2018.
 */

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener{

    @BindView(R.id.step_description)
    TextView stepDescription;
    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;
    @BindView(R.id.step_image_view)
    ImageView stepImageView;

    private SimpleExoPlayer simpleExoPlayer;
    private PlaybackState.Builder mStateBuilder;
    private static MediaSession mediaSession;
    private Step step;
    private String videoURL = "";
    private long position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_details, container, false);
        ButterKnife.bind(this, rootView);

        if(step != null){
            stepDescription.setText(step.getDescription());
            if(!step.getVideoURL().equals("")){
                Log.i("TESTING", "onCreateView: " + step.getVideoURL());
                if(getFileType(step.getVideoURL()).equals("mp4")){
                    stepImageView.setVisibility(View.GONE);
                    videoURL = step.getVideoURL();
                    initializePlayer(Uri.parse(videoURL));
                }else if(step.getThumbnailURL().equals("jpg") || step.getThumbnailURL().equals("png")){
                    ((ViewManager)playerView.getParent()).removeView(playerView);
                    stepImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(step.getThumbnailURL()).into(stepImageView);
                }else{
                    ((ViewManager)playerView.getParent()).removeView(playerView);
                    stepImageView.setVisibility(View.GONE);
                }
            }else{
                ((ViewManager)playerView.getParent()).removeView(playerView);
                stepImageView.setVisibility(View.GONE);
            }

        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            stepDescription.setText(savedInstanceState.getString("stepDescription"));
            videoURL = savedInstanceState.getString("videoURL");
            initializePlayer(Uri.parse(videoURL));
            simpleExoPlayer.seekTo(savedInstanceState.getLong("position", C.TIME_UNSET));
            //simpleExoPlayer.setPlayWhenReady(savedInstanceState.getInt("player_state"));
            if(savedInstanceState.getLong("position", C.TIME_UNSET)!=0){
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoURL.equals(""))
            initializePlayer(Uri.parse(videoURL));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            position = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stepDescription", stepDescription.getText().toString());
        outState.putString("videoURL", videoURL);
        outState.putLong("position", position);
    }

    public void setData(Step step) {
        this.step = step;
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(simpleExoPlayer);
            String userAgent = "Agent";
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null,
                    null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    private static String getFileType(String url){
        String[] result = url.split("\\.");
        if(result.length>1){
            return result[result.length-1];
        }else{
            return result[0];
        }

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
            mStateBuilder.setState(PlaybackState.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackState.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}

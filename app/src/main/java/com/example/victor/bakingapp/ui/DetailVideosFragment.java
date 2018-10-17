package com.example.victor.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.StepItem;
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

/******
 * Created by Victor on 8/27/2018.
 ******/
public class DetailVideosFragment extends Fragment
        implements ExoPlayer.EventListener {

    private static final String LOG_TAG = DetailVideosFragment.class.getSimpleName();
    View rootView;
    static int stepIndex;
    static ArrayList<StepItem> stepItems;

    private static final String SAVED_STATE_EXOPLAYER_POSITION = "savedStateExoplayerPosition";
    private static MediaSessionCompat bakingAppMediaSession;
    //SaveInstanceState
    private static Bundle mSavedInstanceState = null;
    private static long exoplayerPosition = 0;
    //ExoPlayer & MediaSession
    private SimpleExoPlayer bakingAppExoPlayer;
    //    private NotificationManager notificationManager;
    private SimpleExoPlayerView simpleExoPlayerView;
    private PlaybackStateCompat.Builder bakingAppStateBuilder;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_video, container, false);
        simpleExoPlayerView = rootView.findViewById(R.id.fragment_recipe_detail_exoplayer);

        mSavedInstanceState = savedInstanceState;
        populateDetailFragment(stepItems, stepIndex);
        context = getContext();
        initializeMediaSession();
        return rootView;
    }

    private void initializeMediaSession() {
        //Create MediaSessionCompat object
        bakingAppMediaSession = new MediaSessionCompat(context, LOG_TAG);
        //Set Flags
        bakingAppMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //Set an optional Media Button Receiver component
        bakingAppMediaSession.setMediaButtonReceiver(null);
        //Set the available actions and initial state
        bakingAppStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        bakingAppMediaSession.setPlaybackState(bakingAppStateBuilder.build());
        //Set callbacks
        bakingAppMediaSession.setCallback(new BakingAppSessionCallback());
        //Start session
        bakingAppMediaSession.setActive(true);
    }

    public void populateDetailFragment(ArrayList<StepItem> stepItems, int stepIndex) {
        TextView detailVideoView = rootView.findViewById(R.id.fragment_recipe_detail_videos);
        TextView detailDescriptionView = rootView.findViewById(R.id.fragment_recipe_detail_description);

        StepItem stepItem = stepItems.get(stepIndex);

        //Set video
        String recipeUrl;
        if (stepItem.getStepVideoUrl() != null && stepItem.getStepVideoUrl().length() > 0) {
            detailVideoView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            recipeUrl = stepItem.getStepVideoUrl();
            //Initialize exoplayer
            initializePlayer(Uri.parse(recipeUrl));
        } else if (stepItem.getStepThumbnailUrl() != null && stepItem.getStepThumbnailUrl().length() > 0) {
            detailVideoView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            recipeUrl = stepItem.getStepThumbnailUrl();
            initializePlayer(Uri.parse(recipeUrl));
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
            detailVideoView.setVisibility(View.VISIBLE);
            recipeUrl = rootView.getResources().getString(R.string.no_visual_information_available);
        }
        detailVideoView.setText(recipeUrl);

        //Set description
        String description;
        if (stepItem.getStepDescription() != null && stepItem.getStepDescription().length() > 0) {
            description = stepItem.getStepDescription();
        } else {
            description = rootView.getResources().getString(R.string.no_description_available);
        }
        detailDescriptionView.setText(description);
    }

    private void initializePlayer(Uri mediaUri) {
        if (bakingAppExoPlayer == null) {
            //Create ExoPlayer instance
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            bakingAppExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(bakingAppExoPlayer);

            if (mSavedInstanceState != null) {
                exoplayerPosition = mSavedInstanceState.getLong(SAVED_STATE_EXOPLAYER_POSITION);
            }

            bakingAppExoPlayer.seekTo(exoplayerPosition);
            //Prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            bakingAppExoPlayer.prepare(mediaSource);
            bakingAppExoPlayer.setPlayWhenReady(true);
        }

        // Set the ExoPlayer.EventListener to this fragment.
        bakingAppExoPlayer.addListener(this);
    }

    private void releasePlayer() {
        bakingAppExoPlayer.stop();
        bakingAppExoPlayer.release();
        bakingAppExoPlayer = null;
        bakingAppMediaSession.setActive(false);
//        notificationManager.cancelAll();
    }

    public void setStepIndex(int stepIndex) {
        DetailVideosFragment.stepIndex = stepIndex;
    }

    public void setStepItems(ArrayList<StepItem> stepItems) {
        DetailVideosFragment.stepItems = stepItems;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bakingAppExoPlayer != null) {
            exoplayerPosition = bakingAppExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVED_STATE_EXOPLAYER_POSITION, exoplayerPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        exoplayerPosition = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bakingAppExoPlayer != null) {
            releasePlayer();
        }
    }

    // Exoplayer Event Listener Methods
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
        //Ensure media session is synced with internal exoplayer client
        if (playWhenReady && (playbackState == ExoPlayer.STATE_READY)) {
            bakingAppStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, bakingAppExoPlayer.getCurrentPosition(), 1);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            bakingAppStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, bakingAppExoPlayer.getCurrentPosition(), 1);
        }
        bakingAppMediaSession.setPlaybackState(bakingAppStateBuilder.build());
        //Call the method in the onPlayerStateChanged
//        showNotification(bakingAppStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    //Media Session Callbacks
    private class BakingAppSessionCallback extends MediaSessionCompat.Callback {
        //Ensure external clients can control exoplayer
        @Override
        public void onPlay() {
            bakingAppExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            bakingAppExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            bakingAppExoPlayer.seekTo(0);
        }
    }

//    private void showNotification(PlaybackStateCompat state) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
//
//        //Use state to define if you are using play or pause action
//        int icon;
//        String play_pause;
//        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
//            icon = R.drawable.exo_controls_pause;
//            play_pause = "pause";
//        } else {
//            icon = R.drawable.exo_controls_play;
//            play_pause = "play";
//        }
//
//        //Create two notification actions, use mediaButtonPendingIntents to pass actions
//        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
//                icon,
//                play_pause,
//                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(), PlaybackStateCompat.ACTION_PLAY_PAUSE));
//
//        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat.Action(
//                R.drawable.exo_controls_previous,
//                "restart",
//                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
//
//        //Create a contentPendingIntent that relaunches activity on clicked
//        PendingIntent contentPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), DetailVideosFragment.class), 0);
//
//
//        builder.setContentTitle("content title")
//                .setContentText("content text")
//                .setSmallIcon(R.drawable.arrow)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(restartAction)
//                .addAction(playPauseAction)
//                //set MediaSessionToken
//                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
//                        .setMediaSession(bakingAppMediaSession.getSessionToken())
//                        .setShowActionsInCompactView(0,1));
//
//        //use NotificationManager to deliver the notification
//        notificationManager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//    }

//    public static class MediaReceiver extends BroadcastReceiver {
//        public MediaReceiver() {}
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MediaButtonReceiver.handleIntent(bakingAppMediaSession, intent);
//        }
//    }
}


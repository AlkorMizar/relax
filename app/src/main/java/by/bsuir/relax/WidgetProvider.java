package by.bsuir.relax;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import by.bsuir.relax.ui.music.MusicViewModel;
import by.bsuir.relax.user.Mood;

public class WidgetProvider extends android.appwidget.AppWidgetProvider {

    enum PLAYER_STATE{ GONNA_PLAY, GONNA_RESUME, GONNA_PAUSE};
    private static PLAYER_STATE currState = PLAYER_STATE.GONNA_PLAY;

    private Mood currentMood=Mood.Calm;

    private static final String MyOnClick0 = "0";
    private static final String MyOnClick1 = "1";
    private static final String MyOnClick2 = "2";
    private static final String MyOnClick3 = "3";
    private static final String MyOnClick4 = "4";
    private static final String MyOnClick5 = "5";
    private static final String MyOnClick6 = "6";

    private static final String prevSong = "previous";
    private static final String stopPlaySong = "stopPlay";
    private static final String nextSong = "next";

    private static String songName = "Kurdt Cobain - Sappy";

    private RemoteViews remoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            remoteViews.setTextViewText(R.id.songArtistWidget, songName);

            if (currState == PLAYER_STATE.GONNA_PLAY || currState == PLAYER_STATE.GONNA_RESUME){
                remoteViews.setImageViewResource(R.id.playStopSongWidget, R.drawable.ic_baseline_play_arrow_24);
            } else {
                remoteViews.setImageViewResource(R.id.playStopSongWidget, R.drawable.ic_pause);
            }

            remoteViews.setOnClickPendingIntent(R.id.calmWidget, getPendingSelfIntent(context, MyOnClick0));
            remoteViews.setOnClickPendingIntent(R.id.relaxWidget, getPendingSelfIntent(context, MyOnClick1));
            remoteViews.setOnClickPendingIntent(R.id.concWidget, getPendingSelfIntent(context, MyOnClick2));
            remoteViews.setOnClickPendingIntent(R.id.agitWidget, getPendingSelfIntent(context, MyOnClick3));
            remoteViews.setOnClickPendingIntent(R.id.sadWidget, getPendingSelfIntent(context, MyOnClick4));
            remoteViews.setOnClickPendingIntent(R.id.happyWidget, getPendingSelfIntent(context, MyOnClick5));
            remoteViews.setOnClickPendingIntent(R.id.angryWidget, getPendingSelfIntent(context, MyOnClick6));

            remoteViews.setOnClickPendingIntent(R.id.previousSongWidget, getPendingSelfIntent(context, prevSong));
            remoteViews.setOnClickPendingIntent(R.id.playStopSongWidget, getPendingSelfIntent(context, stopPlaySong));
            remoteViews.setOnClickPendingIntent(R.id.nextSongWidget, getPendingSelfIntent(context, nextSong));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentName thisWidget = new ComponentName(context, AppWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        if (MyOnClick0.equals(intent.getAction())) {
            currentMood=Mood.Calm;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        } else if (MyOnClick1.equals(intent.getAction())) {
            currentMood=Mood.Relaxed;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        } else if (MyOnClick2.equals(intent.getAction())) {
            currentMood=Mood.Concentrated;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        } else if (MyOnClick3.equals(intent.getAction())) {
            currentMood=Mood.Agitated;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        } else if (MyOnClick4.equals(intent.getAction())) {
            currentMood=Mood.Sad;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        } else if (MyOnClick5.equals(intent.getAction())) {
            currentMood=Mood.Happy;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        }if (MyOnClick6.equals(intent.getAction())) {
            currentMood=Mood.Angry;
            currState = PLAYER_STATE.GONNA_PLAY;
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        }


        else if (prevSong.equals(intent.getAction())) {
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        }




        else if (stopPlaySong.equals(intent.getAction())) {

            if (currState == PLAYER_STATE.GONNA_PLAY){
                currState = PLAYER_STATE.GONNA_PAUSE;

                getSpotifyAccess(context, appWidgetManager, appWidgetIds);
            }

            else if (currState == PLAYER_STATE.GONNA_PAUSE){
                currState = PLAYER_STATE.GONNA_RESUME;

                mSpotifyAppRemote.getPlayerApi().pause();
            }

            else if (currState == PLAYER_STATE.GONNA_RESUME){
                currState = PLAYER_STATE.GONNA_PAUSE;

                mSpotifyAppRemote.getPlayerApi().resume();
            }


        }






        else if (nextSong.equals(intent.getAction())) {
            if (mSpotifyAppRemote != null) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        } else{
            super.onReceive(context, intent);
        }


        onUpdate(context, appWidgetManager, appWidgetIds);
    }




    private void getSpotifyAccess(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        connected(context, appWidgetManager, appWidgetIds);
                        System.out.println("asbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                    }
                });
    }


    private void connected(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        mSpotifyAppRemote.getPlayerApi().play(SUPP_PLAYLIST_PLAY_STR + MusicViewModel.moodMusic.get(currentMood));

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        songName = track.artist.name + " - " + track.name;
                        onUpdate(context, appWidgetManager, appWidgetIds);
                    }
                });
    }






    private static final String CLIENT_ID = "7cf7883fb9e4417ba0514bb03e50ec82";
    private static final String REDIRECT_URI = "https://localhost:8080";
    private static SpotifyAppRemote mSpotifyAppRemote = null;

    public static final String SUPP_PLAYLIST_PLAY_STR = "spotify:playlist:";
}
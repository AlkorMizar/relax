package by.bsuir.relax.ui.music;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;

import java.util.Date;
import java.util.HashMap;

import by.bsuir.relax.R;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.UserDistributor;

public class MusicFragment extends Fragment {

    private boolean gonna_play = false;
    private static final String CLIENT_ID = "7cf7883fb9e4417ba0514bb03e50ec82";
    private static final String REDIRECT_URI = "https://localhost:8080";
    public static SpotifyAppRemote mSpotifyAppRemote = null;

    public static final String SUPP_PLAYLIST_PLAY_STR = "spotify:playlist:";
    public static HashMap<Mood,String> moodMusic;

    static {
        moodMusic=new HashMap<>();
        moodMusic.put(Mood.Calm,"0vvXsWCC9xrXsKd4FyS8kM");
        moodMusic.put(Mood.Happy,"4M0aH9xVLOilYreFAsGMQy");
        moodMusic.put(Mood.Agitated,"1r4hnyOWexSvylLokn2hUa");
        moodMusic.put(Mood.Angry,"37i9dQZF1DX1s9knjP51Oa");
        moodMusic.put(Mood.Concentrated,"471N195f5jAVs086lzYglw");
        moodMusic.put(Mood.Relaxed,"3ksy3Zso4vdt4JIzTYvpF9");
        moodMusic.put(Mood.Sad,"3i387fsvfse1cWivqio5NI");
    }

    private TextView
            playlistName,
            albumName,
            artistName,
            songName;

    private ImageView
            coverImage,
            previousSong,
            playStopSong,
            nextSong;

    private void findAllTextViews(View view){
        playlistName = view.findViewById(R.id.playlistName);
        albumName = view.findViewById(R.id.albumName);
        artistName = view.findViewById(R.id.artistName);
        songName = view.findViewById(R.id.songName);
    }

    private void findAllImageViews(View view){
        coverImage = view.findViewById(R.id.coverImage);
        previousSong = view.findViewById(R.id.previousSong);
        playStopSong = view.findViewById(R.id.playStopSong);
        nextSong = view.findViewById(R.id.nextSong);
    }



    private int current_index = 0;

    private void connected(){
        current_index = 0 ;
        String playlist=moodMusic.get(UserDistributor.getUser().getCurrentMood());
        mSpotifyAppRemote.getPlayerApi().play(SUPP_PLAYLIST_PLAY_STR + playlist);

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        artistName.setText(track.artist.name);
                        songName.setText(track.name);
                        albumName.setText(track.album.name);
                        playlistName.setText(UserDistributor.getUser().getCurrentMood().name());
                        mSpotifyAppRemote
                                .getImagesApi()
                                .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
                                .setResultCallback(
                                        bitmap -> {
                                            coverImage.setImageBitmap(bitmap);
                                        });
                    }
                });
    }

    public static void previousMusic(){
        if (mSpotifyAppRemote != null) {
            mSpotifyAppRemote.getPlayerApi().skipPrevious();
        }
    }

    public static void stopMusic(){
        if (mSpotifyAppRemote != null) {
            mSpotifyAppRemote.getPlayerApi().pause();
        }
    }

    public static void playMusic(){
        if (mSpotifyAppRemote != null) {
            mSpotifyAppRemote.getPlayerApi().resume();
        }
    }

    public static void nextMusic(){
        if (mSpotifyAppRemote != null) {
            mSpotifyAppRemote.getPlayerApi().skipNext();
        }
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MusicFragment() {   }
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        findAllTextViews(view);
        findAllImageViews(view);

        previousSong.setOnClickListener(lambda->{
            previousMusic();
        });

        playStopSong.setOnClickListener(lambda->{
            if (gonna_play){
                playMusic();
                gonna_play = false;
                playStopSong.setImageResource(R.drawable.ic_pause);
            } else{
                stopMusic();
                gonna_play = true;
                playStopSong.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        });

        nextSong.setOnClickListener(lambda->{
            nextMusic();
        });



        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                    }
                });

        return view;
    }
}

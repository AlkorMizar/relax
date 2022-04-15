package by.bsuir.relax.ui.music;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import by.bsuir.relax.user.Mood;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MusicViewModel {
    public static final String CLIENT_ID = "7cf7883fb9e4417ba0514bb03e50ec82";
    public static final String REDIRECT_URI = "https://localhost:8080";
    public static String USER_ID="31u6nwltsgearrymfr5zsicbyty4";
    public static final int REQUEST_CODE = 1337;
    private String ACCESS_TOKEN;
    private SpotifyAppRemote mSpotifyAppRemote;
    public static HashMap<Mood,String> moodMusic;

    private ReentrantLock lock;
    private ReentrantLock tokenLock;

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

    MusicViewModel(){
        super();
        tokenLock=new ReentrantLock();
        lock=new ReentrantLock();
        lock.lock();
        tokenLock.lock();
    }

    public void setToken(String token){
        tokenLock.unlock();
        ACCESS_TOKEN=token;
    }
    public boolean isTokenSet(){
        return ACCESS_TOKEN!=null;
    }

    public SpotifyAppRemote getmSpotifyAppRemote() {
        lock.lock();
        lock.unlock();
        return mSpotifyAppRemote;
    }

    public void setmSpotifyAppRemote(SpotifyAppRemote mSpotifyAppRemote) {
        lock.unlock();
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }

    public LiveData<List<PlaylistTrack>> getMusic(Mood mood){
        List<PlaylistTrack> list=new ArrayList<>();
        MutableLiveData<List<PlaylistTrack>> playlist=new MutableLiveData<>();
        SpotifyApi API = new SpotifyApi();
        tokenLock.lock();
        tokenLock.unlock();
        System.out.println(ACCESS_TOKEN);
        System.out.println("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        API.setAccessToken(ACCESS_TOKEN);
        SpotifyService spotify = API.getService();

        spotify.getPlaylist(USER_ID, moodMusic.get(mood), new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Pager<PlaylistTrack> tracks = playlist.tracks;
                List<PlaylistTrack> songs = tracks.items;

                Date d=new Date();
                Random random = new Random(d.getTime());

                for (int i=0;i<15;i++) {
                    list.add(songs.get(random.nextInt(songs.size())));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                playlist.setValue(null);
            }

        });
        return  playlist;
    }
}

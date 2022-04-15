package by.bsuir.relax.ui.music;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import by.bsuir.relax.R;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.UsersMood;
import by.bsuir.relax.util.DrawableUtil;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class MusicAdapter extends BaseAdapter {
    Context ctx;
    FragmentActivity activity;
    LayoutInflater lInflater;
    List<PlaylistTrack> objects;
    MusicViewModel model;
    Track currTrack;
    boolean paused=false;
    Callback callback;

    MusicAdapter(Context context, FragmentActivity activity, List<PlaylistTrack> moods, MusicViewModel model) {
        ctx = context;
        this.activity = activity;
        objects = moods;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.model=model;
        callback= () -> {};
    }


    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        final View view ;
        if (convertView == null) {
            view = lInflater.inflate(R.layout.mood_list_item, parent, false);
        }else{
            view = convertView;
        }
        PlaylistTrack p = getProduct(position);
        ((TextView)view.findViewById(R.id.musicAuthor)).setText(p.track.artists.get(0).toString());
        ((TextView)view.findViewById(R.id.musicName)).setText(p.track.name);
        ((TextView)view.findViewById(R.id.musicTime)).setText(p.track.duration_ms/1000/60 +":"+( p.track.duration_ms/1000%60));
        DrawableUtil.loadImageOnImageView(((ImageView)view.findViewById(R.id.imageMusic)),p.track.album.images.get(0).url);


        ((ImageView)view.findViewById(R.id.imageMusic)).setOnClickListener(view1 -> {
            if (currTrack!=p.track){
                model.getmSpotifyAppRemote().getPlayerApi().play(p.track.uri);
                callback.onChange();
                paused=false;
                currTrack=p.track;

                callback= () -> {
                    view.findViewById(R.id.musicPaused).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.musicPlaying).setVisibility(View.INVISIBLE);
                };
            }else if(paused){
                paused=false;
                model.getmSpotifyAppRemote().getPlayerApi().resume();
            }else {
                paused=true;
                model.getmSpotifyAppRemote().getPlayerApi().pause();
            }
            view.findViewById(R.id.musicPaused).setVisibility(paused?View.INVISIBLE:View.VISIBLE);
            view.findViewById(R.id.musicPlaying).setVisibility(!paused?View.INVISIBLE:View.VISIBLE);
        });

        view.findViewById(R.id.goBack).setOnClickListener(view1 -> {
            currTrack=getProduct(position-1).track;

            model.getmSpotifyAppRemote().getPlayerApi().play(currTrack.uri);
            callback.onChange();
            paused=false;

            callback= () -> {
                view.findViewById(R.id.musicPaused).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.musicPlaying).setVisibility(View.INVISIBLE);
            };

            view.findViewById(R.id.musicPaused).setVisibility(paused?View.INVISIBLE:View.VISIBLE);
            view.findViewById(R.id.musicPlaying).setVisibility(!paused?View.INVISIBLE:View.VISIBLE);
        });

        view.findViewById(R.id.goBack).setOnClickListener(view1 -> {
            currTrack=getProduct(position+1).track;

            model.getmSpotifyAppRemote().getPlayerApi().play(currTrack.uri);
            callback.onChange();
            paused=false;

            callback= () -> {
                view.findViewById(R.id.musicPaused).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.musicPlaying).setVisibility(View.INVISIBLE);
            };

            view.findViewById(R.id.musicPaused).setVisibility(paused?View.INVISIBLE:View.VISIBLE);
            view.findViewById(R.id.musicPlaying).setVisibility(!paused?View.INVISIBLE:View.VISIBLE);
        });

        return view;
    }

    // товар по позиции
    PlaylistTrack getProduct(int position) {
        return ((PlaylistTrack) getItem((position+getCount())%getCount()));
    }

    public void onDestroy(){
        model.getmSpotifyAppRemote().getPlayerApi().pause();
    }
}

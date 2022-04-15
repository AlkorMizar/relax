package by.bsuir.relax.ui.recommended;

import android.util.Pair;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import by.bsuir.relax.R;
import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.user.Mood;

public class RecommendViewModel extends ViewModel {
    HashMap<Mood, Pair<String,String>> moodAdvice;
    String[] moodSwings;
    public RecommendViewModel(){
        super();

        moodAdvice= new HashMap<>();
        String[] name=RelaxApplication.getAppContext().getResources().getStringArray(R.array.moodName);
        String[] titles=RelaxApplication.getAppContext().getResources().getStringArray(R.array.moodTitles);
        String[] descriptions=RelaxApplication.getAppContext().getResources().getStringArray(R.array.moodContent);
        for (int i = 0; i < titles.length; i++) {
            moodAdvice.put(Mood.valueOf(name[i]),new Pair<>(titles[i],descriptions[i]));
        }

        moodSwings=RelaxApplication.getAppContext().getResources().getStringArray(R.array.mood_swing);
    }

    public Pair<String,String> getByMood(Mood mood){
        return  moodAdvice.get(mood);
    }

    public Pair<String,String> getSwing(float swing){
        if (swing<5.1){
            return  new Pair<>("Stability",moodSwings[0]);
        }
        return  new Pair<>("Mood swings",moodSwings[1]);
    }

}

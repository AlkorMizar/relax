package by.bsuir.relax.ui.mental;

import android.content.res.TypedArray;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import by.bsuir.relax.R;
import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.user.Mood;

public class MentalRecommViewModel {
    private HashMap<Mood,InfoModel> recommendations;

    MentalRecommViewModel(){
        recommendations= new HashMap<>();
        Mood[] moods={Mood.Sad,Mood.Happy,Mood.Concentrated,Mood.Relaxed,Mood.Calm,Mood.Agitated,Mood.Angry};
        TypedArray imgs = RelaxApplication.getAppContext().getResources().obtainTypedArray(R.array.drawableRecom);
        String[] titles=RelaxApplication.getAppContext().getResources().getStringArray(R.array.textRecom);
        for (int i = 0; i < titles.length; i++) {
            recommendations.put(moods[i],new InfoModel(titles[i],imgs.getResourceId(i,0)));
        }
        imgs.recycle();
    }

    public InfoModel getByMood(Mood i){
        return recommendations.get(i);
    }
}

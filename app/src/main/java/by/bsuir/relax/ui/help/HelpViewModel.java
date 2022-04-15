package by.bsuir.relax.ui.help;

import android.content.res.TypedArray;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;
import java.util.List;

import by.bsuir.relax.R;
import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.net.AstrologyAPI;

public class HelpViewModel{

    private List<InfoModel> sign;

    HelpViewModel(){
        sign= new ArrayList<>();
        TypedArray imgs = RelaxApplication.getAppContext().getResources().obtainTypedArray(R.array.drawable);
        String[] titles=RelaxApplication.getAppContext().getResources().getStringArray(R.array.titles);
        String[] descriptions=RelaxApplication.getAppContext().getResources().getStringArray(R.array.description);
        for (int i = 0; i < titles.length; i++) {
            sign.add(new InfoModel(titles[i],descriptions[i],imgs.getResourceId(i,0)));
        }
        imgs.recycle();
    }

    public InfoModel getHelpPage(int i){
        return sign.get(i);
    }

    public int size(){
        return sign.size();
    }
}
package by.bsuir.relax.ui.sign;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.net.AstrologyAPI;
import by.bsuir.relax.net.Sign;

public class SignViewModel extends ViewModel {
    private MutableLiveData<Sign> sign;
    private final AstrologyAPI astrologyAPI=new AstrologyAPI();;

    public LiveData<Sign> getSign(int i) {
        sign=new MutableLiveData<>();
        loadSign(i);
        return sign;
    }

    private void loadSign(int i) {
        if (astrologyAPI.getSigns()[i]==null){
            Runnable astrTask=()->{
                astrologyAPI.load(i);
            };
            Thread t=new Thread(astrTask);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sign.setValue(astrologyAPI.getSigns()[i]);
    }
}
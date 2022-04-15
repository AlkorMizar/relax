package by.bsuir.relax.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import by.bsuir.relax.RegistrActivity;
import by.bsuir.relax.databinding.FragmentRegIntroBinding;
import by.bsuir.relax.ui.greetings.GreetingsFragment;

public class IntroFragment extends Fragment {
    private FragmentRegIntroBinding binding;
    private Timer timer = new Timer();
    RegistrActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegIntroBinding.inflate(inflater, container, false);
        activity = (RegistrActivity) this.getActivity();
        View root = binding.getRoot();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadNext();
            }
        }, 2000, 1);
        return root;
    }

    private void loadNext(){
        timer.cancel();
        timer=null;
        activity.replaceFragment(new GreetingsFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

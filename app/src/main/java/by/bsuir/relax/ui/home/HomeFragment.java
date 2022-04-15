package by.bsuir.relax.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import by.bsuir.relax.R;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.ui.mental.MentalRecommFragment;
import by.bsuir.relax.ui.recommended.RecommendedFragment;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.databinding.FragmentHomeBinding;
import by.bsuir.relax.net.AstrologyAPI;
import by.bsuir.relax.ui.sign.SignSlider;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.horoscBtn.setOnClickListener(view -> {
            System.out.println("hello");
            Runnable task=()->{
                if (AstrologyAPI.canConnect()) {
                    goToHoroscope();
                }else {
                    Toast.makeText(container.getContext(),"Can't connect to space",Toast.LENGTH_LONG).show();
                }
            };
            (new Thread(task)).start();
        });

        binding.recomendBtn.setOnClickListener(view -> {
            //getActivity().setContentView(R.layout.fragment_big);
            FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.nav_host_fragment_content_main,new MentalRecommFragment()).addToBackStack( "tag" ).commit();
        });

        binding.textView.setText("Welcome back, "+ UserDistributor.getUser().getName()+"!");

        binding.agitatedMood.setOnClickListener(view ->  onChangeToAgitated());
        binding.calmMood.setOnClickListener(view -> onChangeToCalm());
        binding.angryMood.setOnClickListener(view -> onChangeToAngry());
        binding.concentratedMood.setOnClickListener(view -> onChangeToConcentrated());
        binding.happyMood.setOnClickListener(view -> onChangeToHappy());
        binding.sadMood.setOnClickListener(view -> onChangeToSad());
        binding.relaxedMood.setOnClickListener(view -> onChangeToRelaxed());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void goToHoroscope(){
        System.out.println("Intent");
        Intent intent = new Intent(getContext(), SignSlider.class);
        startActivity(intent);
    }

    private void setMood(Mood mood){
        UserDistributor.getUser().setCurrentMood(mood);
        DAOFactory.getDAO().addMood(UserDistributor.getUser(),mood, LocalDateTime.now());
    }

    public void onChangeToCalm() {
        setMood(Mood.Calm);
    }

    public void onChangeToRelaxed() {
        setMood(Mood.Relaxed);
    }

    public void onChangeToConcentrated() {
        setMood(Mood.Concentrated);
    }

    public void onChangeToAgitated() {
        setMood(Mood.Agitated);
    }

    public void onChangeToSad() {
        setMood(Mood.Sad);
    }

    public void onChangeToHappy() {
        setMood(Mood.Happy);
    }

    public void onChangeToAngry() {
        setMood(Mood.Angry);
    }

}
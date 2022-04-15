package by.bsuir.relax.ui.recommended;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.databinding.FragmentRecommendBinding;
import by.bsuir.relax.ui.sign.SignViewModel;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.user.UsersMood;

public class RecommendedFragment extends Fragment {
    private FragmentRecommendBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecommendViewModel model = new ViewModelProvider(this).get(RecommendViewModel.class);

        binding = FragmentRecommendBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        Pair<Mood,Float> moodRecomm=calculateMood();
        Pair<String,String> advice=model.getByMood(moodRecomm.first);

        binding.moodAdvTitle.setText(advice.first);
        binding.moodAdvCont.setText(advice.second);

        advice=model.getSwing(moodRecomm.second);

        binding.moodSwing.setText(advice.first);
        binding.moodSwCont.setText(advice.second);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Pair<Mood,Float> calculateMood(){
        HashMap<Mood,Integer> moodsMap= new HashMap<>();
        LocalDateTime date=LocalDateTime.now();
        float moodswing=0,amount=0;
        date.minusDays(5);

        List<UsersMood> moods= DAOFactory.getDAO().getUsersMoods(UserDistributor.getUser());
        UsersMood prev=moods.get(0);

        for (UsersMood mood:moods ) {
            System.out.println("+");
            if (mood.getDate().isAfter(date)){
                break;
            }
            moodswing+=(mood.getMood().weight-prev.getMood().weight);
            prev=mood;
            amount++;
            moodsMap.compute(mood.getMood(), (k, v) -> (v == null) ? 1 : v+1);
        }


        moodswing=amount==0?0:moodswing/amount;
        Optional<Map.Entry<Mood, Integer>> maxMoodOpt=moodsMap.entrySet().stream().max((Map.Entry.comparingByValue()));

        Mood maxMood=maxMoodOpt.isPresent()?maxMoodOpt.get().getKey() : Mood.Happy;

        return new Pair<>(maxMood,moodswing);
    }
}

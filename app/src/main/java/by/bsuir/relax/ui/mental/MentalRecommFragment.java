package by.bsuir.relax.ui.mental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import by.bsuir.relax.databinding.FragmentMentalRecommBinding;
import by.bsuir.relax.databinding.FragmentRecommendBinding;
import by.bsuir.relax.ui.recommended.RecommendViewModel;
import by.bsuir.relax.user.UserDistributor;

public class MentalRecommFragment extends Fragment {

    FragmentMentalRecommBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MentalRecommViewModel model = new MentalRecommViewModel();

        binding = FragmentMentalRecommBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        InfoModel m=model.getByMood(UserDistributor.getUser().getCurrentMood());
        binding.recomText.setText(m.getText());
        binding.recomImg.setImageResource(m.getDrawable());

        return root;
    }
}

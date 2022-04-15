package by.bsuir.relax.ui.sign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import by.bsuir.relax.databinding.FragmentSignBinding;

public class SignFragment extends Fragment {

    private int ind;
    private FragmentSignBinding binding;

    public SignFragment(int i){
        ind=i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SignViewModel model = new ViewModelProvider(this).get(SignViewModel.class);

        binding = FragmentSignBinding.inflate(inflater, container, false);

        model.getSign(ind).observe(getViewLifecycleOwner(), sign -> {
            System.out.println(sign.toString());
            binding.zodName.setText(sign.name.toUpperCase());
            binding.dateRange.setText(sign.dateRange);
            binding.compab.setText(sign.compatibility);
            binding.descr.setText(sign.description);
            binding.signMood.setText(sign.mood);
            binding.signColor.setText(sign.color);
            binding.luckyNum.setText(sign.luckyNumber);
            binding.luckyTime.setText(sign.luckyTime);
        });
        return binding.getRoot();
    }
}
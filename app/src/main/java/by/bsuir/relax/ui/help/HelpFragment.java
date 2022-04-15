package by.bsuir.relax.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import by.bsuir.relax.databinding.FragmentHelpBinding;

public class HelpFragment extends Fragment {

    private int ind;
    private FragmentHelpBinding binding;

    public HelpFragment(int i){
        ind=i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HelpViewModel model = new HelpViewModel();

        binding = FragmentHelpBinding.inflate(inflater, container, false);

        InfoModel m=model.getHelpPage(ind);
        binding.titleForHelp.setText(m.title);
        binding.textForHelp.setText(m.description);
        binding.imgForHelp.setImageResource(m.img);

        return binding.getRoot();
    }
}
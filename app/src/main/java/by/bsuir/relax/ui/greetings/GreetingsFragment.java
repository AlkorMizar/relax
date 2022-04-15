package by.bsuir.relax.ui.greetings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import by.bsuir.relax.RegistrActivity;
import by.bsuir.relax.databinding.FragmentGreetingsBinding;
import by.bsuir.relax.ui.login.LogInFragment;
import by.bsuir.relax.ui.signin.SignInFragment;

public class GreetingsFragment extends Fragment {
    private FragmentGreetingsBinding binding;
    RegistrActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGreetingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity= (RegistrActivity) getActivity();

        binding.logIn.setOnClickListener(view -> {
            activity.replaceFragment(new LogInFragment());
        });

        binding.sinIn.setOnClickListener(view -> {
            activity.replaceFragment(new SignInFragment());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package by.bsuir.relax.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import by.bsuir.relax.MainActivity;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.user.User;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.databinding.FragmentLogInBinding;

public class LogInFragment extends Fragment {
    private FragmentLogInBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.logInBtn.setOnClickListener(view -> {

            User user= DAOFactory.getDAO().getIfRegistered(binding.emailInp.getText().toString(),binding.passInp.getText().toString());
            if(user!=null){
                UserDistributor.login(user);
                Intent intent = new Intent(container.getContext(), MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"Such user doesn't exist", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}